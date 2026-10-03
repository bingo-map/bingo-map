document.addEventListener("DOMContentLoaded", function () {

    let myEmail = null;
    let canManageRoles = false;

    fetch("/api/admin/access")
        .then((res) => {
            if (!res.ok) throw new Error("denied");
            return res.json();
        })
        .then((access) => {
            canManageRoles = access.role === "ADMIN";
            if (!canManageRoles) {
                document.querySelector(".admin-sidebar h3").textContent = "매니저 페이지";
            }
        })
        .catch(() => { window.location.href = "/login"; });

    // 1) 대시보드 통계 로드 (회원 수는 실제 값, 나머지는 기능 연동 전이라 0)
    fetch("/api/admin/dashboard")
        .then((res) => {
            if (!res.ok) throw new Error("failed");
            return res.text();
        })
        .then((text) => {
            if (!text) {
                window.location.href = "/login";
                return;
            }
            const data = JSON.parse(text);
            document.getElementById("admin-member-count").textContent = data.memberCount.toLocaleString();
            document.getElementById("admin-bin-count").textContent = data.binCount.toLocaleString();
            document.getElementById("admin-restaurant-count").textContent = data.restaurantCount.toLocaleString();
            document.getElementById("admin-pending-count").textContent = data.pendingReportCount.toLocaleString();
        })
        .catch(() => {
            window.location.href = "/login";
        });

    // 1-1) 대시보드 '제보 검수 대기 목록' 미리보기 (보류 중인 것 상위 5개)
    function loadDashboardPendingReports() {
        fetch("/api/admin/reports")
            .then((res) => (res.ok ? res.json() : []))
            .then((list) => {
                const pending = list.filter((r) => r.status === "PENDING").slice(0, 5);
                const el = document.getElementById("dashboard-pending-reports");
                if (!pending.length) {
                    el.innerHTML = '<p class="admin-empty">보류 중인 제보가 없습니다.</p>';
                    return;
                }
                el.innerHTML = pending.map((r) => (
                    '<p class="admin-message">' +
                    "[" + (categoryLabel[r.category] || r.category) + "] " +
                    escapeHtml(r.name || r.address || (r.latitude + ", " + r.longitude)) +
                    " · " + escapeHtml(r.reporterName) + " · " + r.createdAt +
                    "</p>"
                )).join("");
            })
            .catch(() => {});
    }
    loadDashboardPendingReports();

    // 내 이메일 확인 (본인 계정은 권한변경/삭제 버튼 비활성화하기 위함)
    fetch("/api/mypage/me")
        .then((res) => res.text())
        .then((text) => {
            if (text) myEmail = JSON.parse(text).email;
        })
        .catch(() => {});

    // 2) 사이드바 메뉴 클릭 -> 페이지 이동 없이 해당 탭만 보여주기
    const navLinks = document.querySelectorAll(".admin-nav a[data-tab]");
    const tabSections = document.querySelectorAll(".admin-tab-content");

    function activateTab(tabName) {
        navLinks.forEach((link) => {
            link.classList.toggle("active", link.dataset.tab === tabName);
        });
        tabSections.forEach((section) => {
            section.hidden = section.id !== "tab-" + tabName;
        });
        if (tabName === "users") {
            loadUsers();
        }
        if (tabName === "settings") {
            loadSettings();
        }
        if (tabName === "reports") {
            loadReports();
        }
    }

    navLinks.forEach((link) => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            activateTab(this.dataset.tab);
        });
    });

    document.querySelectorAll("[data-tab-link]").forEach((link) => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            activateTab(this.dataset.tabLink);
        });
    });

    // 3) 유저 관리: 목록 로드 / 렌더 / 권한변경 / 삭제
    const tbody = document.getElementById("admin-user-table-body");
    const emptyEl = document.getElementById("admin-user-empty");
    const messageEl = document.getElementById("admin-user-message");
    const userSearch = document.getElementById("admin-user-search");

    function showMessage(text, isError) {
        messageEl.textContent = text;
        messageEl.className = "admin-message " + (isError ? "error" : "success");
    }

    function loadUsers() {
        fetch("/api/admin/users?keyword=" + encodeURIComponent(userSearch.value.trim()))
            .then((res) => res.json())
            .then((list) => {
                renderUsers(list);
            })
            .catch(() => {
                showMessage("회원 목록을 불러오지 못했습니다.", true);
            });
    }

    function renderUsers(list) {
        tbody.innerHTML = "";
        emptyEl.hidden = list.length > 0;

        list.forEach((u) => {
            const isMe = u.email === myEmail;
            const isAdminUser = u.role === "ADMIN";
            const canBlock = !isMe && !isAdminUser;
            const roleLabel = { USER: "일반회원", MANAGER: "매니저", ADMIN: "관리자" }[u.role] || u.role;
            const blockStatus = !u.blocked
                ? '<span class="admin-block-badge">정상</span>'
                : u.blockedPermanently
                    ? '<span class="admin-block-badge blocked">영구 차단</span>'
                    : '<span class="admin-block-badge blocked">차단 ~ ' + escapeHtml(u.blockedUntil) + '</span>';

            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${u.userId}</td>
                <td>${escapeHtml(u.name)}${isMe ? " (나)" : ""}</td>
                <td>${escapeHtml(u.nickname)}</td>
                <td>${escapeHtml(u.email)}</td>
                <td><span class="admin-role-badge ${u.role.toLowerCase()}">${escapeHtml(roleLabel)}</span></td>
                <td>${u.joinedAt}</td>
                <td>${blockStatus}</td>
                <td>
                    <div class="admin-row-actions">
                        <select class="admin-role-select" data-role-user="${u.userId}" ${isMe || !canManageRoles ? "disabled" : ""} title="${canManageRoles ? "직급 선택" : "직급 변경은 관리자만 할 수 있습니다."}">
                            <option value="USER" ${u.role === "USER" ? "selected" : ""}>일반회원</option>
                            <option value="MANAGER" ${u.role === "MANAGER" ? "selected" : ""}>매니저</option>
                            <option value="ADMIN" ${u.role === "ADMIN" ? "selected" : ""}>관리자</option>
                        </select>
                        <button class="promote" data-action="role-save" data-id="${u.userId}" ${isMe || !canManageRoles ? "disabled" : ""}>직급 변경</button>
                        <button class="delete" data-action="delete" data-id="${u.userId}" ${isMe ? "disabled" : ""}>삭제</button>
                        <select class="admin-block-duration" data-block-user="${u.userId}" aria-label="${escapeHtml(u.name)} 차단 기간" ${canBlock ? "" : "disabled"}>
                            <option value="1">1일</option>
                            <option value="7">7일</option>
                            <option value="30">30일</option>
                            <option value="PERMANENT">영구</option>
                        </select>
                        <button class="block" data-action="block" data-id="${u.userId}" ${canBlock ? "" : "disabled"}>차단</button>
                        <button class="unblock" data-action="unblock" data-id="${u.userId}" ${u.blocked && canBlock ? "" : "disabled"}>해제</button>
                    </div>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }

    let userSearchTimer;
    userSearch.addEventListener("input", function () {
        clearTimeout(userSearchTimer);
        userSearchTimer = setTimeout(loadUsers, 250);
    });

    function escapeHtml(str) {
        const div = document.createElement("div");
        div.textContent = str ?? "";
        return div.innerHTML;
    }

    tbody.addEventListener("click", function (e) {
        const btn = e.target.closest("button[data-action]");
        if (!btn) return;

        const userId = btn.dataset.id;

        if (btn.dataset.action === "role-save") {
            const newRole = tbody.querySelector(`select[data-role-user="${userId}"]`).value;
            fetch(`/api/admin/users/${userId}/role`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ role: newRole }),
            })
                .then(async (res) => {
                    const data = await res.json();
                    if (!res.ok) {
                        showMessage(data.message || "변경에 실패했습니다.", true);
                        return;
                    }
                    showMessage("권한이 변경되었습니다.", false);
                    loadUsers();
                })
                .catch(() => showMessage("변경 중 오류가 발생했습니다.", true));
        }

        if (btn.dataset.action === "delete") {
            if (!confirm("정말 이 회원을 삭제하시겠습니까? 되돌릴 수 없습니다.")) return;

            fetch(`/api/admin/users/${userId}`, { method: "DELETE" })
                .then(async (res) => {
                    const data = await res.json();
                    if (!res.ok) {
                        showMessage(data.message || "삭제에 실패했습니다.", true);
                        return;
                    }
                    showMessage("삭제되었습니다.", false);
                    loadUsers();
                })
                .catch(() => showMessage("삭제 중 오류가 발생했습니다.", true));
        }

        if (btn.dataset.action === "block") {
            const duration = tbody.querySelector(`select[data-block-user="${userId}"]`).value;
            const durationLabel = duration === "PERMANENT" ? "영구" : duration + "일";
            if (!confirm(`${durationLabel} 동안 이 회원을 차단하시겠습니까?`)) return;

            fetch(`/api/admin/users/${userId}/block`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ duration }),
            })
                .then(async (res) => {
                    const data = await res.json();
                    if (!res.ok) {
                        showMessage(data.message || "차단에 실패했습니다.", true);
                        return;
                    }
                    showMessage(data.message || "회원이 차단되었습니다.", false);
                    loadUsers();
                })
                .catch(() => showMessage("차단 중 오류가 발생했습니다.", true));
        }

        if (btn.dataset.action === "unblock") {
            fetch(`/api/admin/users/${userId}/block`, { method: "DELETE" })
                .then(async (res) => {
                    const data = await res.json();
                    if (!res.ok) {
                        showMessage(data.message || "차단 해제에 실패했습니다.", true);
                        return;
                    }
                    showMessage(data.message || "회원 차단을 해제했습니다.", false);
                    loadUsers();
                })
                .catch(() => showMessage("차단 해제 중 오류가 발생했습니다.", true));
        }
    });

    // 3-1) 쓰레기통 제보 관리: 목록 로드 / 렌더 / 개별·일괄 승인·반려·보류
    const reportTbody = document.getElementById("report-table-body");
    const reportEmptyEl = document.getElementById("report-empty");
    const reportMessageEl = document.getElementById("report-message");
    const reportSelectAll = document.getElementById("report-select-all");

    const statusLabel = { PENDING: "보류중", APPROVED: "승인됨", REJECTED: "반려됨" };
    const statusClass = { PENDING: "pending", APPROVED: "approved", REJECTED: "rejected" };
    const categoryLabel = { general: "일반", recycle: "재활용", can: "캔/병" };

    function showReportMessage(text, isError) {
        reportMessageEl.textContent = text;
        reportMessageEl.className = "admin-message " + (isError ? "error" : "success");
    }

    function loadReports() {
        fetch("/api/admin/reports")
            .then((res) => res.json())
            .then((list) => renderReports(list))
            .catch(() => showReportMessage("제보 목록을 불러오지 못했습니다.", true));
    }

    function renderReports(list) {
        reportTbody.innerHTML = "";
        reportEmptyEl.hidden = list.length > 0;
        reportSelectAll.checked = false;

        list.forEach((r) => {
            const location = r.address || (r.latitude.toFixed(5) + ", " + r.longitude.toFixed(5));
            const content = r.name || r.description || "-";
            const isPending = r.status === "PENDING";

            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td><input type="checkbox" class="report-checkbox" value="${r.reportId}"></td>
                <td><span class="admin-role-badge ${statusClass[r.status]}">${statusLabel[r.status]}</span></td>
                <td>${categoryLabel[r.category] || r.category}</td>
                <td title="${escapeHtml(r.description || "")}">${escapeHtml(content)}</td>
                <td>${escapeHtml(location)}</td>
                <td>${escapeHtml(r.reporterName)} (${escapeHtml(r.reporterNickname)})</td>
                <td>${r.createdAt}</td>
                <td>
                    <div class="admin-row-actions">
                        <button class="promote" data-action="approve" data-id="${r.reportId}" ${!isPending ? "disabled" : ""}>승인</button>
                        <button class="delete" data-action="reject" data-id="${r.reportId}" ${!isPending ? "disabled" : ""}>반려</button>
                        <button data-action="hold" data-id="${r.reportId}" ${isPending ? "disabled" : ""}>보류로</button>
                    </div>
                </td>
            `;
            reportTbody.appendChild(tr);
        });
    }

    function updateReportStatus(id, status, rejectReason) {
        return fetch(`/api/admin/reports/${id}/status`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ status: status, rejectReason: rejectReason || null }),
        }).then(async (res) => {
            const data = await res.json();
            if (!res.ok) throw new Error(data.message || "처리에 실패했습니다.");
            return data;
        });
    }

    reportSelectAll.addEventListener("change", function () {
        document.querySelectorAll(".report-checkbox").forEach((cb) => (cb.checked = reportSelectAll.checked));
    });

    reportTbody.addEventListener("click", function (e) {
        const btn = e.target.closest("button[data-action]");
        if (!btn) return;
        const id = btn.dataset.id;
        const action = btn.dataset.action;

        if (action === "approve") {
            updateReportStatus(id, "APPROVED")
                .then(() => { showReportMessage("승인되었습니다.", false); loadReports(); })
                .catch((err) => showReportMessage(err.message, true));
        }
        if (action === "reject") {
            const reason = prompt("반려 사유를 입력해주세요.");
            if (reason === null) return;
            if (!reason.trim()) { showReportMessage("반려 사유를 입력해주세요.", true); return; }
            updateReportStatus(id, "REJECTED", reason.trim())
                .then(() => { showReportMessage("반려되었습니다.", false); loadReports(); })
                .catch((err) => showReportMessage(err.message, true));
        }
        if (action === "hold") {
            updateReportStatus(id, "PENDING")
                .then(() => { showReportMessage("보류 상태로 되돌렸습니다.", false); loadReports(); })
                .catch((err) => showReportMessage(err.message, true));
        }
    });

    function selectedReportIds() {
        return [...document.querySelectorAll(".report-checkbox:checked")].map((cb) => cb.value);
    }

    document.getElementById("report-bulk-approve").addEventListener("click", function () {
        const ids = selectedReportIds();
        if (!ids.length) { showReportMessage("승인할 제보를 선택해주세요.", true); return; }
        Promise.all(ids.map((id) => updateReportStatus(id, "APPROVED")))
            .then(() => { showReportMessage(ids.length + "건 승인되었습니다.", false); loadReports(); })
            .catch((err) => showReportMessage(err.message, true));
    });

    document.getElementById("report-bulk-reject").addEventListener("click", function () {
        const ids = selectedReportIds();
        if (!ids.length) { showReportMessage("반려할 제보를 선택해주세요.", true); return; }
        const reason = prompt("반려 사유를 입력해주세요. (선택한 " + ids.length + "건에 동일하게 적용됩니다)");
        if (reason === null) return;
        if (!reason.trim()) { showReportMessage("반려 사유를 입력해주세요.", true); return; }
        Promise.all(ids.map((id) => updateReportStatus(id, "REJECTED", reason.trim())))
            .then(() => { showReportMessage(ids.length + "건 반려되었습니다.", false); loadReports(); })
            .catch((err) => showReportMessage(err.message, true));
    });

    document.getElementById("report-refresh-btn").addEventListener("click", loadReports);

    // 5) 시스템 설정: 로드 / 저장
    const settingsMessageEl = document.getElementById("settings-message");
    const signupEnabledSelect = document.getElementById("settings-signup-enabled");
    const maintenanceModeSelect = document.getElementById("settings-maintenance-mode");
    const maintenanceMessageInput = document.getElementById("settings-maintenance-message");

    function showSettingsMessage(text, isError) {
        settingsMessageEl.textContent = text;
        settingsMessageEl.className = "admin-message " + (isError ? "error" : "success");
    }

    function loadSettings() {
        fetch("/api/admin/settings")
            .then((res) => res.json())
            .then((data) => {
                signupEnabledSelect.value = String(data.signupEnabled);
                maintenanceModeSelect.value = String(data.maintenanceMode);
                maintenanceMessageInput.value = data.maintenanceMessage || "";
            })
            .catch(() => showSettingsMessage("설정을 불러오지 못했습니다.", true));
    }

    document.getElementById("settings-save-btn").addEventListener("click", function () {
        const payload = {
            signupEnabled: signupEnabledSelect.value === "true",
            maintenanceMode: maintenanceModeSelect.value === "true",
            maintenanceMessage: maintenanceMessageInput.value.trim(),
        };

        fetch("/api/admin/settings", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        })
            .then(async (res) => {
                const data = await res.json();
                if (!res.ok) {
                    showSettingsMessage(data.message || "저장에 실패했습니다.", true);
                    return;
                }
                showSettingsMessage("설정이 저장되었습니다.", false);
            })
            .catch(() => showSettingsMessage("저장 중 오류가 발생했습니다.", true));
    });

    const requestedTab = new URLSearchParams(window.location.search).get("tab");
    if (requestedTab && [...navLinks].some((link) => link.dataset.tab === requestedTab)) {
        activateTab(requestedTab);
    }
});

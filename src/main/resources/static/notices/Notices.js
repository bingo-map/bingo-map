document.addEventListener("DOMContentLoaded", async function () {
    const listEl = document.getElementById("notice-list");
    const emptyEl = document.getElementById("notice-empty");
    const toolbar = document.getElementById("notice-admin-toolbar");
    const form = document.getElementById("notice-form");
    const formMessage = document.getElementById("notice-form-message");
    let notices = [];
    let isAdmin = false;

    try {
        const session = await fetch("/api/session", { cache: "no-store" }).then((res) => res.json());
        isAdmin = session.loggedIn && session.role === "ADMIN";
        toolbar.hidden = !isAdmin;
    } catch (_) {}

    function escapeHtml(value) {
        const div = document.createElement("div");
        div.textContent = value == null ? "" : String(value);
        return div.innerHTML;
    }

    async function loadNotices() {
        try {
            const response = await fetch("/api/notices", { cache: "no-store" });
            if (!response.ok) throw new Error();
            notices = await response.json();
            render();
        } catch (_) {
            emptyEl.hidden = false;
            emptyEl.textContent = "공지사항을 불러오지 못했습니다.";
        }
    }

    function render() {
        emptyEl.hidden = notices.length > 0;
        listEl.innerHTML = notices.map((notice) => `
            <article class="notice-item ${notice.pinned ? "is-pinned" : ""}">
                <a class="notice-item-link" href="/notices/view/${encodeURIComponent(notice.noticeId)}">
                    ${notice.pinned ? '<span class="notice-pin-label">고정</span>' : ""}
                    <span class="notice-item-title">${escapeHtml(notice.title)}</span>
                    <span class="notice-item-preview">${escapeHtml(notice.content)}</span>
                    <span class="notice-item-meta">${escapeHtml(notice.createdAt)} · 조회 ${Number(notice.viewCount) || 0}</span>
                    <span class="notice-item-arrow" aria-hidden="true">›</span>
                </a>
                ${isAdmin ? `<div class="notice-admin-actions"><button type="button" data-action="pin" data-id="${notice.noticeId}">${notice.pinned ? "고정 해제" : "상단 고정"}</button><button type="button" data-action="edit" data-id="${notice.noticeId}">수정</button><button type="button" data-action="delete" data-id="${notice.noticeId}">삭제</button></div>` : ""}
            </article>`).join("");
    }

    function openForm(notice) {
        form.hidden = false;
        formMessage.textContent = "";
        document.getElementById("notice-form-id").value = notice ? notice.noticeId : "";
        document.getElementById("notice-title").value = notice ? notice.title : "";
        document.getElementById("notice-content").value = notice ? notice.content : "";
        document.getElementById("notice-pinned").checked = !!(notice && notice.pinned);
        form.scrollIntoView({ behavior: "smooth", block: "center" });
    }

    document.getElementById("notice-new-btn").addEventListener("click", () => openForm(null));
    document.getElementById("notice-cancel").addEventListener("click", () => { form.hidden = true; });

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const id = document.getElementById("notice-form-id").value;
        const payload = {
            title: document.getElementById("notice-title").value.trim(),
            content: document.getElementById("notice-content").value.trim(),
            pinned: document.getElementById("notice-pinned").checked
        };
        try {
            const response = await fetch(id ? `/api/admin/notices/${id}` : "/api/admin/notices", {
                method: id ? "PUT" : "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });
            const result = await response.json();
            if (!response.ok) throw new Error(result.message || "저장하지 못했습니다.");
            form.hidden = true;
            document.getElementById("notice-message").textContent = id ? "공지사항을 수정했습니다." : "공지사항을 등록했습니다.";
            await loadNotices();
        } catch (error) {
            formMessage.textContent = error.message || "저장 중 오류가 발생했습니다.";
        }
    });

    listEl.addEventListener("click", async (event) => {
        const button = event.target.closest("button[data-action]");
        if (!button) return;
        const notice = notices.find((item) => String(item.noticeId) === button.dataset.id);
        if (!notice) return;
        try {
            if (button.dataset.action === "edit") return openForm(notice);
            if (button.dataset.action === "delete") {
                if (!window.confirm("이 공지사항을 삭제할까요?")) return;
                const response = await fetch(`/api/admin/notices/${notice.noticeId}`, { method: "DELETE" });
                if (!response.ok) throw new Error("삭제하지 못했습니다.");
            } else if (button.dataset.action === "pin") {
                const response = await fetch(`/api/admin/notices/${notice.noticeId}/pin`, {
                    method: "PATCH", headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ pinned: !notice.pinned })
                });
                if (!response.ok) throw new Error("고정 상태를 변경하지 못했습니다.");
            }
            await loadNotices();
        } catch (error) {
            document.getElementById("notice-message").textContent = error.message || "요청을 처리하지 못했습니다.";
        }
    });

    await loadNotices();
});

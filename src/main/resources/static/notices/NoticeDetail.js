document.addEventListener("DOMContentLoaded", async function () {
    const detail = document.getElementById("notice-detail");
    const id = decodeURIComponent(window.location.pathname.split("/").pop());
    const escapeHtml = (value) => {
        const element = document.createElement("div");
        element.textContent = value == null ? "" : String(value);
        return element.innerHTML;
    };
    try {
        const response = await fetch(`/api/notices/${encodeURIComponent(id)}`, { cache: "no-store" });
        if (!response.ok) throw new Error("공지사항을 찾을 수 없습니다.");
        const notice = await response.json();
        detail.innerHTML = `<header class="notice-detail-card-head"><div>${notice.pinned ? '<span class="notice-pin-label">상단 고정</span>' : ''}<h2>${escapeHtml(notice.title)}</h2></div><div class="notice-detail-meta"><span>${escapeHtml(notice.createdAt)}</span><span>조회 ${Number(notice.viewCount) || 0}</span></div></header><div class="notice-detail-content">${escapeHtml(notice.content)}</div>`;
    } catch (error) {
        detail.innerHTML = `<p class="notice-loading">${escapeHtml(error.message || "공지사항을 불러오지 못했습니다.")}</p>`;
    }
});

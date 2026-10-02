(function () {
    'use strict';
    if (window.BinGoBinFavorites) return;
    const busy = new Set();
    const escape = value => String(value).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
    const style = document.createElement('style');
    style.textContent = `
      .bingo-popup .popup:has(.bbf-heart){position:relative}
      .bingo-popup .popup:has(.bbf-heart)>small,
      .bingo-popup .popup:has(.bbf-heart)>strong{padding-right:40px}
      .bingo-popup .popup button.bbf-heart{position:absolute;top:12px;right:12px;
        display:grid;place-items:center;width:34px;height:34px;min-height:0;
        margin:0;padding:0;border:1px solid #e1e8e3;border-radius:50%;
        background:#fff;color:#728179;font-size:24px;line-height:1;cursor:pointer}
      .bingo-popup .popup button.bbf-heart[aria-pressed="true"]{color:#e74665;background:#fff1f4;border-color:#f6d4dd}
      .bingo-popup .popup button.bbf-heart:disabled{opacity:.55;cursor:wait}
      .bingo-popup .popup button.bbf-heart:focus-visible{outline:3px solid #1677ff;outline-offset:2px}
    `;
    document.head.append(style);
    function paint(button, saved) {
        button.textContent = saved ? '♥' : '♡';
        button.setAttribute('aria-pressed', String(saved));
        button.title = saved ? '즐겨찾기 해제' : '즐겨찾기 저장';
        button.setAttribute('aria-label', button.title);
    }
    async function statuses() {
        const response = await fetch('/api/favorites/status?targetType=WASTE_BIN', {
            credentials:'same-origin', cache:'no-store', headers:{Accept:'application/json'}
        });
        if (response.status === 401) throw Object.assign(new Error('로그인이 필요합니다.'), {login:true});
        if (!response.ok) throw new Error('즐겨찾기 상태를 확인하지 못했습니다. 다시 눌러주세요.');
        const rows = await response.json();
        if (!Array.isArray(rows)) throw new Error('즐겨찾기 응답을 확인해주세요.');
        return rows;
    }
    function savedItem(rows, id) { return rows.find(row => String(row.targetId) === id); }
    async function refresh(popup) {
        const button = popup.getElement()?.querySelector('.bbf-heart');
        if (!button || busy.has(button.dataset.bbfId)) return;
        button.disabled = true;
        try { paint(button, Boolean(savedItem(await statuses(), button.dataset.bbfId))); }
        catch (error) { if (error.login) paint(button, false); else button.title = error.message; }
        finally { button.disabled = false; }
    }
    document.addEventListener('click', async event => {
        const button = event.target.closest?.('.bbf-heart');
        if (!button) return;
        event.preventDefault(); event.stopPropagation();
        const id = button.dataset.bbfId;
        if (busy.has(id)) return;
        busy.add(id); button.disabled = true;
        try {
            const existing = savedItem(await statuses(), id);
            const response = await fetch(existing ? '/api/favorites/' + encodeURIComponent(existing.favoriteId) : '/api/favorites', {
                method:existing ? 'DELETE' : 'POST', credentials:'same-origin',
                headers:{'Content-Type':'application/json',Accept:'application/json'},
                ...(existing ? {} : {body:JSON.stringify({targetType:'WASTE_BIN',targetId:id,targetName:button.dataset.bbfName})})
            });
            if (response.status === 401) throw Object.assign(new Error('로그인이 필요합니다.'), {login:true});
            // Another tab may already have saved/deleted this same favorite.
            if (!response.ok && !(existing && response.status === 404) && !(!existing && response.status === 409)) {
                const body = await response.json().catch(() => ({}));
                throw new Error(body.message || '저장 상태를 변경하지 못했습니다. 다시 시도해주세요.');
            }
            paint(button, !existing);
        } catch (error) {
            if (error.login) {
                paint(button, false);
                if (window.confirm('즐겨찾기는 로그인 후 사용할 수 있어요. 로그인 화면으로 이동할까요?')) {
                    window.location.href = '/login?returnUrl=' + encodeURIComponent(window.location.pathname + window.location.search);
                }
            } else window.alert(error.message);
        } finally { busy.delete(id); button.disabled = false; }
    }, true);
    const attached = new WeakSet();
    window.BinGoBinFavorites = {
        buttonHtml(bin) {
            // Never substitute bin.id: backend resolves waste bins by OSM ID.
            if (bin.isReport || bin.osmId == null || !/^\d+$/.test(String(bin.osmId))) return '';
            const name = bin.category === 'can' ? '캔/병 수거함' : bin.category === 'recycle' ? '재활용 수거함' : '쓰레기통';
            return `<button type="button" class="bbf-heart" data-bbf-id="${escape(bin.osmId)}" data-bbf-name="${escape(name)}" aria-pressed="false" aria-label="즐겨찾기 저장" title="즐겨찾기 저장">♡</button>`;
        },
        attach(map) {
            if (attached.has(map)) return;
            attached.add(map);
            map.on('popupopen', event => { refresh(event.popup); });
        }
    };
})();

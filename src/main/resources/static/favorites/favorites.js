(function () {
    'use strict';

    const list = document.getElementById('favoritesList');
    const state = document.getElementById('favoritesState');
    const count = document.getElementById('favoritesCount');
    const retry = document.getElementById('retryFavorites');
    let favorites = [];

    let selectedType = 'ALL';
    let loaded = false;
    const controls = document.createElement('section');
    controls.className = 'bfs-controls';
    controls.innerHTML = `<label for="bfsSearch">저장한 장소 검색</label>
      <div class="bfs-search-row"><input id="bfsSearch" type="search" placeholder="이름, 주소, 도톤보리·오사카성·유니버셜 검색"><button type="button" id="bfsReset">초기화</button></div>
      <div class="bfs-types" role="group" aria-label="즐겨찾기 종류">
      <button type="button" data-bfs-type="ALL" aria-pressed="true">전체</button>
      <button type="button" data-bfs-type="RESTAURANT" aria-pressed="false">맛집</button>
      <button type="button" data-bfs-type="WASTE_BIN" aria-pressed="false">쓰레기통</button></div>
      <p>쓰레기통은 가장 가까운 기준 스팟과 직선 거리로 구분해보세요.</p>`;
    state.before(controls);
    const search = controls.querySelector('input');
    const css = document.createElement('style');
    css.textContent = `.bfs-controls{padding:20px;margin-bottom:24px;border:1px solid #e0eae3;border-radius:20px;background:#fff}
      .bfs-controls label{display:block;font-size:14px;font-weight:700;margin-bottom:10px}
      .bfs-search-row{display:flex;gap:8px}.bfs-search-row input{min-width:0;flex:1;padding:12px;border:1px solid #d7e3da;border-radius:12px;font:inherit;font-size:14px}
      .bfs-controls button{padding:9px 15px;border:1px solid #d7e3da;border-radius:22px;background:#fff;color:#29643b;cursor:pointer;font:inherit;font-size:13px}
      .bfs-types{display:flex;gap:8px;margin-top:14px}.bfs-types button[aria-pressed="true"]{background:#198b41;color:white;border-color:#198b41}
      .bfs-controls p{font-size:12px;color:#66796b;margin:12px 0 0}.bfs-controls :focus-visible{outline:3px solid #86bdff;outline-offset:2px}
      .favorite-card .bfs-bin-location{white-space:normal;overflow-wrap:anywhere;font-size:12px;color:#6b7c70;margin:5px 0}
      .favorite-card .bfs-spot{font-size:12px;color:#187c37;margin:6px 0;line-height:1.6}`;
    document.head.append(css);
    const spots = [
      {name:'도톤보리', lat:34.6690561, lon:135.5013611},
      {name:'유니버셜 스튜디오', lat:34.6672, lon:135.43583},
      {name:'오사카성', lat:34.68625, lon:135.52579}
    ];
    function coords(item) {
        const values = [item.latitude,item.longitude];
        if (values.some(v => v == null || String(v).trim() === '')) return null;
        const [lat,lon] = values.map(Number);
        return Number.isFinite(lat) && Number.isFinite(lon) && Math.abs(lat)<=90 && Math.abs(lon)<=180 ? {lat,lon} : null;
    }
    function nearestSpot(item) {
        if (item.targetType !== 'WASTE_BIN') return null;
        const point = coords(item); if (!point) return null;
        const rad = Math.PI/180;
        return spots.map(spot => {
            const h = Math.sin((point.lat-spot.lat)*rad/2)**2 + Math.cos(point.lat*rad)*Math.cos(spot.lat*rad)*Math.sin((point.lon-spot.lon)*rad/2)**2;
            return {...spot, meters:12742000*Math.asin(Math.sqrt(Math.min(1,Math.max(0,h))))};
        }).sort((a,b)=>a.meters-b.meters)[0];
    }
    function spotLabel(item) {
        const spot=nearestSpot(item); if(!spot) return '';
        const d=spot.meters<1000 ? Math.round(spot.meters)+'m' : (spot.meters/1000).toFixed(1)+'km';
        return '가장 가까운 기준 스팟: '+spot.name+' · 직선 '+d;
    }
    function locationLabel(item) {
        const address=String(item.location||'').trim();
        if(item.targetType!=='WASTE_BIN') return address||'위치 정보 없음';
        const point=coords(item);
        const coordinate=point ? `위도 ${point.lat.toFixed(6)} · 경도 ${point.lon.toFixed(6)}` : '';
        const validAddress=!['','주소 정보 없음','위치 정보 없음','지도에서 위치를 확인하세요'].includes(address);
        return [validAddress?address:'',coordinate].filter(Boolean).join(' / ')||'좌표 정보 없음';
    }
    function normalized(text) { return String(text||'').normalize('NFKC').toLowerCase().replace(/\s+/g,''); }
    function matches(item) {
        if(selectedType!=='ALL' && item.targetType!==selectedType) return false;
        const spot=nearestSpot(item);
        const type=item.targetType==='WASTE_BIN'?'쓰레기통':'맛집 식당';
        const text=normalized([item.targetName,item.location,locationLabel(item),item.targetId,type,spot?.name,spot?.name.includes('유니버셜')?'USJ 유니버설':''].join(' '));
        return search.value.trim().split(/\s+/).every(term=>text.includes(normalized(term)));
    }
    search.addEventListener('input',()=>{if(loaded)render();});
    controls.addEventListener('click',event=>{
        const button=event.target.closest('[data-bfs-type]');
        if(button) selectedType=button.dataset.bfsType;
        else if(event.target.id==='bfsReset'){selectedType='ALL';search.value='';search.focus();}
        else return;
        controls.querySelectorAll('[data-bfs-type]').forEach(b=>b.setAttribute('aria-pressed',String(b.dataset.bfsType===selectedType)));
        if(loaded)render();
    });


    function escapeHtml(value) {
        return String(value ?? '').replace(/[&<>"']/g, character => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[character]));
    }

    function login() {
        window.location.replace('/login?returnUrl=%2Ffavorites');
    }

    function dateLabel(value) {
        if (!value) return '저장 날짜 정보 없음';
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) return '저장 날짜 정보 없음';
        return '저장일 ' + new Intl.DateTimeFormat('ko-KR', {
            year: 'numeric', month: 'long', day: 'numeric'
        }).format(date);
    }

    function cardHtml(item) {
        const restaurant = item.targetType === 'RESTAURANT';
        const name = item.targetName || (restaurant ? '이름 없는 맛집' : '쓰레기통');
        const openUrl = restaurant
            ? '/restaurants/detail?id=' + encodeURIComponent(item.targetId)
            : '/map?favoriteBin=' + encodeURIComponent(item.targetId);

        return `
            <article class="favorite-card" data-favorite-id="${escapeHtml(item.id)}">
                <div class="favorite-card-icon" aria-hidden="true">${restaurant ? '🍽️' : '🗑️'}</div>
                <div class="favorite-card-main">
                    <span class="favorite-type">${restaurant ? '맛집' : '쓰레기통'}</span>
                    <a class="favorite-name" href="${openUrl}">${escapeHtml(name)}</a>
                    <p class="favorite-location ${restaurant ? '' : 'bfs-bin-location'}">${escapeHtml(locationLabel(item))}</p>
                    ${!restaurant && spotLabel(item) ? `<p class="bfs-spot">${escapeHtml(spotLabel(item))}</p>` : ''}
                    <p class="favorite-date">${escapeHtml(dateLabel(item.createdAt))}</p>
                </div>
                <div class="favorite-card-actions">
                    <a class="favorite-open" href="${openUrl}">${restaurant ? '상세 보기' : '지도 보기'} →</a>
                    <button class="favorite-remove" type="button" data-remove-id="${escapeHtml(item.id)}">삭제</button>
                </div>
            </article>`;
    }

    function showMessage(html, isError) {
        state.hidden = false;
        state.classList.toggle('is-error', Boolean(isError));
        state.innerHTML = html;
        list.hidden = true;
    }

    function render() {
        const visible = favorites.filter(matches);
        count.textContent = favorites.length + '개 저장됨 · ' + visible.length + '개 표시';
        state.classList.remove('is-error');
        retry.hidden = true;

        if (!favorites.length) {
            showMessage('<div><strong>아직 저장한 항목이 없어요.</strong><br>맛집이나 지도 쓰레기통의 하트를 눌러 저장해보세요.<br><a href="/restaurants">주변 맛집 둘러보기 →</a></div>', false);
            return;
        }

        if (!visible.length) {
            showMessage('검색 결과가 없어요. 검색어 또는 종류를 바꾸거나 초기화를 눌러주세요.', false);
            return;
        }
        state.hidden = true;
        list.hidden = false;
        list.innerHTML = visible.map(cardHtml).join('');
    }

    async function loadFavorites() {
        loaded = false;
        count.textContent = '불러오는 중';
        retry.hidden = true;
        showMessage('즐겨찾기를 불러오는 중입니다.', false);

        try {
            const response = await fetch('/api/favorites', {
                headers: { Accept: 'application/json' },
                credentials: 'same-origin',
                cache: 'no-store'
            });

            if (response.status === 401) {
                login();
                return;
            }
            if (!response.ok) throw new Error('HTTP ' + response.status);

            favorites = await response.json();
            if (!Array.isArray(favorites)) throw new Error('잘못된 응답');
            loaded = true;
            render();
        } catch (error) {
            count.textContent = '불러오기 실패';
            showMessage('즐겨찾기를 불러오지 못했어요. 잠시 후 다시 시도해주세요.', true);
            retry.hidden = false;
        }
    }

    list.addEventListener('click', async event => {
        const button = event.target.closest('[data-remove-id]');
        if (!button) return;

        const favoriteId = button.dataset.removeId;
        button.disabled = true;

        try {
            const response = await fetch('/api/favorites/' + encodeURIComponent(favoriteId), {
                method: 'DELETE',
                credentials: 'same-origin'
            });
            if (response.status === 401) {
                login();
                return;
            }
            if (!response.ok && response.status !== 204) {
                throw new Error('HTTP ' + response.status);
            }
            favorites = favorites.filter(item => String(item.id) !== String(favoriteId));
            render();
        } catch (error) {
            button.disabled = false;
            state.hidden = false;
            state.classList.add('is-error');
            state.textContent = '삭제하지 못했어요. 다시 시도해주세요.';
        }
    });

    retry.addEventListener('click', loadFavorites);
    loadFavorites();
})();

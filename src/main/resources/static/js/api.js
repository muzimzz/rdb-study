// ── 세션 관리 (sessionStorage) ──────────────────────────────
const Session = {
    get: () => JSON.parse(sessionStorage.getItem('member') || 'null'),
    set: (member) => sessionStorage.setItem('member', JSON.stringify(member)),
    clear: () => sessionStorage.removeItem('member'),
};

// ── 공통 fetch 래퍼 ──────────────────────────────────────────
// 401 응답 시 자동으로 로그인 페이지로 이동
async function api(url, options = {}) {
    const defaultHeaders = {};

    // 로그인 요청(formLogin)은 x-www-form-urlencoded 방식을 사용하므로 별도 처리
    if (options.json) {
        defaultHeaders['Content-Type'] = 'application/json';
        options.body = JSON.stringify(options.json);
        delete options.json;
    }

    const res = await fetch(url, {
        headers: { ...defaultHeaders, ...options.headers },
        credentials: 'same-origin', // 세션 쿠키 자동 전송
        ...options,
    });

    if (    res.status === 401) {
        Session.clear();
        location.href = '/login.html';
        return null;
    }

    return res;
}

// ── 로그아웃 ──────────────────────────────────────────────────
async function logout() {
    await fetch('/logout', { method: 'POST', credentials: 'same-origin' });
    Session.clear();
    location.href = '/login.html';
}

// ── 인증 필요 페이지 진입 시 체크 ──────────────────────────────
function requireAuth() {
    const member = Session.get();
    if (!member) {
        location.href = '/login.html';
        return null;
    }
    return member;
}

// ── 관리자 권한 체크 ──────────────────────────────────────────
function requireAdmin() {
    const member = requireAuth();
    if (member && member.role !== 'ADMIN') {
        alert('관리자만 접근 가능합니다.');
        location.href = '/index.html';
        return null;
    }
    return member;
}

// ── 공통 Navbar 렌더링 ─────────────────────────────────────────
function renderNavbar(activePage = '') {
    const member = Session.get();
    const nav = document.getElementById('navbar');
    if (!nav) return;

    const isAdmin = member && member.role === 'ADMIN';

    nav.innerHTML = `
        <a class="navbar-brand" href="/index.html">🛒 RDB Shop</a>
        <ul class="navbar-nav">
            <li><a href="/index.html" ${activePage === 'home' ? 'style="color:#fff"' : ''}>상품</a></li>
            ${member ? `
                <li><a href="/cart.html" ${activePage === 'cart' ? 'style="color:#fff"' : ''}>장바구니</a></li>
                <li><a href="/orders.html" ${activePage === 'orders' ? 'style="color:#fff"' : ''}>주문내역</a></li>
                <li><a href="/profile.html" ${activePage === 'profile' ? 'style="color:#fff"' : ''}>내 정보</a></li>
                ${isAdmin ? `<li><a href="/admin/products.html" ${activePage === 'admin' ? 'style="color:#fff"' : ''}>관리자</a></li>` : ''}
                <li><span class="navbar-user">${member.name}님</span></li>
                <li><a href="#" onclick="logout(); return false;">로그아웃</a></li>
            ` : `
                <li><a href="/login.html">로그인</a></li>
                <li><a href="/register.html">회원가입</a></li>
            `}
        </ul>
    `;
}

// ── 가격 포맷 (1000 → "1,000원") ─────────────────────────────
function formatPrice(price) {
    return price.toLocaleString('ko-KR') + '원';
}

// ── 날짜 포맷 ─────────────────────────────────────────────────
function formatDate(dateArr) {
    // Spring이 LocalDateTime을 배열로 직렬화: [year, month, day, hour, min, sec]
    if (!dateArr) return '';
    if (Array.isArray(dateArr)) {
        const [y, mo, d, h, mi] = dateArr;
        return `${y}.${String(mo).padStart(2,'0')}.${String(d).padStart(2,'0')} ${String(h).padStart(2,'0')}:${String(mi).padStart(2,'0')}`;
    }
    return new Date(dateArr).toLocaleString('ko-KR');
}

// ── 주문 상태 한글 변환 ─────────────────────────────────────────
function statusLabel(status) {
    const map = {
        PENDING: '주문접수',
        PROCESSING: '처리중',
        SHIPPED: '배송중',
        DELIVERED: '배송완료',
        CANCELLED: '취소됨',
    };
    return map[status] || status;
}

function statusBadge(status) {
    const cls = {
        PENDING: 'badge-warning',
        PROCESSING: 'badge-info',
        SHIPPED: 'badge-secondary',
        DELIVERED: 'badge-success',
        CANCELLED: 'badge-danger',
    };
    return `<span class="badge ${cls[status] || 'badge-secondary'}">${statusLabel(status)}</span>`;
}

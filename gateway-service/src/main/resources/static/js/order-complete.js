let currentPage = 0;
const pageSize = 10;

document.addEventListener('DOMContentLoaded', function() {
    loadBoards();
});

async function loadBoards() {
    try {
        const response = await fetch(`/api/boards?page=${currentPage}&size=${pageSize}`);
        const data = await response.json();
        
        const tbody = document.getElementById('boardTableBody');
        tbody.innerHTML = '';
        
        data.content.forEach(board => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${board.id}</td>
                <td>${board.orderId}</td>
                <td>${board.title.replace('주문 완료: ', '')}</td>
                <td>${board.author}</td>
                <td>${extractQuantity(board.content)}</td>
                <td>${formatDateTime(extractOrderDate(board.content))}</td>
                <td>${formatDateTime(board.createdAt)}</td>
            `;
            tbody.appendChild(tr);
        });

        renderPagination(data.totalPages, data.number);
    } catch (error) {
        console.error('게시판 목록을 불러오는데 실패했습니다:', error);
        alert('게시판 목록을 불러오는데 실패했습니다.');
    }
}

function extractQuantity(content) {
    const match = content.match(/수량: (\d+)/);
    return match ? match[1] : '알 수 없음';
}

function extractOrderDate(content) {
    const match = content.match(/주문일시: (.+)/);
    return match ? match[1] : '';
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
}

function renderPagination(totalPages, currentPage) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    // 이전 페이지 버튼
    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 0 ? 'disabled' : ''}`;
    prevLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Previous" onclick="changePage(${currentPage - 1})">
            <span aria-hidden="true">&laquo;</span>
        </a>
    `;
    pagination.appendChild(prevLi);

    // 페이지 번호
    const startPage = Math.max(0, currentPage - 2);
    const endPage = Math.min(totalPages - 1, currentPage + 2);

    for (let i = startPage; i <= endPage; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        li.innerHTML = `<a class="page-link" href="#" onclick="changePage(${i})">${i + 1}</a>`;
        pagination.appendChild(li);
    }

    // 다음 페이지 버튼
    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}`;
    nextLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Next" onclick="changePage(${currentPage + 1})">
            <span aria-hidden="true">&raquo;</span>
        </a>
    `;
    pagination.appendChild(nextLi);
}

function changePage(page) {
    currentPage = page;
    loadBoards();
} 
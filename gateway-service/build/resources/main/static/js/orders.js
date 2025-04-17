let orderModal;
let products = [];
let currentUser = null;

document.addEventListener('DOMContentLoaded', function() {
    orderModal = new bootstrap.Modal(document.getElementById('orderModal'));
    loadOrders();
    loadProducts();
    loadCurrentUser();
});

async function loadOrders() {
    try {
        const response = await fetch('/api/orders');
        const orders = await response.json();
        
        const tbody = document.getElementById('orderTableBody');
        tbody.innerHTML = '';
        
        orders.forEach(order => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${order.id}</td>
                <td>${order.productId}</td>
                <td>${order.quantity}</td>
                <td>${order.totalPrice ? order.totalPrice.toLocaleString() : '0'}원</td>
                <td>${order.customerName}</td>
                <td>${order.customerEmail}</td>
                <td>${new Date(order.orderDate).toLocaleString()}</td>
                <td>${getStatusText(order.status)}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editOrder(${order.id})">수정</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteOrder(${order.id})">삭제</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('주문 목록을 불러오는데 실패했습니다:', error);
        alert('주문 목록을 불러오는데 실패했습니다.');
    }
}

function getStatusText(status) {
    const statusMap = {
        'PENDING': '대기',
        'PROCESSING': '처리중',
        'COMPLETED': '완료'
    };
    return statusMap[status] || status;
}

async function loadProducts() {
    try {
        const response = await fetch('/api/products');
        products = await response.json();
        
        const productSelect = document.getElementById('productId');
        productSelect.innerHTML = '<option value="">상품을 선택하세요</option>';
        
        products.forEach(product => {
            const option = document.createElement('option');
            option.value = product.id;
            option.textContent = `${product.name} (${product.price.toLocaleString()}원)`;
            productSelect.appendChild(option);
        });

        // 상품 선택 이벤트 리스너 추가
        productSelect.addEventListener('change', updateSelectedProductInfo);
    } catch (error) {
        console.error('상품 목록을 불러오는데 실패했습니다:', error);
    }
}

function updateSelectedProductInfo() {
    const productId = document.getElementById('productId').value;
    const selectedProductInfo = document.getElementById('selectedProductInfo');
    
    if (productId) {
        const product = products.find(p => p.id === parseInt(productId));
        if (product) {
            selectedProductInfo.innerHTML = `
                <div class="card">
                    <div class="card-body">
                        <h6 class="card-title">${product.name}</h6>
                        <p class="card-text">
                            가격: ${product.price.toLocaleString()}원<br>
                            재고: ${product.stock}개<br>
                            설명: ${product.description}
                        </p>
                    </div>
                </div>
            `;
        }
    } else {
        selectedProductInfo.innerHTML = '';
    }
}

async function loadCurrentUser() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = '/login';
            return;
        }

        const response = await fetch('/api/auth/current', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            currentUser = {
                username: data.username,
                role: data.role,
                email: data.email
            };
        } else {
            window.location.href = '/login';
        }
    } catch (error) {
        console.error('현재 사용자 정보를 불러오는데 실패했습니다:', error);
        window.location.href = '/login';
    }
}

function showAddOrderModal() {
    document.getElementById('modalTitle').textContent = '주문 추가';
    document.getElementById('orderForm').reset();
    document.getElementById('orderId').value = '';
    document.getElementById('selectedProductInfo').innerHTML = '';
    
    // 로그인한 사용자 정보가 있으면 자동으로 입력
    if (currentUser) {
        document.getElementById('customerName').value = currentUser.username;
        document.getElementById('customerEmail').value = currentUser.email || '';
    }
    
    orderModal.show();
}

async function editOrder(id) {
    try {
        const response = await fetch(`/api/orders/${id}`);
        const order = await response.json();
        
        document.getElementById('modalTitle').textContent = '주문 수정';
        document.getElementById('orderId').value = order.id;
        document.getElementById('productId').value = order.productId;
        document.getElementById('quantity').value = order.quantity;
        document.getElementById('totalPrice').value = order.totalPrice;
        document.getElementById('customerName').value = order.customerName;
        document.getElementById('customerEmail').value = order.customerEmail;
        document.getElementById('status').value = order.status;
        
        orderModal.show();
    } catch (error) {
        console.error('주문 정보를 불러오는데 실패했습니다:', error);
        alert('주문 정보를 불러오는데 실패했습니다.');
    }
}

async function saveOrder() {
    const id = document.getElementById('orderId').value;
    const order = {
        productId: parseInt(document.getElementById('productId').value),
        quantity: parseInt(document.getElementById('quantity').value),
        totalPrice: parseFloat(document.getElementById('totalPrice').value),
        customerName: document.getElementById('customerName').value,
        customerEmail: document.getElementById('customerEmail').value,
        status: document.getElementById('status').value
    };

    try {
        const url = id ? `/api/orders/${id}` : '/api/orders';
        const method = id ? 'PUT' : 'POST';
        
        console.log('주문 저장 요청:', { url, method, order });
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order)
        });

        if (!response.ok) {
            throw new Error('저장에 실패했습니다.');
        }

        const savedOrder = await response.json();
        console.log('주문 저장 성공:', savedOrder);

        // 주문 상태가 COMPLETED로 변경된 경우 RabbitMQ로 메시지 전송
        if (order.status === 'COMPLETED') {
            const product = products.find(p => p.id === order.productId);
            const message = {
                orderId: savedOrder.id,
                productName: product ? product.name : '알 수 없음',
                quantity: order.quantity,
                customerName: order.customerName,
                orderDate: new Date(savedOrder.orderDate).toISOString()
            };

            console.log('주문 완료 메시지 전송 시도:', message);

            try {
                const completeResponse = await fetch('/api/orders/complete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(message)
                });

                if (!completeResponse.ok) {
                    throw new Error('주문 완료 메시지 전송 실패');
                }

                console.log('주문 완료 메시지 전송 성공');
                alert('주문이 완료되었고 게시판에 등록되었습니다.');
            } catch (error) {
                console.error('주문 완료 메시지 전송에 실패했습니다:', error);
                alert('주문은 저장되었지만, 게시판 등록에 실패했습니다. 관리자에게 문의해주세요.');
            }
        }

        orderModal.hide();
        loadOrders();
        alert('저장되었습니다.');
    } catch (error) {
        console.error('저장에 실패했습니다:', error);
        alert('저장에 실패했습니다.');
    }
}

async function deleteOrder(id) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/orders/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('삭제에 실패했습니다.');
        }

        loadOrders();
        alert('삭제되었습니다.');
    } catch (error) {
        console.error('삭제에 실패했습니다:', error);
        alert('삭제에 실패했습니다.');
    }
} 
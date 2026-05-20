package com.study.rdb_study.order;

import com.study.rdb_study.cart.Cart;
import com.study.rdb_study.cart.CartRepository;
import com.study.rdb_study.cartItem.CartItem;
import com.study.rdb_study.cartItem.CartItemRepository;
import com.study.rdb_study.member.MemberRepository;
import com.study.rdb_study.order.dto.OrderDetailResponse;
import com.study.rdb_study.order.dto.OrderCreateRequest;
import com.study.rdb_study.order.dto.OrderResponse;
import com.study.rdb_study.orderItem.OrderItem;
import com.study.rdb_study.orderItem.OrderItemRepository;
import com.study.rdb_study.orderItem.OrderItemResponse;
import com.study.rdb_study.product.Product;
import com.study.rdb_study.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // 주문정보 저장, 주문상세 반환, order_items에 save, 장바구니 제거, 재고 차감
    public OrderDetailResponse save(OrderCreateRequest orderCreateRequest) {
        // 고객 존재 여부 검증
        memberRepository.findById(orderCreateRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객"));

        // 장바구니 존재 여부 겁증
        Cart cart = cartRepository.findByMemberId(orderCreateRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니"));

        // 장바구니 비어있는지 검증
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("빈 장바구니");
        }

        // 장바구니 각 아이템 존재 여부, 주문 개수 여부, 재고 부족 검증
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품ID: " + cartItem.getProductId()));

            if (cartItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("주문 개수는 1 이상이어야 합니다.");
            }

            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new IllegalArgumentException("-----재고 부족-----\n상품명: " + product.getName());
            }
        }

        // 주문 저장
        Order order = orderRepository.save(orderCreateRequest.toEntity());

        // 주문 아이템 저장 + 응답(반환)용 dto 생성 + 재고 차감
        for (CartItem cartItem : cartItems) {
            OrderItem item = OrderItem.builder()
                    .orderId(order.getOrderId())
                    .productId(cartItem.getProductId())
                    .quantity(cartItem.getQuantity())
                    .build();

            orderItemRepository.save(item);
            productRepository.decreaseStock(cartItem.getProductId(), cartItem.getQuantity());
        }

        cartItemRepository.deleteAllByCartId(cart.getCartId());

        List<OrderItemResponse> orderItems = orderItemRepository.findOrderItemsByOrderId(order.getOrderId());
        return OrderDetailResponse.toDto(order, orderItems);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findById(Long orderId, Long memberId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 조회 실패"));

        // Spring Security 도입하면 memberId직접 받지 않아도 됨
        if (!order.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("본인 주문만 조회 가능");
        }

        List<OrderItemResponse> orderItems = orderItemRepository.findOrderItemsByOrderId(orderId);
        return OrderDetailResponse.toDto(order, orderItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByMemberId(Long memberId) {

        List<Order> orders = orderRepository.findByMemberId(memberId);

        return orders.stream()
                .map(order -> {
                    List<OrderItemResponse> items = orderItemRepository.findOrderItemsByOrderId(order.getOrderId());
                    return OrderResponse.toDto(order, items);
                })
                .collect(Collectors.toList());
    }

    // 관리자용
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    List<OrderItemResponse> items = orderItemRepository.findOrderItemsByOrderId(order.getOrderId());
                    return OrderResponse.toDto(order, items);
                })
                .collect(Collectors.toList());
    }

    // 관리자용
//    public void update(Long id, OrderCreateRequest orderCreateRequest) {
//        if (!orderRepository.existsById(id))
//            throw new IllegalArgumentException("존재하지 않는 주문");
//
//        // orderRepository.update(orderUpdateRequest.toEntity(id));
//    }

    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문, orderId: " + id));

        // Security: 본인 삭제 검증 추가

        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new IllegalArgumentException("이미 취소된 주문, orderId: " + id);
        }

        if (order.getStatus().equals(OrderStatus.SHIPPED) || order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new IllegalArgumentException("배송 중이거나 배송 완료된 주문은 취소할 수 없습니다.");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        for (OrderItem orderItem : orderItems) {
            productRepository.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
        }

        orderRepository.updateStatus(id, OrderStatus.CANCELLED);
    }


}

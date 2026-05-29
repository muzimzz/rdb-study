package com.study.rdb_study.order;

import com.study.rdb_study.cart.Cart;
import com.study.rdb_study.cart.CartRepository;
import com.study.rdb_study.cartItem.CartItem;
import com.study.rdb_study.cartItem.CartItemRepository;
import com.study.rdb_study.coupon.Coupon;
import com.study.rdb_study.coupon.CouponRepository;
import com.study.rdb_study.coupon.MemberCoupon;
import com.study.rdb_study.coupon.MemberCouponRepository;
import com.study.rdb_study.global.exception.BadRequestException;
import com.study.rdb_study.global.exception.ForbiddenException;
import com.study.rdb_study.global.exception.NotFoundException;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    // 주문정보 저장, 주문상세 반환, order_items에 save, 장바구니 제거, 재고 차감
    public OrderDetailResponse save(Long memberId, OrderCreateRequest orderCreateRequest) {

        // 장바구니 존재 여부 겁증
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 장바구니"));

        // 장바구니 비어있는지 검증
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new BadRequestException("빈 장바구니");
        }

        // 장바구니 각 아이템 존재 여부, 주문 개수 여부, 재고 부족 검증
        Map<Long, Product> productMap = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 상품ID: " + cartItem.getProductId()));

            productMap.put(cartItem.getProductId(), product);
            if (cartItem.getQuantity() <= 0) {
                throw new BadRequestException("주문 개수는 1 이상이어야 합니다.");
            }

            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new BadRequestException("-----재고 부족-----\n상품명: " + product.getName());
            }
        }

        int totalDiscountAmount = 0;
        if (orderCreateRequest.getMemberCouponId() != null) {
            MemberCoupon memberCoupon = memberCouponRepository.findById(orderCreateRequest.getMemberCouponId())
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 쿠폰"));

            if (memberCoupon.isUsed())
                throw new BadRequestException("이미 사용한 쿠폰");

            Coupon coupon = couponRepository.findById(memberCoupon.getCouponId())
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 쿠폰"));

            if (coupon.getExpiredAt().isBefore(LocalDateTime.now()))
                throw new BadRequestException("만료된 쿠폰");

            int totalPrice = 0;
            for (CartItem cartItem : cartItems) {
                Product product = productMap.get(cartItem.getProductId());
                totalPrice += product.getPrice() * cartItem.getQuantity();
            }

            if (totalPrice < coupon.getMinOrderAmount())
                throw new BadRequestException("최소 주문 금액 미달");

            memberCouponRepository.markAsUsed(memberCoupon.getMemberCouponId());
            totalDiscountAmount = coupon.calculateDiscountAmount(totalPrice);
        }

        // 주문 저장
        Order order = orderRepository.save(orderCreateRequest.toEntity(memberId, totalDiscountAmount));

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
                .orElseThrow(() -> new NotFoundException("주문 조회 실패"));

        if (!order.getMemberId().equals(memberId)) {
            throw new ForbiddenException("본인 주문만 조회 가능");
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
//            throw new NotFoundException("존재하지 않는 주문");
//
//        // orderRepository.update(orderUpdateRequest.toEntity(id));
//    }

    public void cancelOrder(Long id, Long memberId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 주문, orderId: " + id));

        // Security: 본인 삭제 검증 추가
        if (!order.getMemberId().equals(memberId)) {
            throw new ForbiddenException("본인의 주문만 취소 가능");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("이미 취소된 주문, orderId: " + id);
        }
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("배송 중이거나 배송 완료된 주문은 취소할 수 없습니다.");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        for (OrderItem orderItem : orderItems) {
            productRepository.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
        }

        if (order.getMemberCouponId() != null)
            memberCouponRepository.markAsUnused(order.getMemberCouponId());

        orderRepository.updateStatus(id, OrderStatus.CANCELLED);
    }


}

package com.study.rdb_study.order;

import com.study.rdb_study.order.dto.OrderDetailResponse;
import com.study.rdb_study.order.dto.OrderRequest;
import com.study.rdb_study.order.dto.OrderResponse;
import com.study.rdb_study.orderItem.OrderItem;
import com.study.rdb_study.orderItem.OrderItemRepository;
import com.study.rdb_study.orderItem.OrderItemRequest;
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

    public OrderResponse save(OrderRequest orderRequest) {

        // 이 id검증 로직이 없어도 아래의 productRepository.decreaseStock()에서 재고가 음수가 될 경우
        // Transaction으로 롤백되지만, 불필요한 insert query로 인한 성능 저하를 막는다.
        // (없으면 insert, insert, insert, 재고검증 시 롤백 -> 불필요한 insert)
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "존재하지 않는 상품 ID: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity())
                throw new IllegalArgumentException(
                        "재고 부족 - 상품명: " + product.getName()
                        + " (요청: " + itemRequest.getQuantity()
                        + ", 재고: " + product.getStockQuantity() + ")");
        }

        // [2단계] 주문 저장
        Order order = orderRepository.save(orderRequest.toEntity());

        // [3단계] 주문 아이템 저장 + 재고 차감
        // decreaseStock()은 DB 레벨에서도 stock_quantity >= quantity 조건을 걸어둠
        // → 1단계와 3단계 사이에 동시 요청이 끼어들어도 음수 재고 방지
        // → decreaseStock()이 예외를 던지면 @Transactional이 전체(주문 포함) 롤백
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            orderItemRepository.save(itemRequest.toEntity(order.getOrderId()));
            productRepository.decreaseStock(itemRequest.getProductId(), itemRequest.getQuantity());
        }

        return OrderResponse.toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문 조회 실패"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        return OrderDetailResponse.toDto(order, orderItems.stream()
                .map(OrderItemResponse::toDto)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, OrderRequest orderRequest) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 주문");

        orderRepository.update(orderRequest.toEntityWithId(id));
    }

    public void deleteById(Long id) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 주문");
        orderRepository.deleteById(id);
    }
}

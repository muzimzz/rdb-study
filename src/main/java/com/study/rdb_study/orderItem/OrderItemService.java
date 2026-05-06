package com.study.rdb_study.orderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemResponse save(OrderItemRequest request) {
        orderItemRepository.save(request.toEntity());
        OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(request.getOrderId(), request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 추가 실패"));
        return OrderItemResponse.fromEntity(orderItem);
    }

    public OrderItemResponse increaseQuantity(Long orderId, Long productId, int addQuantity) {
        orderItemRepository.increaseQuantity(orderId, productId, addQuantity);
        OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new IllegalArgumentException("수량 추가 실패"));
        return OrderItemResponse.fromEntity(orderItem);
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponse> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteByOrderId(Long orderId) {
        orderItemRepository.deleteByOrderId(orderId);
    }

    public void deleteByOrderIdAndProductId(Long orderId, Long productId) {
        orderItemRepository.deleteByOrderIdAndProductId(orderId, productId);
    }
}

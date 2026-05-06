package com.study.rdb_study.orderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long orderId;
    private Long productId;
    private int quantity;

    public static OrderItemResponse toDto(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderId(orderItem.getOrderId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .build();
    }
}

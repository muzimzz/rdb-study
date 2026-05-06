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

    public static OrderItemResponse fromEntity(OrderItem orderitem) {
        return OrderItemResponse.builder()
                .orderId(orderitem.getOrderId())
                .productId(orderitem.getProductId())
                .quantity(orderitem.getQuantity())
                .build();
    }
}

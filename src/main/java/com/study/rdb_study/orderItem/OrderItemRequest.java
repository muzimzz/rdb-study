package com.study.rdb_study.orderItem;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequest {
    private Long orderId;
    private Long productId;
    private int quantity;

    public OrderItem toEntity() {
        return OrderItem.builder()
                .orderId(this.orderId)
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }

    public OrderItem toEntity(Long orderId) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }
}

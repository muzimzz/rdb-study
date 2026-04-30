package com.study.rdb_study.dto;

import com.study.rdb_study.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemRequest {
    private Long orderId;
    private Long productId;
    private int quantity;

    @Builder
    public OrderItem toEntity() {
        return OrderItem.builder()
                .orderId(this.orderId)
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }
}

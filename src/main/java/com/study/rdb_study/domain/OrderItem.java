package com.study.rdb_study.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 제1정규화 (Order: Product를 OrderItem으로 분리)
@Getter
@NoArgsConstructor
public class OrderItem {
    private Long orderId;
    private Long productId;
    private int quantity;

    @Builder
    public OrderItem(Long orderId, Long productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }
}

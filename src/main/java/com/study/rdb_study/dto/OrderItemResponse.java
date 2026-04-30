package com.study.rdb_study.dto;

import com.study.rdb_study.domain.OrderItem;
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

    static OrderItemResponse fromEntity(OrderItem orderitem) {
        return OrderItemResponse.builder()
                .orderId(orderitem.getOrderId())
                .productId(orderitem.getProductId())
                .quantity(orderitem.getQuantity())
                .build();
    }
}

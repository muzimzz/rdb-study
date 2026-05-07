package com.study.rdb_study.order.dto;

import com.study.rdb_study.order.Order;
import com.study.rdb_study.orderItem.OrderItemRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Long customerId;
    private String status;
    private List<OrderItemRequest> items;

    public Order toEntity() {
        return Order.builder()
                .customerId(this.customerId)
                .status(this.status)
                .build();
    }

    public Order toEntityWithId(Long id) {
        return Order.builder()
                .orderId(id)
                .customerId(this.customerId)
                .status(this.status)
                .build();
    }
}

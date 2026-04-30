package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Long customerId;
    private String status;

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

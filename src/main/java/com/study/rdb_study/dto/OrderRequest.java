package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Long customerId;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime orderDate;

    private List<OrderItemRequest> orderItems;

    public Order toEntity() {
        return Order.builder()
                .customerId(this.customerId)
                .status(this.status)
                .orderDate(this.orderDate)
                .orderItems(this.orderItems.stream()
                        .map(OrderItemRequest::toEntity)
                        .toList())
                .build();
    }

    public Order toEntityWithId(Long id) {
        return Order.builder()
                .orderId(id)
                .customerId(this.customerId)
                .status(this.status)
                .orderDate(this.orderDate)
                .build();
    }
}

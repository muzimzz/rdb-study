package com.study.rdb_study.order.dto;

import com.study.rdb_study.order.Order;
import com.study.rdb_study.order.OrderStatus;
import com.study.rdb_study.orderItem.OrderItemRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    public Order toEntity(Long id) {
        return Order.builder()
                .memberId(id)
                .status(OrderStatus.PENDING) // 주문 생성 시 무조건 PENDING으로 고정
                .build();
    }

}

package com.study.rdb_study.order.dto;

import com.study.rdb_study.order.Order;
import com.study.rdb_study.orderItem.OrderItemRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    private Long customerId;
    // private String status; --관리자용

    public Order toEntity() {
        return Order.builder()
                .customerId(this.customerId)
                .status("PENDING") // 주문 생성 시 무조건 PENDING으로 고정
                .build();
    }

    // 관리자용
//    public Order toEntityWithId(Long id) {
//        return Order.builder()
//                .orderId(id)
//                .customerId(this.customerId)
//                .status(this.status) // update에 사용: 배송상태 변경 가능하도록 받기
//                .build();
//    }
}

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

    private Long memberCouponId;  // 사용할 쿠폰 발급 ID, 없으면 null

    public Order toEntity(Long memberId, int disCountAmount) {
        return Order.builder()
                .memberId(memberId)
                .status(OrderStatus.PENDING) // 주문 생성 시 무조건 PENDING으로 고정
                .memberCouponId(memberCouponId)
                .discountAmount(disCountAmount)
                .build();
    }

}

package com.study.rdb_study.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequest {
    private Long customerId;

    @Builder
    public Cart toEntity() {
        return Cart.builder()
                .customerId(this.customerId)
                .build();
    }
}

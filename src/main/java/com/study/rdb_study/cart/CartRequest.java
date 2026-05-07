package com.study.rdb_study.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequest {
    public Long customerId;

    public Cart toEntity() {
        return Cart.builder()
                .customerId(this.customerId)
                .build();
    }
}

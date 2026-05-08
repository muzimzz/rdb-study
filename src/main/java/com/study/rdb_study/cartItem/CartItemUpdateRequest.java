package com.study.rdb_study.cartItem;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemUpdateRequest {
    private Long cartId;    // 시큐리티 도입 시 삭제 예정
    private int quantity;

    public CartItem toEntity() {
        return CartItem.builder()
                .cartId(this.cartId)
                .quantity(this.quantity)
                .build();
    }
}

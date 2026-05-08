package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private Long customerId;
    private List<CartItemResponse> cartItems;

    public static CartResponse toDto (Cart cart, List<CartItemResponse> cartItems) {
        return CartResponse.builder()
                .cartId(cart.getCartId())
                .customerId(cart.getCustomerId())
                .cartItems(cartItems)
                .build();
    }
}

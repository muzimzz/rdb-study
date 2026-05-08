package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class Cart {
    private Long cartId;
    private Long customerId;
    // private List<CartItemResponse>

    @Builder
    public Cart(Long cartId, Long customerId) {
        this.cartId = cartId;
        this.customerId = customerId;
    }
}

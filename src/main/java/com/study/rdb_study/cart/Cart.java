package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Cart {
    private Long id;
    private Long customerId;
    private LocalDateTime createdAt;
    // private List<CartItemResponse>

    @Builder
    public Cart(Long id, Long customerId, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }
}

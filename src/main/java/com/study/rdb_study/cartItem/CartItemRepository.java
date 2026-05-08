package com.study.rdb_study.cartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> CartItem.builder()
            .cartItemId(rs.getLong("cart_item_id"))
            .cartId(rs.getLong("cart_id"))
            .productId(rs.getLong("product_id"))
            .quantity(rs.getInt("quantity"))
            .build();
}

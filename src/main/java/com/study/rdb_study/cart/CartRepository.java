package com.study.rdb_study.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Cart> cartRowMapper = (rs, rowNum) -> Cart.builder()
            .cartId(rs.getLong("cart_id"))
            .customerId(rs.getLong("customer_id"))
            .build();

    public Cart save(Cart cart) {
        String sql = "insert into carts (customer_id) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((conn) -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, cart.getCustomerId());
            return pstmt;
        }, keyHolder);

        return findByCustomerId(cart.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("고객 조회 실패"));
    }

    public Optional<Cart> findByCustomerId(Long customerId) {
        String sql = "select cart_id, customer_id from carts where customer_id=?";
        List<Cart> result = jdbcTemplate.query(sql, cartRowMapper, customerId);

        return result.stream().findFirst();
    }

    public void deleteByCustomerId(Long customerId) {
        String sql = "delete from carts where customer_id=?";
        jdbcTemplate.update(sql, customerId);
    }
}

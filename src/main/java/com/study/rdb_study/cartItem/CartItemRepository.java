package com.study.rdb_study.cartItem;

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
public class CartItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> CartItem.builder()
            .cartItemId(rs.getLong("cart_item_id"))
            .cartId(rs.getLong("cart_id"))
            .productId(rs.getLong("product_id"))
            .quantity(rs.getInt("quantity"))
            .build();

    private final RowMapper<CartItemResponse> cartItemResponseRowMapper = (rs, rowNum) -> CartItemResponse.builder()
            .cartItemId(rs.getLong("cart_item_id"))
            .productName(rs.getString("name"))
            .price(rs.getInt("price"))
            .quantity(rs.getInt("quantity"))
            .build();

    public List<CartItemResponse> findCartItemsByCartId(Long cartId) {
        String sql = """
        select ci.cart_item_id, p.name, p.price, ci.quantity 
        from cart_items ci 
        join products p on ci.product_id = p.product_id 
        where ci.cart_id=?
        """;

        return jdbcTemplate.query(sql, cartItemResponseRowMapper, cartId);
    }

    public List<CartItem> findByCartId(Long cartId) {
        String sql = "select cart_item_id, cart_id, product_id, quantity from cart_items where cart_id=?";

        return jdbcTemplate.query(sql, cartItemRowMapper, cartId);
    }

    public CartItem save(CartItem cartItem) {
        String sql = "insert into cart_items (cart_id, product_id, quantity) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, cartItem.getCartId());
            pstmt.setLong(2, cartItem.getProductId());
            pstmt.setInt(3, cartItem.getQuantity());
            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new IllegalArgumentException("장바구니 상품 조회 실패"));
    }

    // 비즈니스 상 필요는 없지만 JPA가 save() 성공 시 객체를 반환하는 style을 따라 구현함
    public Optional<CartItem> findById(Long id) {
        String sql = "select cart_item_id, cart_id, product_id, quantity from cart_items where cart_item_id=?";
        return jdbcTemplate.query(sql, cartItemRowMapper, id)
                .stream()
                .findFirst();
    }

    public void updateQuantity(Long id, CartItem cartItem) {
        String sql = "update cart_items set quantity=? where cart_item_id=?";

        jdbcTemplate.update(sql, cartItem.getQuantity(), id);
    }

    public void deleteAllByCartId(Long cartId) {
        String sql = "delete from cart_items where cart_id=?";
        jdbcTemplate.update(sql, cartId);
    }

    public void deleteById(Long id) {
        String sql = "delete from cart_items where cart_item_id=?";
        jdbcTemplate.update(sql, id);
    }

    // Todo
    public Optional<CartItem> findByCartIdAndProductId() {
        // save: upsert 구현 시 필요
        return null;
    }
}

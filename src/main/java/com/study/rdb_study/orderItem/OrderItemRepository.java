package com.study.rdb_study.orderItem;

import com.study.rdb_study.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> OrderItem.builder()
            .orderId(rs.getLong("order_id"))
            .productId(rs.getLong("product_id"))
            .quantity(rs.getInt("quantity"))
            .build();
    private final RowMapper<OrderItemResponse> orderItemResponseRowMapper = (rs, rowNum) -> OrderItemResponse.builder()
            .productName(rs.getString("name"))
            .price(rs.getInt("price"))
            .quantity(rs.getInt("quantity"))
            .build();

    // 주문 아이템 저장(내부용)
    public OrderItem save(OrderItem orderItem) {
        // duplicate key 문법을 이용해 save+increase 합치기 가능
//      String sql = "INSERT INTO order_items (order_id, product_id, quantity)"
//      + " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        String sql = "insert into order_items (order_id, product_id, quantity) values (?, ?, ?)";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());

        return orderItem;
    }

    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = """
                select count(*) from order_items oi
                join orders o on oi.order_id=o.order_id
                where o.member_id = ? 
                  and oi.product_id = ?
                  and o.status != ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId, OrderStatus.CANCELLED.name());
        return count != null && count > 0;
    }

    // 주문 상세 조회용(JOIN)
    public List<OrderItemResponse> findOrderItemsByOrderId(Long orderId) {
        String sql = """
            select p.name, p.price, oi.quantity 
            from order_items oi
            join products p on oi.product_id=p.product_id
            where oi.order_id=?
            """;

        return jdbcTemplate.query(sql, orderItemResponseRowMapper, orderId);
    }

    // save 검증용
    public Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId) {
        String sql = "select order_id, product_id, quantity from order_items where order_id=? and product_id=?";
        List<OrderItem> result = jdbcTemplate.query(sql, orderItemRowMapper, orderId, productId);

        return result.stream().findFirst();
    }

    // cancelOrder용
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "select order_id, product_id, quantity from order_items where order_id=?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }
}
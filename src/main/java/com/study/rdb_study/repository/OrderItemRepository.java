package com.study.rdb_study.repository;

import com.study.rdb_study.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;
    
    public void save(OrderItem orderItem) {
        // duplicate key 문법을 이용해 save+increase 합치기 가능
//      String sql = "INSERT INTO order_items (order_id, product_id, quantity)"
//      + " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        String sql = "insert into order_items (order_id, product_id, quantity) values (?, ?, ?)";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());
    }

    public void increaseQuantity (Long orderId, Long productId, int addQuantity) {
        String sql = "update order_items set quantity=quantity+? where order_id=? and product_id=?";
        jdbcTemplate.update(sql, addQuantity, orderId, productId);
    }

    // findAll()은 모든 주문을 조회하기 때문에 서비스 상 필요x
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "select order_id, product_id, quantity from order_items where order_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> OrderItem.builder()
                .orderId(rs.getLong("order_id"))
                .productId(rs.getLong("product_id"))
                .quantity(rs.getInt("quantity"))
                .build(), orderId);
    }

    // 전체 취소
    public void deleteByOrderId(Long orderId) {
        String sql = "delete from order_items where order_id=?";
        jdbcTemplate.update(sql, orderId);
    }

    // 부분 취소: 요구사항에 따라 추가 검토
    public void deleteByOrderIdAndProductId(Long orderId, Long productId) {
        String sql = "delete from order_items where order_id=? and product_id=?";
        jdbcTemplate.update(sql, orderId, productId);
    }
}

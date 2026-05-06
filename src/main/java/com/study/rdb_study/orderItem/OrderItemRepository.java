package com.study.rdb_study.orderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderItem save(OrderItem orderItem) {
        // duplicate key 문법을 이용해 save+increase 합치기 가능
//      String sql = "INSERT INTO order_items (order_id, product_id, quantity)"
//      + " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        String sql = "insert into order_items (order_id, product_id, quantity) values (?, ?, ?)";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());

        return findByOrderIdAndProductId(orderItem.getOrderId(), orderItem.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("저장된 데이터 조회 실패"));
    }

    public void increaseQuantity (Long orderId, Long productId, int addQuantity) {
        String sql = "update order_items set quantity=quantity+? where order_id=? and product_id=?";
        jdbcTemplate.update(sql, addQuantity, orderId, productId);
    }

    // 주문 아이템 1개만 반환
    public Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId) {
        String sql = "select order_id, product_id, quantity from order_items where order_id=? and product_id=?";
//        try {
//            OrderItem orderItem = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> OrderItem.builder()
//                    .orderId(rs.getLong("order_id"))
//                    .productId(rs.getLong("product_id"))
//                    .quantity(rs.getInt("quantity"))
//                    .build(), orderId, productId);
//            return Optional.ofNullable(orderItem);
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }

        // QueryForObject(): 단건 조회 -> try-catch로 감싸줘야 함
        // Query(): 리스트로 반환 -> 데이터가 들어가지 않아도 예외 던질 필요x
        // findById는 Optional<Data>, findAll은 List<Data> return하는 것과 같은 맥락
        List<OrderItem> result = jdbcTemplate.query(sql, (rs, rowNum) -> OrderItem.builder()
                .orderId(rs.getLong("order_id"))
                .productId(rs.getLong("product_id"))
                .quantity(rs.getInt("quantity"))
                .build(), orderId, productId);

        return result.stream().findFirst();

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
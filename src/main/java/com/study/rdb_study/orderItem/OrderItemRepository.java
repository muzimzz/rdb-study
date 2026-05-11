package com.study.rdb_study.orderItem;

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

    // 주문 아이템 저장(내부용)
    public OrderItem save(OrderItem orderItem) {
        // duplicate key 문법을 이용해 save+increase 합치기 가능
//      String sql = "INSERT INTO order_items (order_id, product_id, quantity)"
//      + " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        String sql = "insert into order_items (order_id, product_id, quantity) values (?, ?, ?)";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());

        return findByOrderIdAndProductId(orderItem.getOrderId(), orderItem.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("저장된 데이터 조회 실패"));
    }

    // 주문 아이템 1개만 반환, save() 후 반환, 취소 시 재고 복구용
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
        List<OrderItem> result = jdbcTemplate.query(sql, orderItemRowMapper, orderId, productId);

        return result.stream().findFirst();

    }

    // 주문 상세 조회
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "select order_id, product_id, quantity from order_items where order_id=?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }
}
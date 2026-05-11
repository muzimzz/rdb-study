package com.study.rdb_study.order;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> Order.builder()
            .orderId(rs.getLong("order_id"))
            .customerId(rs.getLong("customer_id"))
            .orderDate(rs.getTimestamp("order_date").toLocalDateTime())
            .status(rs.getString("status"))
            .build();

    // 주문 생성
    public Order save(Order order) {
        String sql = "insert into orders (customer_id, status) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, order.getCustomerId());
            pstmt.setString(2, order.getStatus());
            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new IllegalArgumentException("주문 조회 실패"));
    }

    // 주문 단건 조회, 주문 취소 시 검증
    public Optional<Order> findById(Long id) {
        String sql = "select order_id, customer_id, order_date, status from orders where order_id=?";

        return jdbcTemplate.query(sql, orderRowMapper, id)
                .stream()
                .findFirst();
    }

    // 내 주문 목록
    public List<Order> findByCustomerId(Long id) {
        String sql = "select order_id, customer_id, order_date, status from orders where customer_id=?";

        return jdbcTemplate.query(sql, orderRowMapper, id);
    }

    // 관리자용
    public List<Order> findAll() {
        String sql = "select order_id, customer_id, order_date, status from orders";

        return jdbcTemplate.query(sql, orderRowMapper);
    }

    // 관리자용
    public void update(Order newOrder) {
        String sql = "update orders set status=? where order_id=?";

        jdbcTemplate.update(sql, newOrder.getStatus(), newOrder.getOrderId());
    }

    // 관리자용
    public void deleteById(Long id) {
        String sql = "delete from orders where order_id=?";

        jdbcTemplate.update(sql, id);
    }

    // 주문 정보 강제 수정 시 검증(관리자용)
    public boolean existsById(Long id) {
        String sql = "select count(*) from orders where order_id=?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }

    // 주문 취소(CANCELLED)
    public void updateStatus(Long id, String status) {
        String sql = "update orders set status=? where order_id=?";
        jdbcTemplate.update(sql, status, id);
    }
}

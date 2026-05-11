package com.study.rdb_study.customer;

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
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Customer> customerRowMapper = (rs, rowNum) -> Customer.builder()
            .customerId(rs.getLong("customer_id"))
            .name(rs.getString(("name")))
            .email(rs.getString("email"))
            .address(rs.getString("address"))
            .status(rs.getString("status"))
            .joinDate(rs.getTimestamp("join_date").toLocalDateTime())
            .build();

    // 회원가입
    public Customer save(Customer customer) {
        String sql = "insert into customers (name, email, password, address, status) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPassword());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getStatus());
            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new IllegalArgumentException("고객 조회 실패"));
    }

    // 회원 조회
    public Optional<Customer> findById(Long id) {
        String sql = "select customer_id, name, email, address, status, join_date from customers where customer_id=?";

        List<Customer> result = jdbcTemplate.query(sql, customerRowMapper, id);
        return result.stream().findFirst();
    }

    // 현재 비밀번호 검증(관리자용?)
    public Optional<String> findPasswordById(Long id) {
        String sql = "select password from customers where customer_id=?";

        List<String> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return rs.getString(1);
            } ,id);

        return result.stream().findFirst();
    }

    // 관리자용
    public List<Customer> findAll() {
        String sql = "select customer_id, name, email, address, status, join_date from customers";

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    // 회원정보 수정
    public void update(Customer customer) {
        String sql = "update customers set email=?, address=? where customer_id=?";

        jdbcTemplate.update(sql, customer.getEmail(), customer.getAddress(), customer.getCustomerId());
    }

    // 비밀번호 변경
    public void updatePassword(Long id, String newPassword) {
        String sql = "update customers set password=? where customer_id=?";

        jdbcTemplate.update(sql, newPassword, id);
    }

    // 회원탈퇴 (INACTIVE)
    public void updateStatus(Long id, String status) {
        String sql = "update customers set status=? where customer_id=?";

        jdbcTemplate.update(sql, status, id);
    }

    // update 시 회원 존재 검증
    public boolean existsById(Long id) {
        String sql = "select count(*) from customers where customer_id=?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }
}
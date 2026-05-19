package com.study.rdb_study.admin.product;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AdminProductResponse> adminProductRowMapper = (rs, rowNum) -> AdminProductResponse.builder()
            .productId(rs.getLong("product_id"))
            .name(rs.getString("name"))
            .price(rs.getInt("price"))
            .stockQuantity(rs.getInt("stock_quantity"))
            .description(rs.getString("description"))
            .status(rs.getString("status"))
            .build();

    // Todo: JPA+QueryDSL로 하나의 메서드에서 조건 조합
    // 품절임박 상품 조회
    public List<AdminProductResponse> findLowStockProducts() {
        String sql = """
                select product_id, name, price, stock_quantity, description, status, from products" +
                where stock_quantity < 5
                """;
        return null;
    }

    // 장기 미판매 상품 조회
    public List<AdminProductResponse> findLongUnsoldProducts() {
        // TODO
        return null;
    }

    // 상품 목록
    public List<AdminProductResponse> findAll() {
        String sql = "select product_id, name, price, stock_quantity, description, status from products";

        return jdbcTemplate.query(sql, adminProductRowMapper);
    }

    //  단건 상품 조회(관리자용, 비활성화 상품 포함)
    public Optional<AdminProductResponse> findById(Long id) {
        String sql = "select product_id, name, price, stock_quantity, description, status from products where product_id = ?";

        List<AdminProductResponse> result = jdbcTemplate.query(sql, adminProductRowMapper, id);
        return result.stream().findFirst();
    }

    public boolean existsById(Long id) {
        String sql = "select count(*) from products where product_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }
}

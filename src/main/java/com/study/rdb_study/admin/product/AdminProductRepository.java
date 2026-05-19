package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.Product;
import com.study.rdb_study.product.ProductRepository;
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

    // 품절임박 상품 조회
    public List<AdminProductResponse> findLowStockProducts() {
        // TODO
        return null;
    }

    // 장기 미판매 상품 조회
    public List<AdminProductResponse> findLongUnsoldProducts() {
        // TODO
        return null;
    }
}

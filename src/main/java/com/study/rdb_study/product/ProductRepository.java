package com.study.rdb_study.product;

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
public class ProductRepository {

//    private final JdbcTemplate jdbcTemplate;
//
//    public ProductRepository(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> Product.builder()
            .productId(rs.getLong("product_id"))
            .name(rs.getString("name"))
            .price(rs.getInt("price"))
            .stockQuantity(rs.getInt("stock_quantity"))
            .description(rs.getString("description"))
            .build();

    // 관리자용
    public Product save(Product product) {
        String sql = "insert into products (name, price, stock_quantity, description) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStockQuantity());
            pstmt.setString(4, product.getDescription());
            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new IllegalArgumentException("상품 조회 실패"));
    }

    public Optional<Product> findById(Long id) {
        String sql = "select product_id, name, price, stock_quantity, description from products where product_id = ?";

        List<Product> result = jdbcTemplate.query(sql, productRowMapper, id);
        return result.stream().findFirst();
    }

    public List<Product> findAll() {
        String sql = "select product_id, name, price, stock_quantity, description from products";

        return jdbcTemplate.query(sql, productRowMapper);
    }

    // 관리자용
    public void update(Product product) {
        String sql = "update products set name=?, price=?, stock_quantity=?, description=? where product_id=?";

        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getStockQuantity(), product.getDescription(), product.getProductId());
    }

    // 관리자용
    public void deleteById(Long id) {
        String sql = "delete from products where product_id=?";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "select count(*) from products where product_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }

    public void decreaseStock(Long productId, int quantity) {
        // product.save() 시점의 quantity는 업데이트 되지 않기 때문에 DB레벨에서 stock_quantity를 검사한다
        String sql = "update products set stock_quantity = stock_quantity - ? " +
                     "where product_id = ? and stock_quantity >= ?";

        // 영향받은 row가 0이면 재고 부족 (또는 동시 요청으로 인한 race condition)
        int updatedRows = jdbcTemplate.update(sql, quantity, productId, quantity);
        if (updatedRows == 0)
            throw new IllegalArgumentException("재고가 부족합니다.");
    }

    public void increaseStock(Long productId, int quantity) {
        String sql = "update products set stock_quantity = stock_quantity + ? where product_id = ?";
        jdbcTemplate.update(sql, quantity, productId);
    }
}
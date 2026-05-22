package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.ProductCategory;
import com.study.rdb_study.product.ProductStatus;
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
            .category(ProductCategory.valueOf(rs.getString("category")))
            .price(rs.getInt("price"))
            .stockQuantity(rs.getInt("stock_quantity"))
            .description(rs.getString("description"))
            .status(ProductStatus.valueOf(rs.getString("status")))
            .createdAt(rs.getTimestamp("created_at") != null
                    ? rs.getTimestamp("created_at").toLocalDateTime() : null)
            .build();

    // Todo: JPA+QueryDSL로 하나의 메서드에서 조건 조합
    // 품절임박 상품 조회
    public List<AdminProductResponse> findLowStockProducts() {
        String sql = """
                select product_id, name, category, price, stock_quantity, description, status, created_at
                from products
                where stock_quantity < 5
                """;
        return jdbcTemplate.query(sql, adminProductRowMapper);
    }

    // 장기 미판매 상품 조회 (30일 이상 주문 없거나 한 번도 안 팔린 상품)
    public List<AdminUnsoldProductResponse> findLongUnsoldProducts() {
        String sql = """
                SELECT
                    p.product_id,
                    p.name,
                    p.category,
                    p.price,
                    p.stock_quantity,
                    MAX(o.order_date) AS last_order_date,
                    DATEDIFF(NOW(), COALESCE(MAX(o.order_date), p.created_at)) AS days_unsold
                FROM products p
                LEFT JOIN order_items oi ON p.product_id = oi.product_id
                LEFT JOIN orders o ON oi.order_id = o.order_id AND o.status != 'CANCELLED'
                WHERE p.status = 'ON_SALE'
                GROUP BY p.product_id, p.name, p.category, p.price, p.stock_quantity, p.created_at
                HAVING MAX(o.order_date) < DATE_SUB(NOW(), INTERVAL 30 DAY)
                    OR MAX(o.order_date) IS NULL
                ORDER BY days_unsold DESC
                """;

        RowMapper<AdminUnsoldProductResponse> unsoldRowMapper = (rs, rowNum) -> AdminUnsoldProductResponse.builder()
                .productId(rs.getLong("product_id"))
                .name(rs.getString("name"))
                .category(ProductCategory.valueOf(rs.getString("category")))
                .price(rs.getInt("price"))
                .stockQuantity(rs.getInt("stock_quantity"))
                .lastOrderDate(rs.getTimestamp("last_order_date") != null
                        ? rs.getTimestamp("last_order_date").toLocalDateTime() : null)
                .daysUnsold(rs.getObject("days_unsold") != null
                        ? rs.getLong("days_unsold") : null)
                .build();

        return jdbcTemplate.query(sql, unsoldRowMapper);
    }

    // 상품 목록
    public List<AdminProductResponse> findAll() {
        String sql = "select product_id, name, category, price, stock_quantity, description, status, created_at from products";

        return jdbcTemplate.query(sql, adminProductRowMapper);
    }

    //  단건 상품 조회(관리자용, 비활성화 상품 포함)
    public Optional<AdminProductResponse> findById(Long id) {
        String sql = "select product_id, name, category, price, stock_quantity, description, status, created_at from products where product_id = ?";

        List<AdminProductResponse> result = jdbcTemplate.query(sql, adminProductRowMapper, id);
        return result.stream().findFirst();
    }

    public boolean existsById(Long id) {
        String sql = "select count(*) from products where product_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }
}

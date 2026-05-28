package com.study.rdb_study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class SubQueryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("서브쿼리 테스트")
    void 서브쿼리_테스트() {
        long start_scalar = System.currentTimeMillis();

        String sql_scalar = """
                SELECT c.customer_id, c.name,
                    (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.customer_id) AS order_count
                FROM customers c
                """;

        jdbcTemplate.query(sql_scalar, (rs, rowNum) -> rs.getString("name"));

        long end_scalar = System.currentTimeMillis();
        System.out.println("스칼라 서브쿼리 실행 시간: " + (end_scalar - start_scalar) + "ms");

        long start_inlineView = System.currentTimeMillis();

        String sql_inlineView = """
                SELECT c.customer_id, c.name, COALESCE(t.order_count, 0) AS order_count
                FROM customers c
                LEFT JOIN (
                    SELECT customer_id, COUNT(*) AS order_count
                    FROM orders
                    GROUP BY customer_id
                ) t ON c.customer_id = t.customer_id
                """;

        jdbcTemplate.query(sql_inlineView, (rs, rowNum) -> rs.getString("name"));

        long end_inlineView = System.currentTimeMillis();
        System.out.println("인라인 뷰 실행 시간: " + (end_inlineView - start_inlineView) + "ms");
    }
}

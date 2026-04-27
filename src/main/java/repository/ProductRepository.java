package repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}

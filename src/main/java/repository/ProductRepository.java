package repository;

import domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(Product product) {
        String sql = "insert into product " +
                "(name, price, stock_quantity, description) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getDescription()
        );
    }
}

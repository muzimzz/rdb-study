package repository;

import com.zaxxer.hikari.HikariDataSource;
import domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

//    private final JdbcTemplate jdbcTemplate;
//
//    public ProductRepository(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    private final DataSource dataSource;

    public void save(Product product) {
        String sql = "insert into products (name, price, stock_quantity, description) values (?, ?, ?, ?)";


        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStockQuantity());
            pstmt.setString(4, product.getDescription());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public Product findById(Long id) {
        String sql = "select product_id, name, price, stock_quantity, description from products where product_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Product product = mapRow(rs);
                return product;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public List<Product> findAll() {
        String sql = "select product_id, name, price, stock_quantity, description from products";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(mapRow(rs));
            }
            return products;


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public void update(Product product) {
        String sql = "update products set name=?, price=?, stock_quantity=?, description=? where product_id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStockQuantity());
            pstmt.setString(4, product.getDescription());
            pstmt.setLong(5, product.getProductId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from products where product_id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getLong("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getInt("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setDescription(rs.getString("description"));
        return product;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

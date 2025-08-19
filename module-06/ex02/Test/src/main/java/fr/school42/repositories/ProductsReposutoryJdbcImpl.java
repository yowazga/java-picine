/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ProductsReposutoryJdbcImpl.java                    :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/10 13:27:09 by Younes            #+#    #+#             */
/*   Updated: 2025/06/11 17:56:13 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import fr.school42.models.Product;

public class ProductsReposutoryJdbcImpl implements ProductsRepository {

    private final DataSource dataSource;

    public ProductsReposutoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAll() {
        
        List products = new ArrayList<>();
        
        String sql = "SELECT * FROM product";
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
                
                while(rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
                
             } catch (SQLException e) {
                throw new RuntimeException("Failed to find all products", e);
             }
        
        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {

        String sql = "SELECT * FROM product WHERE id = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find product by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Product product) {

        String sql = "UPDATE product SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getPrice());
                stmt.setLong(3, product.getId());

                int effectedRows = stmt.executeUpdate();
                if (effectedRows == 0) {
                    throw new RuntimeException("No product found with id: " + product.getId());
                }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product: " + product, e);
        }
    }

    @Override
    public void save(Product product) {

        String sql = "INSERT INTO product (id, name, price) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, product.getId());
                stmt.setString(2, product.getName());
                stmt.setDouble(3, product.getPrice());

                stmt.executeUpdate();
                
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save product: " + product, e);
        }
    }

    @Override
    public void delete(Long id) {

        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, id);
                
                int effectedRows = stmt.executeUpdate();
                if (effectedRows == 0) {
                    throw  new RuntimeException("No product found with id: " + id);
                }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product: with id: " + id, e);
        }
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        
        return new Product(rs.getLong("id"),
                           rs.getString("name"),
                           rs.getDouble("price"));
    }

}

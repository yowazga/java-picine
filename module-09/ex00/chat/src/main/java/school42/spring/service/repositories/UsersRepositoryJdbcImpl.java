/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepositoryJdbcImpl.java                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/26 15:26:35 by Younes            #+#    #+#             */
/*   Updated: 2025/06/27 18:41:05 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.repositories;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import school42.spring.service.models.User;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    private final DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findById(Long id) {
        
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String email = rs.getString("email");
                    User user = new User(id, email);
                    return user;
                }
                return null;
        } catch (SQLException e) {
            
            throw new RuntimeException("Failed to find user by ID: " + id + e);
        }
    }

    @Override
    public List<User> findAll() {
        
        List<User> users = new ArrayList<>();
        
        String sql = "SELECT * FROM users";
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                
            while (rs.next()) {
                Long id = rs.getLong("id");
                String email = rs.getString("email");
                users.add(new User(id, email));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch users", e);
        }

        return users;
    }

    @Override
    public void save(User entity) {
        
        String sql = "INSERT INTO users (email) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, entity.getEmail());
                stmt.executeUpdate();
                
                ResultSet key = stmt.getGeneratedKeys();
                if (key.next()) {
                    entity.setId(key.getLong("id"));;
                }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + entity + e);
        }
    }

    @Override
    public void update(User entity) {
        
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, entity.getEmail());
                stmt.setLong(2, entity.getId());

                stmt.executeUpdate();
            
        } catch (SQLException e) {
            
            throw new RuntimeException("Failed to update user: " + entity + e);
        }
    }

    @Override
    public void delete(Long id) {
        
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, id);

                stmt.executeUpdate();
            
        } catch (Exception e) {
           
           throw new RuntimeException("Failed to delete user with id: " + id + e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        
       
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String newEmail = rs.getString("email");
                    
                    return Optional.of(new User(id, newEmail));
                }
            
                return Optional.empty();
        } catch (Exception e) {
            
            throw new RuntimeException("Failed to find user by email: " + email + e);
        }
    }

}

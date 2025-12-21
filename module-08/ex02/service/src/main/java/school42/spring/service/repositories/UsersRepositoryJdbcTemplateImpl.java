/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepositoryJdbcTemplateImpl.java               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/27 18:44:50 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 10:01:04 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package school42.spring.service.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import school42.spring.service.models.User;

@Component("usersRepositoryJdbcTemplate")
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository, RowMapper<User>{

    private final JdbcTemplate jdbcTemplate;

    public UsersRepositoryJdbcTemplateImpl(@Qualifier("driverManagerDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
                );
    }
    
    @Override
    public User findById(Long id) {
        
        String sql = "SELECT * FROM users WHERE id = ?";

        List<User> users = jdbcTemplate.query(sql, this, id);

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        
        String sql = "SELECT * FROM users";
        
        return jdbcTemplate.query(sql, this);
    }

    @SuppressWarnings("null")
    @Override
    public void save(User entity) {
       
        String sql ="INSERT INTO users (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getPassword());
            return stmt;
        }, keyHolder);
        
        if (keyHolder.getKey() != null) {
            entity.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void update(User entity) {
        
        String sql = "UPDATE users SET email = ?, password = ? WHERE id = ?";

        jdbcTemplate.update(sql, entity.getEmail(),entity.getPassword(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        
        String sql = "DELETE FROM users WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        
        String sql = "SELECT * FROM users WHERE email = ?";

        List<User> users = jdbcTemplate.query(sql, this, email);
        
        return users.stream().findFirst();
    }

}

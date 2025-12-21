/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepositoryImpl.java                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/29 18:04:06 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 17:02:24 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import fr.school42.sockets.models.User;

@Component
public class UsersRepositoryImpl implements UsersRepository, RowMapper<User> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(@Qualifier("hikariDataSource")DataSource dataSource) {
        
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
        
        String sql = "INSERT INTO users (login, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, entity.getLogin());
            stmt.setString(2, entity.getPassword());
            return stmt;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("id");
        if (id != null) {
            entity.setId(id.longValue());
        }
    }

    @Override
    public void update(User entity) {
        
        String sql = "UPDATE users SET login = ?, password = ?";
        
        jdbcTemplate.update(sql, entity.getLogin(), entity.getPassword());
    }

    @Override
    public void delete(Long id) {
        
        String sql = "DELETE FROM users WHERE id = ?";
        
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        
        String sql = "SELECT * FROM users WHERE login = ?";

        List<User> users = jdbcTemplate.query(sql, this, login);

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0)) ;
    }

    @Override
    @Nullable
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("password")
            );
    }

}

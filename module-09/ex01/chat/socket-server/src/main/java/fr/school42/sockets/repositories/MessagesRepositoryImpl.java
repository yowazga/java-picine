/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesRepositoryImpl.java                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 09:49:23 by Younes            #+#    #+#             */
/*   Updated: 2025/12/21 17:05:14 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.User;

@Component
public class MessagesRepositoryImpl implements MessagesRepository,  RowMapper<Message> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessagesRepositoryImpl(@Qualifier("hikariDataSource")DataSource dataSource) {
        
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public void save(Message message) {
        
        String sql = "INSERT INTO messages (user_id, text, created_at) VALUES (?, ?, ?)";
        
        jdbcTemplate.update(sql, message.getSender().getId(), message.getMessage(), message.getTimestamp());
    }

    @Override
    public List<Message> findAll() {
        
        String sql = "SELECT * FROM messages ORDER BY timestamp ASC";

        return jdbcTemplate.query(sql, this);
    }

    @Override
    public Message findById(Long id) {
        
        String sql = "SELECT * FROM messages WHERE id = ?";
        
        return jdbcTemplate.query(sql, this, id).stream().findAny().orElse(null);
    }

    @Override
    public void update(Message entity) {
        
        String sql = "UPDATE messages SET user_id = ? , text = ? , created_at = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                entity.getSender().getId(), entity.getMessage(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public void delete(Long id) {
       
        String sql = "DELETE FROM messages WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
    
    @SuppressWarnings("null")
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        User sender = new User(rs.getLong("user_id"), rs.getString("login"), rs.getString("password"));

        return new Message(
            rs.getLong("id"),
            sender,
            rs.getString("text"),
            rs.getTimestamp("created_at")
        );
    }
}

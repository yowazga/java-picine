/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesRepositoryImpl.java                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 09:49:23 by Younes            #+#    #+#             */
/*   Updated: 2025/07/03 18:33:44 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.User;

@Repository
public class MessagesRepositoryImpl implements MessagesRepository,  RowMapper<Message> {

    private final JdbcTemplate jdbcTemplate;

    public MessagesRepositoryImpl(DataSource dataSource) {
        
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public void save(Message message) {
        
        String sql = "INSERT INTO messages (user_id, text, created_at) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, message.getSender().getId());
            stmt.setString(2, message.getMessage());
            stmt.setObject(3, message.getTimestamp());
            return stmt;
        }, keyHolder);

        @SuppressWarnings("null")
        Number id = (Number) keyHolder.getKeys().get("id");
        if (id != null) {
            message.setId(id.longValue());
}
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
        
        User sender = new User();
        sender.setId(rs.getLong("user_id"));

        return new Message(
            rs.getLong("id"),
            sender,
            rs.getString("text"),
            rs.getTimestamp("created_at")
        );
    }
}

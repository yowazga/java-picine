/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesRepositoryImpl.java                        :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 09:49:23 by Younes            #+#    #+#             */
/*   Updated: 2025/12/22 10:08:00 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.Room;
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
        
        String sql = "INSERT INTO messages (user_id, text, created_at, room_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, message.getSender().getId());
            stmt.setString(2, message.getMessage());
            stmt.setTimestamp(3, new Timestamp(message.getTimestamp().getTime()));
            stmt.setLong(4, message.getRoom().getId());
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
        
        String sql = "SELECT m.*, u.login, u.password, r.name AS room_name " +
             "FROM messages m " +
             "JOIN users u ON m.user_id = u.id " +
             "JOIN rooms r ON m.room_id = r.id " +
             "ORDER BY m.created_at";

        return jdbcTemplate.query(sql, this);
    }

    @Override
    public Message findById(Long id) {
        
        String sql = "SELECT m.*, u.login, u.password, r.name AS room_name " +
             "FROM messages m " +
             "JOIN users u ON m.user_id = u.id " +
             "JOIN rooms r ON m.room_id = r.id " +
             "WHERE m.id = ?";
        
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
        sender.setLogin(rs.getString("login"));
        sender.setPassword(rs.getString("password"));
        
        Room room = new Room(rs.getLong("room_id"), rs.getString("room_name"));
        
        return new Message(
            rs.getLong("id"),
            sender,
            room,
            rs.getString("text"),
            rs.getTimestamp("created_at")
        );
    }

    @Override
    public List<Message> findByRoomId(Long roomId, int limit, int offset) {

        String sql = "SELECT m.*, u.login, u.password, r.name AS room_name FROM messages m JOIN users u ON m.user_id = u.id JOIN rooms r ON m.room_id = r.id WHERE m.room_id = ? ORDER BY m.created_at LIMIT ? OFFSET ?";
        
        return jdbcTemplate.query(sql, this, roomId, limit, offset);
    }
}

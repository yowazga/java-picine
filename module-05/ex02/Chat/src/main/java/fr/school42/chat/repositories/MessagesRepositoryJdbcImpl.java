/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   MessagesRepositoryJdbcImpl.java                    :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/13 13:29:30 by Younes            #+#    #+#             */
/*   Updated: 2025/05/16 16:52:28 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.repositories;

import java.sql.Statement;
import java.sql.Timestamp;
// import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;

import fr.school42.chat.exceptions.NotSavedSubEntityException;
import fr.school42.chat.models.Chatroom;
import fr.school42.chat.models.Message;
import fr.school42.chat.models.User;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {

    private final DataSource dataSource;
    private static String SQL_QUERY =
        "SELECT m.id AS message_id, m.text, m.created_at, " +
        "u.id AS author_id, u.login AS author_login, " +
        "r.id AS room_id, r.name AS room_name " +
        "FROM messages m " +
        "LEFT JOIN users u ON m.author_id = u.id " +
        "LEFT JOIN chatrooms r ON m.room_id = r.id " +
        "WHERE m.id = ?";
    
    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    private void validateMessage(Message message) {
        if (message.getAuthor() == null || message.getAuthor().getId() == null)
            throw new NotSavedSubEntityException("Message author must exist in DB");
        if (message.getChatroom() == null || message.getChatroom().getId() == null) 
            throw new NotSavedSubEntityException("Message chatroom must exist in DB");
    }

    private void setStatementParameters(PreparedStatement statement, Message message) throws SQLException{
        
        statement.setLong(1, message.getAuthor().getId());
        statement.setLong(2, message.getChatroom().getId());
        statement.setString(3, message.getText());
        
        if (message.getCreatedAt() != null)
            statement.setTimestamp(4, Timestamp.valueOf(message.getCreatedAt()));
        else
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
    }

    private void executeInsert(PreparedStatement statement, Message message) throws SQLException {
        
        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Creating message failed, no rows affected");
            
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next())
                message.setId(generatedKeys.getLong(1));
            else
                throw new SQLException("Creating message failed, no ID obtained");
        }
    }

    @Override
    public void save(Message message) {
        validateMessage(message);
        
        String sql = "INSERT INTO messages (author_id, room_id, text, created_at)" +
                     "VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (!connection.isValid(2))
                    throw new SQLException("Database connection is not valid");
                setStatementParameters(statement, message);
                executeInsert(statement, message);
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save message", e);
        }
    }
    
    @Override
    public Optional<Message> findById(Long id) {
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY))
              {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                    
                        Message message = new Message();
                        message.setId(rs.getLong("message_id"));
                        message.setText(rs.getString("text"));
                        LocalDateTime dateTime = rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime();
                        message.setCreatedAt(dateTime);
                    
                        User author = new User();
                        author.setId(rs.getLong("author_id"));
                        author.setLogin(rs.getString("author_login"));
                        message.setAuthor(author);

                        Chatroom chatroom = new Chatroom();
                        chatroom.setId(rs.getLong("room_id"));
                        chatroom.setName(rs.getString("room_name"));
                        message.setChatroom(chatroom);

                        return Optional.of(message);
                    }
                }
                
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find message by id: " + id, e);
        }
    }
    
}

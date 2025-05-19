/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UsersRepositoryJdbcImpl.java                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/05/16 20:30:33 by Younes            #+#    #+#             */
/*   Updated: 2025/05/19 12:50:02 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import fr.school42.chat.models.Chatroom;
import fr.school42.chat.models.User;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    private final DataSource dataSource;
    private static final String SQL_QUERY = "WITH user_page AS (\n" + 
                "    SELECT id FROM users\n" + 
                "    ORDER BY id\n" + 
                "    LIMIT ? OFFSET ?\n" + 
                ")\n" + 
                "SELECT \n" +
                "    u.id AS user_id, u.login,\n" +
                "    -- Created rooms\n" +
                "    cr.id AS created_room_id, cr.name AS created_room_name,\n" +
                "    -- Participated rooms\n" +
                "    pr.id AS participated_room_id, pr.name AS participated_room_name\n" +
                "FROM user_page up\n" +
                "LEFT JOIN users u ON up.id = u.id\n" +
                "LEFT JOIN chatrooms cr ON cr.owner_id = u.id\n" +
                "LEFT JOIN user_chatrooms uc ON uc.user_id = u.id\n" +
                "LEFT JOIN chatrooms pr ON uc.room_id = pr.id\n" +
                "ORDER BY u.id, cr.id, pr.id";

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("Page must be >= 0");
        if (size <= 0) throw new IllegalArgumentException("Size must be > 0");
    }

    private List<User> processResultSet(ResultSet rs) throws SQLException {
        Map<Long, User> users = new LinkedHashMap<>();
        
        while (rs.next()) {
            Long userId = rs.getLong("user_id");
            User user = users.computeIfAbsent(userId, id -> {
                User newUser = new User();
                newUser.setId(id);
                try {
                    newUser.setLogin(rs.getString("login"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return newUser;
            });
                
            // Add created room if exists
            long createdRoomId = rs.getLong("created_room_id");
            if (!rs.wasNull()) {
                Chatroom createdchatroom = new Chatroom();
                createdchatroom.setId(createdRoomId);
                createdchatroom.setName(rs.getString("created_room_name"));
                user.getCreatedChatrooms().add(createdchatroom);
                   
            }
            // Add participated room if exists
            long participatedRoomId = rs.getLong("participated_room_id");
            if (!rs.wasNull()) {
                Chatroom participedRoom = new Chatroom();
                participedRoom.setId(participatedRoomId);
                participedRoom.setName(rs.getString("participated_room_name"));
                user.getParticipatingRooms().add(participedRoom);
                   
            }
        }
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findAll(int page, int size)  {
        
        validatePagination(page, size);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY)) {

                statement.setInt(1, size);
                statement.setInt(2, page * size);

                return processResultSet(statement.executeQuery());
            
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching users", e);
        }
    }
    
}

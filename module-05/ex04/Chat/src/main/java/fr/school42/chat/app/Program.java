package fr.school42.chat.app;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariDataSource;

import fr.school42.chat.models.Chatroom;
import fr.school42.chat.models.User;
import fr.school42.chat.repositories.UsersRepository;
import fr.school42.chat.repositories.UsersRepositoryJdbcImpl;

public class Program {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345";
    
    public static void main(String[] args) throws SQLException {

        try (HikariDataSource dataSource = new HikariDataSource()) {
            
            dataSource.setJdbcUrl(DB_URL);
            dataSource.setUsername(DB_USERNAME);
            dataSource.setPassword(DB_PASSWORD);

           UsersRepository usersRepository = new UsersRepositoryJdbcImpl(dataSource);

           List<User> users = usersRepository.findAll(0, 5);

           users.forEach(user -> {
            System.out.println(user.getLogin() + ":");
            System.out.println("    Created rooms: " +
                 user.getCreatedChatrooms().stream().map(Chatroom::getName).collect(Collectors.joining(", ")));
            System.out.println("    Participating rooms: " +
                 user.getParticipatingRooms().stream().map(Chatroom::getName).collect(Collectors.joining(", ")));
           });

        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }
}
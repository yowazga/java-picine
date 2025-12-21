package fr.school42.chat.app;

import java.sql.SQLException;
import java.time.LocalDateTime;
import com.zaxxer.hikari.HikariDataSource;

import fr.school42.chat.models.Chatroom;
import fr.school42.chat.models.Message;
import fr.school42.chat.models.User;
import fr.school42.chat.repositories.MessagesRepository;
import fr.school42.chat.repositories.MessagesRepositoryJdbcImpl;

public class Program {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345";
    
    public static void main(String[] args) throws SQLException {

        try (HikariDataSource dataSource = new HikariDataSource()) {
            
            dataSource.setJdbcUrl(DB_URL);
            dataSource.setUsername(DB_USERNAME);
            dataSource.setPassword(DB_PASSWORD);

            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);

            User author = new User();
            author.setId(1L);

            Chatroom room = new Chatroom();
            room.setId(4L);

            Message message = new Message();
            message.setAuthor(author);
            message.setChatroom(room);
            message.setCreatedAt(LocalDateTime.now());
            message.setText("my name is younes!");

            messagesRepository.save(message);
            System.out.println("Saved message with ID: " + message.getId());
        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }
}
package fr.school42.chat.app;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.zaxxer.hikari.HikariDataSource;

import fr.school42.chat.models.Message;
import fr.school42.chat.repositories.MessagesRepository;
import fr.school42.chat.repositories.MessagesRepositoryJdbcImpl;

public class Program {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String DB_USERNAME = "yowazga";
    private static final String DB_PASSWORD = "yowazga";
    
    public static void main(String[] args) throws SQLException {

        try (HikariDataSource dataSource = new HikariDataSource()) {
            
            dataSource.setJdbcUrl(DB_URL);
            dataSource.setUsername(DB_USERNAME);
            dataSource.setPassword(DB_PASSWORD);

            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);

            Optional<Message> optionalMessage = messagesRepository.findById(9L);

            if (optionalMessage.isPresent()) {

                Message message = optionalMessage.get();

                message.setText("Updated text in nighn");
                message.setCreatedAt(LocalDateTime.now());
                messagesRepository.update(message);

                System.out.println("Message update successfully");
            }

        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }
}
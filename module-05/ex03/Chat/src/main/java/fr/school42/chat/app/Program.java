package fr.school42.chat.app;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.zaxxer.hikari.HikariDataSource;

import fr.school42.chat.models.Message;
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

            Optional<Message> optionalMessage = messagesRepository.findById(6L);

            if (optionalMessage.isPresent()) {

                Message message = optionalMessage.get();

                message.setText("now let's check this one");
                message.setCreatedAt(null);
                messagesRepository.update(message);

                System.out.println("Message update successfully");
            }

        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }
}
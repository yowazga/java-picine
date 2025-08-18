# Module 05: Database Programming and SQL

This module focuses on database connectivity and SQL programming in Java. You'll learn how to connect to databases, execute SQL queries, manage transactions, and build database-driven applications.

## 📋 Exercises

### ex00: Basic Database Operations
- **Objective**: Establish database connections and perform basic operations
- **Concepts**: JDBC connections, basic SQL queries, result sets
- **Files**: `Chat/` directory with Maven project
- **Database**: SQLite database with basic schema

### ex01: Advanced Database Queries
- **Objective**: Implement complex SQL queries and data manipulation
- **Concepts**: JOIN operations, subqueries, data aggregation
- **Files**: `Chat/` directory with enhanced database operations
- **Database**: Extended SQLite schema with relationships

### ex02: Database Transactions
- **Objective**: Implement transaction management and data consistency
- **Concepts**: ACID properties, transaction isolation, rollback/commit
- **Files**: `Chat/` directory with transaction support
- **Database**: Transaction-aware SQLite operations

### ex03: Complex Database Operations
- **Objective**: Handle complex database scenarios and optimizations
- **Concepts**: Stored procedures, batch operations, performance tuning
- **Files**: `Chat/` directory with advanced database features
- **Database**: Optimized SQLite schema and operations

### ex04: Database Optimization
- **Objective**: Optimize database performance and query efficiency
- **Concepts**: Indexing, query optimization, connection pooling
- **Files**: `Chat/` directory with performance optimizations
- **Database**: Fully optimized SQLite implementation

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Maven for dependency management
- Understanding of SQL and database concepts
- Basic knowledge of JDBC

### Compilation and Execution
```bash
# Navigate to any exercise directory
cd ex00/Chat

# Compile and run with Maven
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.school42.Chat.Program"

# Or run tests
mvn test
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Connect to databases using JDBC
- Execute SQL queries and process results
- Manage database transactions
- Handle complex database operations
- Optimize database performance
- Build robust database applications

## 🔍 Key Concepts Covered

- **JDBC**: Java Database Connectivity, database drivers
- **SQL**: Structured Query Language, CRUD operations
- **Transactions**: ACID properties, isolation levels
- **Database Design**: Normalization, relationships, constraints
- **Performance**: Indexing, query optimization, connection management
- **Security**: SQL injection prevention, prepared statements

## 🗄️ Database Basics

### JDBC Connection
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:chat.db";
    private static final String USER = "";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
```

### Basic CRUD Operations
```java
public class UserDAO {
    
    // Create - Insert new user
    public boolean createUser(String username, String email) {
        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    // Read - Get user by ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update - Update user information
    public boolean updateUser(int id, String username, String email) {
        String sql = "UPDATE users SET username = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setInt(3, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    // Delete - Remove user
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}
```

## 🔄 Transaction Management

### Basic Transaction
```java
public class TransactionManager {
    
    public boolean transferMoney(int fromAccountId, int toAccountId, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Deduct from source account
            if (!deductAmount(conn, fromAccountId, amount)) {
                conn.rollback();
                return false;
            }
            
            // Add to destination account
            if (!addAmount(conn, toAccountId, amount)) {
                conn.rollback();
                return false;
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Transaction error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    private boolean deductAmount(Connection conn, int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.setDouble(3, amount);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    private boolean addAmount(Connection conn, int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
```

## 🔍 Advanced Queries

### Complex JOIN Operations
```java
public class ChatDAO {
    
    public List<ChatMessage> getChatHistory(int chatId, int limit) {
        String sql = """
            SELECT m.id, m.content, m.timestamp, 
                   u.username as sender_name,
                   c.name as chat_name
            FROM messages m
            JOIN users u ON m.sender_id = u.id
            JOIN chats c ON m.chat_id = c.id
            WHERE m.chat_id = ?
            ORDER BY m.timestamp DESC
            LIMIT ?
            """;
        
        List<ChatMessage> messages = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, chatId);
            pstmt.setInt(2, limit);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ChatMessage message = new ChatMessage(
                    rs.getInt("id"),
                    rs.getString("content"),
                    rs.getTimestamp("timestamp"),
                    rs.getString("sender_name"),
                    rs.getString("chat_name")
                );
                messages.add(message);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting chat history: " + e.getMessage());
        }
        
        return messages;
    }
    
    public Map<String, Integer> getUserMessageCounts() {
        String sql = """
            SELECT u.username, COUNT(m.id) as message_count
            FROM users u
            LEFT JOIN messages m ON u.id = m.sender_id
            GROUP BY u.id, u.username
            ORDER BY message_count DESC
            """;
        
        Map<String, Integer> userCounts = new LinkedHashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String username = rs.getString("username");
                int count = rs.getInt("message_count");
                userCounts.put(username, count);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user message counts: " + e.getMessage());
        }
        
        return userCounts;
    }
}
```

## 🚨 Common Pitfalls

- **Resource Leaks**: Not closing connections, statements, or result sets
- **SQL Injection**: Using string concatenation instead of prepared statements
- **Transaction Issues**: Not handling rollbacks properly
- **Connection Management**: Not using connection pooling for production
- **Exception Handling**: Catching exceptions without proper logging
- **Performance**: N+1 query problems, missing indexes

## 🔧 Best Practices

1. **Use Prepared Statements**: Prevent SQL injection and improve performance
2. **Resource Management**: Always use try-with-resources
3. **Transaction Boundaries**: Keep transactions as short as possible
4. **Connection Pooling**: Use connection pools for production applications
5. **Error Handling**: Log errors and provide meaningful error messages
6. **Indexing**: Create appropriate indexes for frequently queried columns

## 📊 Database Schema Example

```sql
-- Users table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Chats table
CREATE TABLE chats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Chat participants
CREATE TABLE chat_participants (
    chat_id INTEGER,
    user_id INTEGER,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (chat_id, user_id),
    FOREIGN KEY (chat_id) REFERENCES chats(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Messages table
CREATE TABLE messages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    chat_id INTEGER NOT NULL,
    sender_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES chats(id),
    FOREIGN KEY (sender_id) REFERENCES users(id)
);

-- Indexes for performance
CREATE INDEX idx_messages_chat_timestamp ON messages(chat_id, timestamp);
CREATE INDEX idx_messages_sender ON messages(sender_id);
CREATE INDEX idx_chat_participants_user ON chat_participants(user_id);
```

## 📖 Additional Resources

- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)
- [Database Design Tutorial](https://www.w3schools.com/sql/)
- [SQL Performance Tuning](https://use-the-index-luke.com/)

## 🎯 Project Examples

This module includes practical examples like:
- Chat application with database backend
- User management system
- Transaction processing system
- Database optimization tools

---

**Previous Module**: [Module 04: Image Processing](../module-04/README.md)  
**Next Module**: [Module 06: Testing](../module-06/README.md)

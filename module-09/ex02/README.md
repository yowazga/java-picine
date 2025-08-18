# Chat Application

A real-time chat application built with Java sockets, featuring a client-server architecture with database persistence. This project implements a multi-user chat system with room management, user authentication, and message history.

**🎓 Learning Project**: This project was built to learn Spring Framework fundamentals before moving to Spring Boot. It demonstrates manual Spring configuration, dependency injection, and JDBC integration without the auto-configuration features of Spring Boot.

## 🏗️ Architecture

The project consists of two main components:

### Socket Server (`socket-server/`)
- **Technology Stack**: Java 21, Spring Framework (Core), PostgreSQL, HikariCP
- **Features**: 
  - Multi-threaded socket server
  - User authentication and management
  - Room-based chat system
  - Message persistence
  - Database integration with PostgreSQL
  - Manual Spring configuration (no auto-configuration)
  - Spring dependency injection with `@Bean` and `@Autowired`
  - Spring JDBC with `JdbcTemplate`

### Socket Client (`socket-client/`)
- **Technology Stack**: Java 21, JSON communication
- **Features**:
  - Command-line interface
  - Real-time message reception
  - JSON-based communication protocol
  - Multi-threaded design (separate send/receive threads)

## 📁 Project Structure

```
chat/
├── socket-server/          # Server application
│   ├── src/main/java/fr/school42/sockets/
│   │   ├── app/           # Main application entry point
│   │   ├── config/        # Spring configuration
│   │   ├── models/        # Data models (User, Room, Message)
│   │   ├── repositories/  # Data access layer
│   │   ├── server/        # Socket server implementation
│   │   ├── services/      # Business logic layer
│   │   └── shared/        # Shared components
│   ├── src/main/resources/
│   │   └── db.properties  # Database configuration
│   └── pom.xml
└── socket-client/          # Client application
    ├── src/main/java/fr/school42/sockets/
    │   ├── app/           # Main application entry point
    │   ├── client/        # Socket client implementation
    │   └── models/        # Command model
    └── pom.xml
```

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL** database server
- **Git**

### Database Setup

1. **Install PostgreSQL** (if not already installed)
2. **Create a database**:
   ```sql
   CREATE DATABASE chat_db;
   ```
3. **Update database configuration** in `socket-server/src/main/resources/db.properties`:
   ```properties
   db.url=jdbc:postgresql://localhost:5432/chat_db
   db.user=your_username
   db.password=your_password
   db.driver.name=org.postgresql.Driver
   ```

### Building the Applications

#### Build Server
```bash
cd chat/socket-server
mvn clean package
```

#### Build Client
```bash
cd chat/socket-client
mvn clean package
```

### Running the Applications

#### Start the Server
```bash
# From socket-server directory
java -jar target/socket-server.jar --port=8080

# Or using Maven
mvn exec:java -Dexec.args="--port=8080"
```

#### Start the Client
```bash
# From socket-client directory
java -jar target/socket-client.jar --server-port=8080

# Or using Maven
mvn exec:java -Dexec.args="--server-port=8080"
```

## 🔧 Configuration

### Server Configuration

The server uses Spring Framework (Core) for dependency injection and configuration:

- **Spring Context**: Manual configuration with `AnnotationConfigApplicationContext`
- **Dependency Injection**: Manual bean definition with `@Bean` annotations
- **Database Access**: Spring JDBC with `JdbcTemplate` and `RowMapper`
- **Port**: Configurable via command line argument `--port`
- **Database**: PostgreSQL with HikariCP connection pooling
- **Threading**: Multi-threaded with synchronized client handling

### Client Configuration

- **Server Host**: Defaults to `localhost`
- **Server Port**: Required command line argument `--server-port`
- **Communication**: JSON-based protocol over TCP sockets

## 📡 Communication Protocol

The application uses a JSON-based communication protocol:

### Command Structure
```json
{
  "type": "command|message|response|menu|error",
  "content": "message content or command",
  "from": "client|server",
  "options": ["option1", "option2"]
}
```

### Message Types
- **command**: Client commands to server
- **message**: Chat messages between users
- **response**: Server responses to client
- **menu**: Menu options and navigation
- **error**: Error messages and notifications

## 🛠️ Development

### Key Components

#### Server Side
- **Server.java**: Main socket server with client acceptance
- **ClientHandler.java**: Individual client connection handling
- **SocketsApplicationConfig.java**: Spring configuration with manual bean definitions
- **Services**: Business logic for users, messages, and rooms
- **Repositories**: Data access layer with PostgreSQL using Spring JDBC
- **Models**: User, Room, and Message entities

#### Client Side
- **Client.java**: Socket client with dual-threaded design
- **Command.java**: JSON command serialization/deserialization

### Dependencies

#### Server Dependencies
- **Spring Framework Core** (6.1.6) - Manual configuration and DI
- **Spring JDBC** (6.1.6) - Database access with JdbcTemplate
- **HikariCP** (4.0.3) - Connection pooling
- **PostgreSQL Driver** (42.7.3)
- **JCommander** (1.82) - Command line parsing
- **Spring Security Crypto** (6.2.1) - Password hashing
- **JSON** (20231013)

#### Client Dependencies
- JCommander (1.78) - Command line parsing
- JSON (20231013)

## 🧪 Testing

Run tests for both applications:

```bash
# Server tests
cd chat/socket-server
mvn test

# Client tests
cd chat/socket-client
mvn test
```

## 📝 Usage Examples

### Starting a Chat Session

1. **Start the server**:
   ```bash
   cd chat/socket-server
   java -jar target/socket-server.jar --port=8080
   ```

2. **Connect multiple clients**:
   ```bash
   # Terminal 1
   cd chat/socket-client
   java -jar target/socket-client.jar --server-port=8080
   
   # Terminal 2
   cd chat/socket-client
   java -jar target/socket-client.jar --server-port=8080
   ```

3. **Use the chat interface**:
   - Type messages and press Enter to send
   - Receive real-time messages from other users
   - Use server commands for room management

## 🔒 Security Features

- **Password Hashing**: Spring Security Crypto for user passwords
- **Connection Pooling**: HikariCP for secure database connections
- **Input Validation**: Server-side validation of all commands
- **Error Handling**: Comprehensive error handling and logging

## 🚀 Performance Features

- **Multi-threading**: Concurrent client handling
- **Connection Pooling**: Efficient database connections
- **JSON Protocol**: Lightweight communication
- **Synchronized Collections**: Thread-safe client management

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**:
   ```bash
   # Check if port is in use
   lsof -i :8080
   # Kill process or use different port
   ```

2. **Database Connection Failed**:
   - Verify PostgreSQL is running
   - Check `db.properties` configuration
   - Ensure database exists

3. **Client Connection Failed**:
   - Verify server is running
   - Check port number matches
   - Ensure firewall allows connections

### Logs and Debugging

- Server logs connection events and errors
- Client logs communication errors
- Use `System.err.println()` for debugging

## 🎯 Learning Objectives

This project demonstrates key Spring Framework concepts:

- **Manual Configuration**: Using `@Configuration` and `@Bean` instead of component scanning
- **Dependency Injection**: Manual wiring with `@Autowired` and constructor injection
- **Spring JDBC**: Using `JdbcTemplate` and `RowMapper` for database operations
- **Application Context**: Manual context creation with `AnnotationConfigApplicationContext`
- **Property Management**: Using `@PropertySource` for external configuration

**Perfect foundation for learning Spring Boot later!**

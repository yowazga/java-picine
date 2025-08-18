# Module 09: Network Programming and Chat Applications

This module focuses on network programming in Java, specifically socket programming and building chat applications. You'll learn how to create client-server applications, handle network communication, and implement real-time messaging systems.

## 📋 Exercises

### ex00: Basic Socket Programming
- **Objective**: Learn fundamental socket programming concepts
- **Concepts**: TCP sockets, client-server communication, basic messaging
- **Files**: `chat/` directory with socket client and server
- **Features**: Simple text-based communication

### ex01: Enhanced Socket Communication
- **Objective**: Improve socket communication with better protocols
- **Concepts**: Protocol design, message formatting, error handling
- **Files**: `chat/` directory with enhanced socket implementation
- **Features**: Structured messaging and connection management

### ex02: Chat Application with Sockets
- **Objective**: Build a complete chat application
- **Concepts**: Multi-client support, message broadcasting, user management
- **Files**: `chat/` directory with full chat application
- **Features**: Real-time messaging, user authentication, room management

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Maven for dependency management
- Understanding of multithreading (Module 03)
- Basic knowledge of network concepts

### Compilation and Execution
```bash
# Navigate to any exercise directory
cd ex00/chat

# Compile and run server
cd socket-server
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.school42.chat.server.ChatServer"

# In another terminal, compile and run client
cd ../socket-client
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.school42.chat.client.ChatClient"
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Create TCP socket connections in Java
- Implement client-server architectures
- Handle network communication protocols
- Build multi-client chat applications
- Manage network resources and connections
- Implement real-time messaging systems

## 🔍 Key Concepts Covered

- **Socket Programming**: TCP/UDP socket communication
- **Client-Server Architecture**: Network application design patterns
- **Protocol Design**: Message formatting and communication protocols
- **Multi-threading**: Handling multiple client connections
- **Network I/O**: Input/output operations over network
- **Real-time Communication**: Live messaging and updates

## 🌐 Socket Programming Basics

### Basic Server Implementation
```java
import java.net.*;
import java.io.*;

public class BasicServer {
    private ServerSocket serverSocket;
    private boolean running;
    
    public BasicServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }
    
    public void start() {
        running = true;
        System.out.println("Server is listening for connections...");
        
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                // Handle client in a new thread
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
                
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client: " + e.getMessage());
                }
            }
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }
    
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    
                    // Echo the message back
                    out.println("Server: " + inputLine);
                }
                
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                closeResources();
            }
        }
        
        private void closeResources() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        BasicServer server = new BasicServer(8080);
        server.start();
    }
}
```

### Basic Client Implementation
```java
import java.net.*;
import java.io.*;

public class BasicClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Connected to server at " + host + ":" + port);
    }
    
    public void start() {
        // Start listener thread
        Thread listener = new Thread(this::listenForMessages);
        listener.start();
        
        // Start sender thread
        Thread sender = new Thread(this::sendMessages);
        sender.start();
        
        try {
            listener.join();
            sender.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
        }
    }
    
    private void sendMessages() {
        try {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }
                out.println(userInput);
            }
        } catch (IOException e) {
            System.err.println("Error reading user input: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (stdIn != null) stdIn.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        BasicClient client = new BasicClient();
        try {
            client.connect("localhost", 8080);
            client.start();
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }
}
```

## 💬 Chat Application Implementation

### Chat Server with Multi-Client Support
```java
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private ServerSocket serverSocket;
    private Map<String, ClientHandler> clients;
    private ExecutorService threadPool;
    private boolean running;
    
    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ConcurrentHashMap<>();
            threadPool = Executors.newCachedThreadPool();
            running = true;
            
            System.out.println("Chat server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not start chat server: " + e.getMessage());
        }
    }
    
    public void start() {
        System.out.println("Chat server is listening for connections...");
        
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                ClientHandler handler = new ClientHandler(clientSocket, this);
                threadPool.execute(handler);
                
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client: " + e.getMessage());
                }
            }
        }
    }
    
    public void broadcastMessage(String message, String sender) {
        String formattedMessage = String.format("[%s]: %s", sender, message);
        System.out.println(formattedMessage);
        
        for (ClientHandler client : clients.values()) {
            if (!client.getUsername().equals(sender)) {
                client.sendMessage(formattedMessage);
            }
        }
    }
    
    public void broadcastSystemMessage(String message) {
        String formattedMessage = "SYSTEM: " + message;
        System.out.println(formattedMessage);
        
        for (ClientHandler client : clients.values()) {
            client.sendMessage(formattedMessage);
        }
    }
    
    public void addClient(String username, ClientHandler handler) {
        clients.put(username, handler);
        broadcastSystemMessage(username + " joined the chat");
        sendUserList();
    }
    
    public void removeClient(String username) {
        clients.remove(username);
        broadcastSystemMessage(username + " left the chat");
        sendUserList();
    }
    
    public boolean isUsernameTaken(String username) {
        return clients.containsKey(username);
    }
    
    private void sendUserList() {
        List<String> userList = new ArrayList<>(clients.keySet());
        String userListMessage = "USERS: " + String.join(", ", userList);
        
        for (ClientHandler client : clients.values()) {
            client.sendMessage(userListMessage);
        }
    }
    
    public void stop() {
        running = false;
        threadPool.shutdown();
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        ChatServer server = new ChatServer(8080);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        
        server.start();
    }
}
```

### Client Handler for Chat Server
```java
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ChatServer server;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    
    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
    }
    
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            // Handle authentication
            if (!authenticate()) {
                return;
            }
            
            // Add client to server
            server.addClient(username, this);
            
            // Handle messages
            String message;
            while ((message = in.readLine()) != null) {
                if ("quit".equalsIgnoreCase(message)) {
                    break;
                }
                server.broadcastMessage(message, username);
            }
            
        } catch (IOException e) {
            System.err.println("Error handling client " + username + ": " + e.getMessage());
        } finally {
            cleanup();
        }
    }
    
    private boolean authenticate() throws IOException {
        out.println("Enter username:");
        username = in.readLine();
        
        if (username == null || username.trim().isEmpty()) {
            out.println("Invalid username");
            return false;
        }
        
        if (server.isUsernameTaken(username)) {
            out.println("Username already taken. Please choose another:");
            username = in.readLine();
            
            if (server.isUsernameTaken(username)) {
                out.println("Username still taken. Disconnecting.");
                return false;
            }
        }
        
        out.println("Welcome, " + username + "!");
        return true;
    }
    
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    
    public String getUsername() {
        return username;
    }
    
    private void cleanup() {
        if (username != null) {
            server.removeClient(username);
        }
        
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client resources: " + e.getMessage());
        }
    }
}
```

### Enhanced Chat Client
```java
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    private String username;
    private ExecutorService executor;
    
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        executor = Executors.newFixedThreadPool(2);
        
        System.out.println("Connected to chat server at " + host + ":" + port);
    }
    
    public void start() {
        // Start message listener
        executor.submit(this::listenForMessages);
        
        // Start message sender
        executor.submit(this::sendMessages);
        
        // Wait for completion
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("Enter username:")) {
                    handleUsernamePrompt();
                } else if (message.startsWith("Welcome, ")) {
                    username = message.substring(9, message.length() - 1);
                    System.out.println("Successfully logged in as: " + username);
                } else if (message.startsWith("USERS:")) {
                    displayUserList(message);
                } else {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
        }
    }
    
    private void handleUsernamePrompt() throws IOException {
        System.out.print("Enter username: ");
        String input = stdIn.readLine();
        out.println(input);
    }
    
    private void displayUserList(String message) {
        String userList = message.substring(7); // Remove "USERS: " prefix
        System.out.println("\n--- Online Users ---");
        System.out.println(userList);
        System.out.println("--------------------\n");
    }
    
    private void sendMessages() {
        try {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                if ("quit".equalsIgnoreCase(userInput)) {
                    out.println("quit");
                    break;
                }
                out.println(userInput);
            }
        } catch (IOException e) {
            System.err.println("Error reading user input: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (stdIn != null) stdIn.close();
            if (socket != null) socket.close();
            if (executor != null) executor.shutdownNow();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        try {
            client.connect("localhost", 8080);
            client.start();
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }
}
```

## 🔧 Protocol Design

### Message Protocol
```java
public class MessageProtocol {
    public static final String DELIMITER = "|";
    
    public enum MessageType {
        CHAT, SYSTEM, USER_JOIN, USER_LEAVE, USER_LIST, AUTH, ERROR
    }
    
    public static String createMessage(MessageType type, String sender, String content) {
        return String.join(DELIMITER, type.name(), sender, content);
    }
    
    public static Message parseMessage(String message) {
        String[] parts = message.split("\\" + DELIMITER, 3);
        if (parts.length < 3) {
            return new Message(MessageType.ERROR, "SYSTEM", "Invalid message format");
        }
        
        try {
            MessageType type = MessageType.valueOf(parts[0]);
            return new Message(type, parts[1], parts[2]);
        } catch (IllegalArgumentException e) {
            return new Message(MessageType.ERROR, "SYSTEM", "Unknown message type");
        }
    }
    
    public static class Message {
        private final MessageType type;
        private final String sender;
        private final String content;
        
        public Message(MessageType type, String sender, String content) {
            this.type = type;
            this.sender = sender;
            this.content = content;
        }
        
        // Getters
        public MessageType getType() { return type; }
        public String getSender() { return sender; }
        public String getContent() { return content; }
        
        @Override
        public String toString() {
            return createMessage(type, sender, content);
        }
    }
}
```

## 🚨 Common Network Programming Pitfalls

- **Resource Leaks**: Not closing sockets and streams properly
- **Blocking Operations**: Network I/O blocking the main thread
- **Connection Management**: Not handling connection failures gracefully
- **Protocol Design**: Inconsistent message formatting
- **Security Issues**: No input validation or authentication
- **Scalability**: Not considering multiple client connections

## 🔧 Best Practices

1. **Use Thread Pools**: Don't create a new thread for each client
2. **Handle Exceptions**: Always handle network exceptions gracefully
3. **Close Resources**: Use try-with-resources for network resources
4. **Protocol Design**: Design clear and consistent communication protocols
5. **Input Validation**: Validate all network input
6. **Connection Timeouts**: Set appropriate connection timeouts

## 📖 Additional Resources

- [Java Socket Programming Tutorial](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [Java NIO.2](https://docs.oracle.com/javase/tutorial/essential/io/file.html)
- [Network Programming](https://en.wikipedia.org/wiki/Network_programming)
- [TCP/IP Protocol](https://en.wikipedia.org/wiki/Internet_protocol_suite)

## 🎯 Project Examples

This module includes practical examples like:
- Basic echo server and client
- Multi-client chat server
- Real-time messaging application
- Network protocol implementation

---

**Previous Module**: [Module 08: Spring Framework](../module-08/README.md)  
**Final Module**: This completes the Java Picine curriculum!

# Module 06: Testing Frameworks and Data Processing

This module focuses on testing methodologies and data processing in Java. You'll learn how to write unit tests, integration tests, and process data using various testing frameworks and data handling techniques.

## 📋 Exercises

### ex00: Unit Testing Basics
- **Objective**: Learn fundamental unit testing concepts and JUnit framework
- **Concepts**: Unit tests, test cases, assertions, test organization
- **Files**: `Tests/` directory with Maven project
- **Testing Framework**: JUnit 5

### ex01: Integration Testing
- **Objective**: Implement integration tests for database and service layers
- **Concepts**: Integration testing, test databases, service testing
- **Files**: `Chat/` and `Tests/` directories with comprehensive testing
- **Data**: SQL scripts and CSV data files

### ex02: Test Data Management
- **Objective**: Manage test data and test environment setup
- **Concepts**: Test data preparation, test fixtures, data cleanup
- **Files**: `Test/` directory with data management tools
- **Resources**: CSV data files and SQL schemas

### ex03: Advanced Testing Strategies
- **Objective**: Implement advanced testing patterns and strategies
- **Concepts**: Test-driven development, mocking, performance testing
- **Files**: `Test/` directory with advanced testing implementations
- **Tools**: Mockito, performance testing utilities

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Maven for dependency management
- Understanding of database concepts (Module 05)
- Basic knowledge of testing principles

### Compilation and Execution
```bash
# Navigate to any exercise directory
cd ex00/Tests

# Compile and run tests with Maven
mvn clean compile
mvn test

# Run specific test classes
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn jacoco:report
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Write comprehensive unit tests using JUnit
- Implement integration tests for complex systems
- Manage test data and test environments
- Apply test-driven development principles
- Use mocking frameworks for isolated testing
- Measure and improve test coverage

## 🔍 Key Concepts Covered

- **Unit Testing**: Testing individual components in isolation
- **Integration Testing**: Testing component interactions
- **Test Data Management**: Preparing and managing test data
- **Test Coverage**: Measuring how much code is tested
- **Mocking**: Creating fake objects for testing
- **Test-Driven Development**: Writing tests before implementation

## 🧪 Testing Fundamentals

### Basic Unit Test Structure
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    @DisplayName("Addition should work correctly")
    void testAddition() {
        // Arrange
        int a = 5;
        int b = 3;
        
        // Act
        int result = calculator.add(a, b);
        
        // Assert
        assertEquals(8, result, "5 + 3 should equal 8");
    }
    
    @Test
    @DisplayName("Division by zero should throw exception")
    void testDivisionByZero() {
        // Arrange
        int a = 10;
        int b = 0;
        
        // Act & Assert
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(a, b);
        }, "Division by zero should throw ArithmeticException");
    }
    
    @Test
    @DisplayName("Multiple operations should work together")
    void testMultipleOperations() {
        // Arrange
        int a = 10;
        int b = 5;
        int c = 2;
        
        // Act
        int result = calculator.add(calculator.multiply(a, b), c);
        
        // Assert
        assertEquals(52, result, "10 * 5 + 2 should equal 52");
    }
}
```

### Test Organization and Naming
```java
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    
    @Nested
    @DisplayName("User Creation Tests")
    class UserCreationTests {
        
        @Test
        @DisplayName("Should create user with valid data")
        void shouldCreateUserWithValidData() {
            // Test implementation
        }
        
        @Test
        @DisplayName("Should reject user with invalid email")
        void shouldRejectUserWithInvalidEmail() {
            // Test implementation
        }
    }
    
    @Nested
    @DisplayName("User Update Tests")
    class UserUpdateTests {
        
        @Test
        @DisplayName("Should update existing user")
        void shouldUpdateExistingUser() {
            // Test implementation
        }
        
        @Test
        @DisplayName("Should fail to update non-existent user")
        void shouldFailToUpdateNonExistentUser() {
            // Test implementation
        }
    }
}
```

## 🔗 Integration Testing

### Database Integration Tests
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

@Testcontainers
public class UserRepositoryIntegrationTest {
    
    private PostgreSQLContainer<?> postgres;
    private UserRepository userRepository;
    private Connection connection;
    
    @BeforeEach
    void setUp() throws SQLException {
        postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
        postgres.start();
        
        connection = DriverManager.getConnection(
            postgres.getJdbcUrl(),
            postgres.getUsername(),
            postgres.getPassword()
        );
        
        // Setup test database schema
        setupTestDatabase();
        
        userRepository = new UserRepository(connection);
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        if (postgres != null) {
            postgres.stop();
        }
    }
    
    @Test
    @DisplayName("Should save and retrieve user from database")
    void shouldSaveAndRetrieveUser() {
        // Arrange
        User user = new User("testuser", "test@example.com");
        
        // Act
        User savedUser = userRepository.save(user);
        User retrievedUser = userRepository.findById(savedUser.getId());
        
        // Assert
        assertNotNull(retrievedUser);
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }
    
    private void setupTestDatabase() throws SQLException {
        String createTableSQL = """
            CREATE TABLE users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }
}
```

### Service Layer Integration Tests
```java
public class UserServiceIntegrationTest {
    
    private UserService userService;
    private UserRepository userRepository;
    private EmailService emailService;
    
    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        emailService = new MockEmailService(); // Mock implementation
        userService = new UserService(userRepository, emailService);
    }
    
    @Test
    @DisplayName("Should create user and send welcome email")
    void shouldCreateUserAndSendWelcomeEmail() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("newuser", "new@example.com");
        
        // Act
        User user = userService.createUser(request);
        
        // Assert
        assertNotNull(user);
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        
        // Verify email was sent
        verify(emailService).sendWelcomeEmail(user.getEmail());
    }
}
```

## 📊 Test Data Management

### CSV Data Processing
```java
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class CSVDataProcessor {
    
    public List<User> loadUsersFromCSV(String filename) throws IOException, CsvException {
        List<User> users = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            List<String[]> rows = reader.readAll();
            
            // Skip header row
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length >= 2) {
                    User user = new User(row[0], row[1]); // username, email
                    users.add(user);
                }
            }
        }
        
        return users;
    }
    
    public void saveUsersToCSV(List<User> users, String filename) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            // Write header
            writer.writeNext(new String[]{"username", "email", "created_at"});
            
            // Write data
            for (User user : users) {
                writer.writeNext(new String[]{
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreatedAt().toString()
                });
            }
        }
    }
}
```

### Test Data Builders
```java
public class UserTestDataBuilder {
    
    private String username = "testuser";
    private String email = "test@example.com";
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public UserTestDataBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public UserTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public UserTestDataBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public User build() {
        User user = new User(username, email);
        user.setCreatedAt(createdAt);
        return user;
    }
    
    public static UserTestDataBuilder aUser() {
        return new UserTestDataBuilder();
    }
    
    // Predefined test data
    public static User validUser() {
        return aUser().build();
    }
    
    public static User userWithLongUsername() {
        return aUser().withUsername("a".repeat(100)).build();
    }
    
    public static User userWithInvalidEmail() {
        return aUser().withEmail("invalid-email").build();
    }
}
```

## 🎭 Mocking and Stubbing

### Mockito Examples
```java
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class UserServiceMockTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, emailService);
    }
    
    @Test
    @DisplayName("Should handle user not found gracefully")
    void shouldHandleUserNotFoundGracefully() {
        // Arrange
        int userId = 999;
        when(userRepository.findById(userId)).thenReturn(null);
        
        // Act
        User result = userService.getUserById(userId);
        
        // Assert
        assertNull(result);
        verify(userRepository).findById(userId);
        verifyNoInteractions(emailService);
    }
    
    @Test
    @DisplayName("Should send email when user is created")
    void shouldSendEmailWhenUserIsCreated() {
        // Arrange
        User user = new User("newuser", "new@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // Act
        userService.createUser("newuser", "new@example.com");
        
        // Assert
        verify(userRepository).save(any(User.class));
        verify(emailService).sendWelcomeEmail("new@example.com");
    }
}
```

## 📈 Test Coverage and Quality

### Coverage Configuration
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Performance Testing
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {
    
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("User creation should complete within 100ms")
    void userCreationShouldCompleteWithin100ms() {
        UserService userService = new UserService();
        
        long startTime = System.currentTimeMillis();
        userService.createUser("perfuser", "perf@example.com");
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        assertTrue(duration < 100, "Operation took " + duration + "ms, expected < 100ms");
    }
}
```

## 🚨 Common Testing Pitfalls

- **Testing Implementation Details**: Testing how instead of what
- **Over-Mocking**: Creating too many mocks that make tests brittle
- **Test Data Pollution**: Tests affecting each other due to shared state
- **Slow Tests**: Tests that take too long to run
- **Poor Test Names**: Unclear test names that don't describe the scenario
- **Testing Framework Code**: Testing the testing framework instead of your code

## 🔧 Best Practices

1. **Arrange-Act-Assert**: Structure tests with clear sections
2. **Test Names**: Use descriptive names that explain the scenario
3. **One Assertion per Test**: Focus each test on one specific behavior
4. **Test Isolation**: Ensure tests don't depend on each other
5. **Meaningful Assertions**: Assert the actual behavior, not implementation details
6. **Test Data**: Use builders and factories for test data creation

## 📖 Additional Resources

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Test-Driven Development](https://en.wikipedia.org/wiki/Test-driven_development)
- [Testing Best Practices](https://martinfowler.com/articles/practical-test-pyramid.html)

## 🎯 Project Examples

This module includes practical examples like:
- Comprehensive test suites for user management
- Integration tests with databases
- Data processing and CSV handling
- Performance and load testing

---

**Previous Module**: [Module 05: Database Programming](../module-05/README.md)  
**Next Module**: [Module 07: Advanced Java Features](../module-07/README.md)

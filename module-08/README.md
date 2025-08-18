# Module 08: Spring Framework Basics

This module introduces the Spring Framework, a comprehensive framework for building enterprise Java applications. You'll learn about dependency injection, Spring configuration, and building Spring-based applications.

## 📋 Exercises

### ex00: Spring Basics and Dependency Injection
- **Objective**: Understand Spring IoC container and dependency injection
- **Concepts**: Spring context, bean definitions, dependency injection
- **Files**: `spring/` directory with Maven project
- **Configuration**: XML-based Spring configuration

### ex01: Spring Services and Configuration
- **Objective**: Implement Spring services and advanced configuration
- **Concepts**: Service layer, component scanning, bean lifecycle
- **Files**: `service/` directory with Maven project
- **Features**: Database integration with Spring

### ex02: Spring Properties and External Configuration
- **Objective**: Use external configuration files and properties
- **Concepts**: Property files, environment-specific configuration, profiles
- **Files**: `service/` directory with enhanced configuration
- **Configuration**: Properties files and environment variables

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Maven for dependency management
- Understanding of OOP concepts (Module 01)
- Basic knowledge of XML configuration

### Compilation and Execution
```bash
# Navigate to any exercise directory
cd ex00/spring

# Compile and run with Maven
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.school42.spring.Program"

# Run tests
mvn test
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Understand Spring IoC container and dependency injection
- Configure Spring applications using XML and annotations
- Implement Spring services and components
- Use external configuration files and properties
- Build Spring-based applications with database integration
- Understand Spring bean lifecycle and scopes

## 🔍 Key Concepts Covered

- **Inversion of Control (IoC)**: Dependency management by the framework
- **Dependency Injection (DI)**: Automatic dependency resolution
- **Spring Context**: Application context and bean container
- **Bean Configuration**: XML and annotation-based configuration
- **Component Scanning**: Automatic bean discovery
- **External Configuration**: Properties files and environment variables

## 🌱 Spring Framework Basics

### Spring IoC Container
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringApplication {
    
    public static void main(String[] args) {
        // Load Spring context from XML configuration
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        
        // Get bean from Spring container
        UserService userService = context.getBean("userService", UserService.class);
        
        // Use the service
        User user = userService.createUser("john", "john@example.com");
        System.out.println("Created user: " + user.getUsername());
        
        // Close context
        ((ClassPathXmlApplicationContext) context).close();
    }
}
```

### XML Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- Enable component scanning -->
    <context:component-scan base-package="fr.school42.spring"/>
    
    <!-- Database configuration -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${db.driver}"/>
        <property name="url" value="${db.url}"/>
        <property name="username" value="${db.username}"/>
        <property name="password" value="${db.password}"/>
    </bean>
    
    <!-- JdbcTemplate bean -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
    <!-- UserRepository bean -->
    <bean id="userRepository" class="fr.school42.spring.repository.UserRepository">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
    
    <!-- UserService bean -->
    <bean id="userService" class="fr.school42.spring.service.UserService">
        <property name="userRepository" ref="userRepository"/>
    </bean>
    
    <!-- Property placeholder -->
    <context:property-placeholder location="classpath:db.properties"/>
    
</beans>
```

### Annotation-Based Configuration
```java
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(String username, String email) {
        User user = new User(username, email);
        return userRepository.save(user);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

@Repository
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public User save(User user) {
        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            return ps;
        }, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        return user;
    }
    
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
}

@Component
public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}
```

## ⚙️ Configuration Management

### Properties File Configuration
```properties
# Database configuration
db.driver=org.sqlite.JDBC
db.url=jdbc:sqlite:spring.db
db.username=
db.password=

# Application configuration
app.name=Spring Demo Application
app.version=1.0.0
app.debug=true

# Logging configuration
logging.level.fr.school42=DEBUG
logging.level.org.springframework=INFO
```

### Environment-Specific Configuration
```xml
<!-- context.xml with profiles -->
<beans profile="development">
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="org.sqlite.JDBC"/>
        <property name="url" value="jdbc:sqlite:dev.db"/>
    </bean>
</beans>

<beans profile="production">
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="${db.url}"/>
        <property name="username" value="${db.username}"/>
        <property name="password" value="${db.password}"/>
    </bean>
</beans>
```

### Java Configuration
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:db.properties")
public class AppConfig {
    
    @Bean
    @Profile("development")
    public DataSource developmentDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:dev.db");
        return dataSource;
    }
    
    @Bean
    @Profile("production")
    public DataSource productionDataSource(
            @Value("${db.url}") String url,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password) {
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new UserRepository(jdbcTemplate);
    }
    
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
```

## 🔄 Bean Lifecycle

### Bean Lifecycle Methods
```java
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class DatabaseInitializer implements InitializingBean, DisposableBean {
    
    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("DatabaseInitializer: @PostConstruct called");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("DatabaseInitializer: afterPropertiesSet called");
        initializeDatabase();
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("DatabaseInitializer: @PreDestroy called");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("DatabaseInitializer: destroy called");
        closeConnections();
    }
    
    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        jdbcTemplate.execute(createTableSQL);
        System.out.println("Users table initialized");
    }
    
    private void closeConnections() {
        System.out.println("Closing database connections");
    }
}
```

## 🎯 Spring Services

### Service Layer Implementation
```java
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return user;
    }
    
    @Transactional
    public User createUser(CreateUserRequest request) {
        // Validate request
        validateCreateUserRequest(request);
        
        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }
        
        // Create user
        User user = new User(request.getUsername(), request.getEmail());
        User savedUser = userRepository.save(user);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser.getEmail());
        } catch (Exception e) {
            // Log error but don't fail user creation
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
        
        return savedUser;
    }
    
    @Transactional
    public User updateUser(Long id, UpdateUserRequest request) {
        User user = getUserById(id);
        
        // Update fields if provided
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        
        // Send goodbye email
        try {
            emailService.sendGoodbyeEmail(user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send goodbye email: " + e.getMessage());
        }
    }
    
    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
```

## 🚨 Common Spring Pitfalls

- **Circular Dependencies**: Beans depending on each other
- **Missing Bean Definitions**: Forgetting to define required beans
- **Scope Issues**: Using wrong bean scopes
- **Configuration Errors**: Incorrect XML or annotation configuration
- **Property Resolution**: Issues with property placeholders
- **Profile Activation**: Not activating required profiles

## 🔧 Best Practices

1. **Use Constructor Injection**: Prefer constructor injection over field injection
2. **Keep Beans Stateless**: Design beans to be stateless when possible
3. **Use Appropriate Scopes**: Choose the right bean scope for your use case
4. **Externalize Configuration**: Use properties files for configuration
5. **Component Scanning**: Use component scanning for automatic bean discovery
6. **Transaction Management**: Use @Transactional for database operations

## 📖 Additional Resources

- [Spring Framework Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring IoC Container](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans)
- [Spring Dependency Injection](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependencies)

## 🎯 Project Examples

This module includes practical examples like:
- Spring-based user management system
- Database integration with Spring JDBC
- Configuration management with profiles
- Service layer implementation with transactions

---

**Previous Module**: [Module 07: Advanced Java Features](../module-07/README.md)  
**Next Module**: [Module 09: Network Programming](../module-09/README.md)

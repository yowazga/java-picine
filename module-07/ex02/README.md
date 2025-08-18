# Java ORM Framework

A lightweight Object-Relational Mapping (ORM) framework built in Java that provides a simple way to map Java objects to database tables using annotations.

## 🚀 Features

- **Annotation-based mapping**: Use custom annotations to define entity relationships
- **Automatic table creation**: Tables are created automatically based on entity classes
- **CRUD operations**: Support for Create, Read, Update operations
- **SQLite support**: Currently supports SQLite database
- **Reflection-based**: Uses Java reflection for dynamic field access
- **Type mapping**: Automatic mapping of Java types to SQL types

## 📁 Project Structure

```
orm/
├── src/main/java/fr/school42/
│   ├── annotations/
│   │   ├── OrmEntity.java      # Entity annotation
│   │   ├── OrmColumn.java      # Column annotation
│   │   └── OrmColumnId.java    # ID field annotation
│   ├── core/
│   │   └── OrmManager.java     # Main ORM manager
│   ├── models/
│   │   └── User.java          # Example entity
│   └── App.java               # Main application
├── pom.xml                    # Maven configuration
└── orm.db                    # SQLite database file
```

## 🛠️ Setup

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd orm
```

2. Build the project:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn exec:java
```

## 📖 Usage

### 1. Define Your Entity

Create a Java class and annotate it with `@OrmEntity`:

```java
@OrmEntity(table = "users")
public class User {
    
    @OrmColumnId
    private Long id;
    
    @OrmColumn(name = "first_name", length = 50)
    private String firstName;
    
    @OrmColumn(name = "last_name", length = 50)
    private String lastName;
    
    @OrmColumn(name = "age")
    private Integer age;
    
    // Constructors, getters, setters...
}
```

### 2. Initialize the ORM

```java
SQLiteDataSource dataSource = new SQLiteDataSource();
dataSource.setUrl("jdbc:sqlite:orm.db");

OrmManager orm = new OrmManager(dataSource);
orm.init(User.class); // Initialize tables for User entity
```

### 3. Perform CRUD Operations

#### Create (Save)
```java
User user = new User("John", "Doe", 25);
orm.save(user);
```

#### Read (Find by ID)
```java
User foundUser = orm.findById(1L, User.class);
```

#### Update
```java
User user = orm.findById(1L, User.class);
user.setFirstName("Jane");
user.setAge(30);
orm.update(user);
```

## 🏷️ Annotations

### @OrmEntity
Marks a class as a database entity.

```java
@OrmEntity(table = "table_name")
public class YourEntity {
    // ...
}
```

**Parameters:**
- `table`: The name of the database table

### @OrmColumnId
Marks a field as the primary key (ID).

```java
@OrmColumnId
private Long id;
```

### @OrmColumn
Maps a field to a database column.

```java
@OrmColumn(name = "column_name", length = 255)
private String fieldName;
```

**Parameters:**
- `name`: The name of the database column
- `length`: The length of the column (default: 255)

## 🔧 Supported Data Types

The ORM supports the following Java types:

| Java Type | SQL Type |
|-----------|----------|
| String    | VARCHAR  |
| Integer   | INT      |
| Double    | DOUBLE   |
| Long      | BIGINT   |
| Boolean   | BOOLEAN  |

## 📝 Example

Here's a complete example showing how to use the ORM:

```java
public class App {
    public static void main(String[] args) {
        try {
            // Setup database connection
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:orm.db");
            
            // Initialize ORM
            OrmManager orm = new OrmManager(dataSource);
            orm.init(User.class);
            
            // Create and save users
            User user1 = new User("John", "Doe", 25);
            User user2 = new User("Jane", "Smith", 30);
            
            orm.save(user1);
            orm.save(user2);
            
            // Find user by ID
            User foundUser = orm.findById(1L, User.class);
            System.out.println(foundUser);
            
            // Update user
            if (foundUser != null) {
                foundUser.setFirstName("Updated");
                foundUser.setAge(26);
                orm.update(foundUser);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 🧪 Testing

Run the tests using Maven:

```bash
mvn test
```

## 📋 Requirements

This project was developed as part of the 42 School curriculum (Module 07 - Ex02). The requirements include:

- Implementing a basic ORM framework
- Using annotations for entity mapping
- Supporting basic CRUD operations
- Using reflection for dynamic field access
- Supporting SQLite database

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is part of the 42 School curriculum and follows the school's coding standards and license requirements.

## 👨‍💻 Author

**Younes** - [@Younes](https://github.com/your-username)

---

⭐ If you find this project helpful, please give it a star! 
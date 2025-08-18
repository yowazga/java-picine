# Module 01: Object-Oriented Programming

This module focuses on object-oriented programming (OOP) concepts in Java. You'll learn how to create classes, objects, and implement inheritance and polymorphism.

## 📋 Exercises

### ex00: Classes and Objects
- **Objective**: Create basic classes and instantiate objects
- **Concepts**: Class definition, constructors, object creation
- **Files**: `Program.java`, `User.java`, `Transaction.java`

### ex01: Constructors and Methods
- **Objective**: Implement constructors and instance methods
- **Concepts**: Constructor overloading, method implementation
- **Files**: `Program.java`, `User.java`, `UserIdsGenerator.java`

### ex02: Inheritance and Polymorphism
- **Objective**: Extend classes and use polymorphism
- **Concepts**: Class inheritance, method overriding, dynamic binding
- **Files**: `Program.java`, `User.java`, `UserIdsGenerator.java`

### ex03: Linked Lists and Data Structures
- **Objective**: Implement custom linked list data structure
- **Concepts**: Node-based structures, linked list operations
- **Files**: `Program.java`, `Transaction.java`, `TransactionsLinkedList.java`

### ex04: Advanced Data Structures
- **Objective**: Enhance data structures with additional functionality
- **Concepts**: Advanced operations, error handling, edge cases
- **Files**: `Program.java`, `Transaction.java`, `TransactionsLinkedList.java`

### ex05: Menu-Driven Applications
- **Objective**: Create interactive applications with user menus
- **Concepts**: User input handling, menu systems, application flow
- **Files**: `Menu.java`, `Program.java`, `Transaction.java`

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Understanding of basic Java syntax (Module 00)
- Basic understanding of OOP concepts

### Compilation and Execution
```bash
# Navigate to any exercise directory
cd ex00

# Compile all Java files
javac *.java

# Run the program
java Program
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Design and implement classes with proper encapsulation
- Create and use constructors effectively
- Implement inheritance hierarchies
- Use polymorphism in your programs
- Design and implement custom data structures
- Create interactive applications with user interfaces

## 🔍 Key Concepts Covered

- **Classes and Objects**: Blueprint vs. instance, state and behavior
- **Constructors**: Default, parameterized, and copy constructors
- **Encapsulation**: Private fields, public methods, getters/setters
- **Inheritance**: Extending classes, super keyword, method inheritance
- **Polymorphism**: Method overriding, dynamic binding, interface implementation
- **Data Structures**: Custom implementations, linked lists, collections
- **User Interaction**: Input validation, menu systems, error handling

## 🏗️ OOP Principles

### 1. Encapsulation
- Bundle data and methods that operate on that data
- Control access to data through public methods
- Hide internal implementation details

### 2. Inheritance
- Create new classes based on existing ones
- Reuse code and establish relationships
- Implement "is-a" relationships

### 3. Polymorphism
- Same interface, different implementations
- Method overriding and overloading
- Dynamic method resolution

### 4. Abstraction
- Hide complex implementation details
- Provide simple interfaces
- Focus on what rather than how

## 💡 Best Practices

1. **Single Responsibility**: Each class should have one reason to change
2. **Open/Closed**: Open for extension, closed for modification
3. **Liskov Substitution**: Subtypes should be substitutable for base types
4. **Interface Segregation**: Many specific interfaces over one general interface
5. **Dependency Inversion**: Depend on abstractions, not concretions

## 🚨 Common Pitfalls

- Creating overly complex inheritance hierarchies
- Not properly encapsulating fields
- Forgetting to call super() in constructors
- Implementing inheritance when composition would be better
- Not considering the "is-a" vs "has-a" relationship

## 🔧 Design Patterns Introduced

- **Builder Pattern**: For complex object construction
- **Singleton Pattern**: For unique instances
- **Factory Pattern**: For object creation
- **Strategy Pattern**: For interchangeable algorithms

## 📖 Additional Resources

- [Java OOP Tutorial](https://docs.oracle.com/javase/tutorial/java/concepts/)
- [Effective Java by Joshua Bloch](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997)
- [Clean Code by Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350884)

## 🎯 Project Examples

This module includes practical examples like:
- User management system
- Transaction tracking
- Custom linked list implementation
- Interactive menu system

---

**Previous Module**: [Module 00: Java Basics](../module-00/README.md)  
**Next Module**: [Module 02: File Operations](../module-02/README.md)

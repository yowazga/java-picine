# Module 07: Advanced Java Features

This module explores advanced Java features including reflection, custom annotations, and Object-Relational Mapping (ORM). You'll learn how to inspect and manipulate classes at runtime, create custom annotations, and build a simple ORM framework.

## 📋 Exercises

### ex00: Reflection API
- **Objective**: Learn Java reflection capabilities for runtime class inspection
- **Concepts**: Class inspection, method invocation, field access
- **Files**: `reflection/` directory with Maven project
- **Features**: Dynamic class loading and method execution

### ex01: Custom Annotations
- **Objective**: Create and use custom annotations for metadata
- **Concepts**: Annotation definition, annotation processing, code generation
- **Files**: `annotations/` directory with Maven project
- **Tools**: Annotation processor for code generation

### ex02: Object-Relational Mapping (ORM)
- **Objective**: Build a simple ORM framework
- **Concepts**: ORM principles, SQL generation, object mapping
- **Files**: `orm/` directory with Maven project
- **Database**: SQLite database with ORM implementation

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Maven for dependency management
- Understanding of OOP concepts (Module 01)
- Basic knowledge of databases (Module 05)

### Compilation and Execution
```bash
# Navigate to any exercise directory
cd ex00/reflection

# Compile and run with Maven
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.school42.Program"

# Run tests
mvn test
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Use reflection to inspect classes at runtime
- Create custom annotations and annotation processors
- Build a basic ORM framework
- Understand metadata-driven programming
- Implement dynamic class loading and method invocation
- Generate code using annotations

## 🔍 Key Concepts Covered

- **Reflection**: Runtime class inspection and manipulation
- **Annotations**: Custom metadata for classes, methods, and fields
- **Annotation Processing**: Code generation and compile-time processing
- **ORM**: Object-relational mapping principles and implementation
- **Dynamic Programming**: Runtime class creation and method invocation
- **Code Generation**: Automatic code creation based on metadata

## 🔮 Reflection API

### Basic Class Inspection
```java
import java.lang.reflect.*;

public class ReflectionInspector {
    
    public void inspectClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            System.out.println("Class: " + clazz.getName());
            
            // Inspect constructors
            System.out.println("\nConstructors:");
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                System.out.println("  " + constructor);
            }
            
            // Inspect fields
            System.out.println("\nFields:");
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                System.out.println("  " + field.getType().getSimpleName() + " " + field.getName());
            }
            
            // Inspect methods
            System.out.println("\nMethods:");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println("  " + method.getReturnType().getSimpleName() + " " + method.getName());
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        }
    }
    
    public Object createInstance(String className, Object... args) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // Find constructor with matching parameter types
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }
            
            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
            
        } catch (Exception e) {
            System.err.println("Error creating instance: " + e.getMessage());
            return null;
        }
    }
    
    public Object invokeMethod(Object obj, String methodName, Object... args) {
        try {
            Class<?> clazz = obj.getClass();
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }
            
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
            
        } catch (Exception e) {
            System.err.println("Error invoking method: " + e.getMessage());
            return null;
        }
    }
    
    public void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
            
        } catch (Exception e) {
            System.err.println("Error setting field: " + e.getMessage());
        }
    }
    
    public Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
            
        } catch (Exception e) {
            System.err.println("Error getting field: " + e.getMessage());
            return null;
        }
    }
}
```

### Dynamic Method Invocation
```java
public class DynamicMethodExecutor {
    
    public static Object executeMethod(Object target, String methodName, Object... args) {
        try {
            // Get the method with the specified name and parameter types
            Method method = findMethod(target.getClass(), methodName, args);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(target, args);
            }
        } catch (Exception e) {
            System.err.println("Error executing method: " + e.getMessage());
        }
        return null;
    }
    
    private static Method findMethod(Class<?> clazz, String methodName, Object... args) {
        Method[] methods = clazz.getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.getName().equals(methodName) && 
                parametersMatch(method.getParameterTypes(), args)) {
                return method;
            }
        }
        
        // Check parent class
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            return findMethod(superClass, methodName, args);
        }
        
        return null;
    }
    
    private static boolean parametersMatch(Class<?>[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) {
            return false;
        }
        
        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] != null && !paramTypes[i].isAssignableFrom(args[i].getClass())) {
                return false;
            }
        }
        
        return true;
    }
}
```

## 🏷️ Custom Annotations

### Annotation Definition
```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Entity {
    String table() default "";
    String schema() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";
    boolean nullable() default true;
    boolean unique() default false;
    int length() default 255;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    boolean autoIncrement() default true;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratedValue {
    String strategy() default "AUTO";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String description() default "";
    int timeout() default 0;
    Class<? extends Throwable>[] expected() default {};
}
```

### Annotation Processing
```java
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes({"fr.school42.annotations.Entity"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EntityProcessor extends AbstractProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            
            for (Element element : annotatedElements) {
                if (element.getKind() == ElementKind.CLASS) {
                    processEntityClass((TypeElement) element);
                }
            }
        }
        return true;
    }
    
    private void processEntityClass(TypeElement classElement) {
        Entity entityAnnotation = classElement.getAnnotation(Entity.class);
        String tableName = entityAnnotation.table().isEmpty() ? 
                          classElement.getSimpleName().toString().toLowerCase() : 
                          entityAnnotation.table();
        
        try {
            generateRepositoryClass(classElement, tableName);
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR, 
                "Failed to generate repository: " + e.getMessage()
            );
        }
    }
    
    private void generateRepositoryClass(TypeElement entityClass, String tableName) throws IOException {
        String packageName = getPackageName(entityClass);
        String className = entityClass.getSimpleName().toString();
        String repositoryName = className + "Repository";
        
        JavaFileObject sourceFile = processingEnv.getFiler()
            .createSourceFile(packageName + "." + repositoryName);
        
        try (PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
            out.println("package " + packageName + ";");
            out.println();
            out.println("import java.sql.*;");
            out.println("import java.util.*;");
            out.println();
            out.println("public class " + repositoryName + " {");
            out.println("    private static final String TABLE_NAME = \"" + tableName + "\";");
            out.println();
            out.println("    public List<" + className + "> findAll() {");
            out.println("        // Implementation for findAll");
            out.println("        return new ArrayList<>();");
            out.println("    }");
            out.println();
            out.println("    public " + className + " findById(Long id) {");
            out.println("        // Implementation for findById");
            out.println("        return null;");
            out.println("    }");
            out.println();
            out.println("    public void save(" + className + " entity) {");
            out.println("        // Implementation for save");
            out.println("    }");
            out.println();
            out.println("    public void delete(Long id) {");
            out.println("        // Implementation for delete");
            out.println("    }");
            out.println("}");
        }
    }
    
    private String getPackageName(TypeElement classElement) {
        Element enclosingElement = classElement.getEnclosingElement();
        if (enclosingElement.getKind() == ElementKind.PACKAGE) {
            return ((PackageElement) enclosingElement).getQualifiedName().toString();
        }
        return "";
    }
}
```

## 🗄️ Object-Relational Mapping (ORM)

### Basic ORM Framework
```java
public class SimpleORM {
    
    public <T> T findById(Class<T> entityClass, Long id) {
        try {
            // Get entity metadata
            EntityMetadata metadata = getEntityMetadata(entityClass);
            
            // Build SELECT query
            String sql = "SELECT * FROM " + metadata.getTableName() + " WHERE id = ?";
            
            // Execute query and map result
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToEntity(rs, entityClass, metadata);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error finding entity: " + e.getMessage());
        }
        
        return null;
    }
    
    public <T> List<T> findAll(Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        
        try {
            EntityMetadata metadata = getEntityMetadata(entityClass);
            String sql = "SELECT * FROM " + metadata.getTableName();
            
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    T entity = mapResultSetToEntity(rs, entityClass, metadata);
                    entities.add(entity);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error finding all entities: " + e.getMessage());
        }
        
        return entities;
    }
    
    public <T> void save(T entity) {
        try {
            EntityMetadata metadata = getEntityMetadata(entity.getClass());
            
            if (isNewEntity(entity, metadata)) {
                insert(entity, metadata);
            } else {
                update(entity, metadata);
            }
            
        } catch (Exception e) {
            System.err.println("Error saving entity: " + e.getMessage());
        }
    }
    
    private <T> void insert(T entity, EntityMetadata metadata) throws Exception {
        StringBuilder sql = new StringBuilder("INSERT INTO " + metadata.getTableName() + " (");
        StringBuilder values = new StringBuilder("VALUES (");
        
        List<Field> columns = metadata.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Field field = columns.get(i);
            if (i > 0) {
                sql.append(", ");
                values.append(", ");
            }
            sql.append(field.getName());
            values.append("?");
        }
        
        sql.append(") ").append(values).append(")");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < columns.size(); i++) {
                Field field = columns.get(i);
                field.setAccessible(true);
                Object value = field.get(entity);
                stmt.setObject(i + 1, value);
            }
            
            stmt.executeUpdate();
        }
    }
    
    private <T> void update(T entity, EntityMetadata metadata) throws Exception {
        StringBuilder sql = new StringBuilder("UPDATE " + metadata.getTableName() + " SET ");
        
        List<Field> columns = metadata.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) sql.append(", ");
            sql.append(columns.get(i).getName()).append(" = ?");
        }
        
        sql.append(" WHERE id = ?");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < columns.size(); i++) {
                Field field = columns.get(i);
                field.setAccessible(true);
                Object value = field.get(entity);
                stmt.setObject(i + 1, value);
            }
            
            // Set ID for WHERE clause
            Field idField = metadata.getIdField();
            idField.setAccessible(true);
            Object id = idField.get(entity);
            stmt.setObject(columns.size() + 1, id);
            
            stmt.executeUpdate();
        }
    }
    
    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityClass, EntityMetadata metadata) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        
        for (Field field : metadata.getColumns()) {
            field.setAccessible(true);
            String columnName = field.getName();
            Object value = rs.getObject(columnName);
            field.set(entity, value);
        }
        
        return entity;
    }
    
    private boolean isNewEntity(Object entity, EntityMetadata metadata) throws Exception {
        Field idField = metadata.getIdField();
        idField.setAccessible(true);
        Object id = idField.get(entity);
        return id == null || (id instanceof Number && ((Number) id).longValue() == 0);
    }
}
```

### Entity Metadata
```java
public class EntityMetadata {
    private final String tableName;
    private final List<Field> columns;
    private final Field idField;
    private final Class<?> entityClass;
    
    public EntityMetadata(Class<?> entityClass) {
        this.entityClass = entityClass;
        this.tableName = extractTableName(entityClass);
        this.columns = extractColumns(entityClass);
        this.idField = extractIdField(entityClass);
    }
    
    private String extractTableName(Class<?> entityClass) {
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.table().isEmpty()) {
            return entityAnnotation.table();
        }
        return entityClass.getSimpleName().toLowerCase();
    }
    
    private List<Field> extractColumns(Class<?> entityClass) {
        List<Field> columns = new ArrayList<>();
        
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) || 
                field.isAnnotationPresent(Id.class)) {
                columns.add(field);
            }
        }
        
        return columns;
    }
    
    private Field extractIdField(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new IllegalStateException("Entity must have an @Id field: " + entityClass.getName());
    }
    
    // Getters
    public String getTableName() { return tableName; }
    public List<Field> getColumns() { return columns; }
    public Field getIdField() { return idField; }
    public Class<?> getEntityClass() { return entityClass; }
}
```

## 🚨 Common Pitfalls

- **Performance Issues**: Reflection is slower than direct method calls
- **Security Concerns**: Reflection can bypass access controls
- **Complexity**: Reflection code can be hard to understand and maintain
- **Type Safety**: Runtime errors instead of compile-time errors
- **Annotation Processing**: Complex setup and debugging

## 🔧 Best Practices

1. **Cache Reflection Results**: Don't look up methods/fields repeatedly
2. **Use Reflection Sparingly**: Only when necessary for dynamic behavior
3. **Handle Exceptions**: Always handle reflection exceptions gracefully
4. **Document Annotations**: Clearly document custom annotation behavior
5. **Test Thoroughly**: Reflection code needs extensive testing
6. **Consider Alternatives**: Sometimes interfaces or generics are better

## 📖 Additional Resources

- [Java Reflection Tutorial](https://docs.oracle.com/javase/tutorial/reflect/)
- [Annotation Processing](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/package-summary.html)
- [ORM Principles](https://en.wikipedia.org/wiki/Object-relational_mapping)
- [Java Annotations](https://docs.oracle.com/javase/tutorial/java/annotations/)

## 🎯 Project Examples

This module includes practical examples like:
- Dynamic class loader and method executor
- Custom annotation processor for code generation
- Simple ORM framework with SQLite
- Reflection-based testing framework

---

**Previous Module**: [Module 06: Testing](../module-06/README.md)  
**Next Module**: [Module 08: Spring Framework](../module-08/README.md)

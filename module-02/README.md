# Module 02: File Operations and Command-Line Applications

This module focuses on file handling, file analysis, and building command-line applications in Java. You'll learn how to work with files, analyze their contents, and create interactive command-line tools.

## 📋 Exercises

### ex00: File Signature Analysis
- **Objective**: Analyze and identify file types based on their signatures
- **Concepts**: File I/O, binary file analysis, magic numbers
- **Files**: `FileSignatureAnalyzer.java`, `LengthComparator.java`, `Program.java`
- **Resources**: Various file types in `utiles/` directory

### ex01: Dictionary Operations
- **Objective**: Implement dictionary functionality for word lookups
- **Concepts**: File reading, string processing, search algorithms
- **Files**: `Program.java`, `dictionary.txt`, `file1`, `file2`

### ex02: Command-Line Shell Implementation
- **Objective**: Create a basic shell with common commands
- **Concepts**: Command parsing, process execution, file system operations
- **Files**: `Cd.java`, `Command.java`, `Ls.java`, `Program.java`

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Understanding of OOP concepts (Module 01)
- Basic knowledge of file systems and command-line operations

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
- Read and write files in Java
- Analyze binary file signatures and types
- Implement efficient search algorithms
- Create interactive command-line applications
- Handle file system operations programmatically
- Process text files and implement data structures

## 🔍 Key Concepts Covered

- **File I/O**: Reading and writing files, buffered operations
- **Binary File Analysis**: Magic numbers, file signatures, hex analysis
- **String Processing**: Text parsing, search algorithms, pattern matching
- **Command-Line Interface**: Argument parsing, user input handling
- **Process Management**: Executing system commands, process control
- **File System Operations**: Directory navigation, file listing, path handling

## 🛠️ File Operations

### File Reading
```java
// Reading text files
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        // Process line
    }
}

// Reading binary files
try (FileInputStream fis = new FileInputStream("file.bin")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        // Process bytes
    }
}
```

### File Writing
```java
// Writing text files
try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    writer.write("Hello, World!");
    writer.newLine();
}

// Writing binary files
try (FileOutputStream fos = new FileOutputStream("output.bin")) {
    byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
    fos.write(data);
}
```

## 🔍 File Signature Analysis

File signatures (magic numbers) are unique byte sequences at the beginning of files that identify their type:

- **PNG**: `89 50 4E 47 0D 0A 1A 0A`
- **JPEG**: `FF D8 FF`
- **GIF**: `47 49 46 38`
- **PDF**: `25 50 44 46`
- **ZIP**: `50 4B 03 04`

## 💻 Command-Line Applications

### Argument Parsing
```java
public static void main(String[] args) {
    if (args.length < 1) {
        System.out.println("Usage: java Program <command> [options]");
        return;
    }
    
    String command = args[0];
    switch (command) {
        case "ls":
            handleLs(args);
            break;
        case "cd":
            handleCd(args);
            break;
        default:
            System.out.println("Unknown command: " + command);
    }
}
```

### User Input Handling
```java
Scanner scanner = new Scanner(System.in);
System.out.print("Enter command: ");
String input = scanner.nextLine();

// Parse and execute command
executeCommand(input);
```

## 🚨 Common Pitfalls

- Not properly closing file streams (use try-with-resources)
- Forgetting to handle file not found exceptions
- Not validating file paths and user input
- Memory issues when reading large files
- Platform-specific path separators
- Not checking file permissions

## 🔧 Best Practices

1. **Resource Management**: Always use try-with-resources for file operations
2. **Error Handling**: Implement proper exception handling for file operations
3. **Input Validation**: Validate all user inputs and file paths
4. **Performance**: Use buffered I/O for better performance
5. **Security**: Be careful with file paths and user input
6. **Cross-Platform**: Use File.separator or Path API for path handling

## 📁 File System Operations

### Directory Operations
```java
// List directory contents
File dir = new File(".");
File[] files = dir.listFiles();
for (File file : files) {
    if (file.isDirectory()) {
        System.out.println("DIR: " + file.getName());
    } else {
        System.out.println("FILE: " + file.getName());
    }
}

// Create directories
File newDir = new File("newDirectory");
if (newDir.mkdir()) {
    System.out.println("Directory created successfully");
}
```

### File Information
```java
File file = new File("example.txt");
System.out.println("Name: " + file.getName());
System.out.println("Size: " + file.length() + " bytes");
System.out.println("Last modified: " + new Date(file.lastModified()));
System.out.println("Readable: " + file.canRead());
System.out.println("Writable: " + file.canWrite());
```

## 📖 Additional Resources

- [Java File I/O Tutorial](https://docs.oracle.com/javase/tutorial/essential/io/)
- [Java NIO.2 Path API](https://docs.oracle.com/javase/tutorial/essential/io/path.html)
- [File Magic Numbers](https://en.wikipedia.org/wiki/List_of_file_signatures)
- [Command Line Interface Guidelines](https://clig.dev/)

## 🎯 Project Examples

This module includes practical examples like:
- File type identification tool
- Dictionary search application
- Basic command-line shell
- File system browser

---

**Previous Module**: [Module 01: Object-Oriented Programming](../module-01/README.md)  
**Next Module**: [Module 03: Concurrency](../module-03/README.md)

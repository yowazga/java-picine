# Module 03: Multithreading and Concurrency

This module introduces multithreading and concurrency in Java. You'll learn how to create and manage threads, handle synchronization, and build concurrent applications that can perform multiple tasks simultaneously.

## 📋 Exercises

### ex00: Basic Threading with Hen and Egg
- **Objective**: Understand basic thread creation and execution
- **Concepts**: Thread class, Runnable interface, thread lifecycle
- **Files**: `Egg.java`, `Hen.java`, `Program.java`

### ex01: Thread Synchronization
- **Objective**: Learn thread synchronization and coordination
- **Concepts**: Locks, synchronized blocks, thread communication
- **Files**: `Egg.java`, `Hen.java`, `LockObject.java`, `Program.java`

### ex02: Thread Management
- **Objective**: Manage multiple threads and their execution
- **Concepts**: Thread pools, thread lifecycle management, coordination
- **Files**: `Program.java`, `SumThread.java`

### ex03: File Downloading with Threads
- **Objective**: Implement concurrent file downloading
- **Concepts**: Concurrent I/O operations, thread coordination, resource management
- **Files**: `DownloadTask.java`, `FileDownloader.java`, `Program.java`

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Understanding of OOP concepts (Module 01)
- Basic knowledge of file operations (Module 02)

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
- Create and manage threads in Java
- Understand thread lifecycle and states
- Implement thread synchronization mechanisms
- Handle race conditions and deadlocks
- Build concurrent applications
- Manage shared resources safely

## 🔍 Key Concepts Covered

- **Threads**: Lightweight processes, thread creation and management
- **Concurrency**: Multiple tasks executing simultaneously
- **Synchronization**: Coordinating access to shared resources
- **Race Conditions**: Unpredictable behavior due to timing
- **Deadlocks**: Threads waiting for each other indefinitely
- **Thread Safety**: Ensuring correct behavior in concurrent environments

## 🧵 Thread Basics

### Creating Threads
```java
// Method 1: Extending Thread class
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running: " + Thread.currentThread().getName());
    }
}

// Method 2: Implementing Runnable interface
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable is running: " + Thread.currentThread().getName());
    }
}

// Usage
MyThread thread1 = new MyThread();
thread1.start();

Thread thread2 = new Thread(new MyRunnable());
thread2.start();
```

### Thread States
```java
Thread thread = new Thread(() -> {
    // Thread work
});

// NEW - Thread created but not started
System.out.println(thread.getState()); // NEW

thread.start();
// RUNNABLE - Thread is executing or ready to execute
System.out.println(thread.getState()); // RUNNABLE

// Other states: BLOCKED, WAITING, TIMED_WAITING, TERMINATED
```

## 🔒 Synchronization

### Synchronized Methods
```java
public class Counter {
    private int count = 0;
    
    // Synchronized method - only one thread can execute at a time
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

### Synchronized Blocks
```java
public class BankAccount {
    private double balance;
    private final Object lock = new Object();
    
    public void deposit(double amount) {
        synchronized (lock) {
            balance += amount;
            System.out.println("Deposited: " + amount + ", New balance: " + balance);
        }
    }
    
    public void withdraw(double amount) {
        synchronized (lock) {
            if (balance >= amount) {
                balance -= amount;
                System.out.println("Withdrawn: " + amount + ", New balance: " + balance);
            } else {
                System.out.println("Insufficient funds");
            }
        }
    }
}
```

### Locks and Conditions
```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class BoundedBuffer {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    private final Object[] items = new Object[100];
    private int putptr, takeptr, count;
    
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
```

## 🚨 Common Concurrency Issues

### Race Conditions
```java
// Problematic code - race condition
public class UnsafeCounter {
    private int count = 0;
    
    public void increment() {
        count++; // This is not atomic!
    }
    
    public int getCount() {
        return count;
    }
}

// Solution - synchronized method
public class SafeCounter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

### Deadlocks
```java
// Example of potential deadlock
public class DeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {
            System.out.println("Method 1: Got lock1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            
            synchronized (lock2) {
                System.out.println("Method 1: Got lock2");
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {
            System.out.println("Method 2: Got lock2");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            
            synchronized (lock1) {
                System.out.println("Method 2: Got lock1");
            }
        }
    }
}
```

## 🔧 Best Practices

1. **Prefer Runnable over Thread**: More flexible and doesn't limit inheritance
2. **Use Thread Pools**: Don't create new threads for every task
3. **Minimize Synchronization**: Only synchronize when necessary
4. **Use Volatile for Simple Flags**: When you only need visibility, not atomicity
5. **Prefer Concurrent Collections**: Use `ConcurrentHashMap`, `CopyOnWriteArrayList`, etc.
6. **Avoid Thread.stop()**: Use interruption instead

## 🚀 Thread Pools

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExample {
    public static void main(String[] args) {
        // Create a thread pool with 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Submit tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + 
                                 Thread.currentThread().getName());
            });
        }
        
        // Shutdown the executor
        executor.shutdown();
    }
}
```

## 📖 Additional Resources

- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java Concurrency in Practice](https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0137940722)
- [Java Thread Documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Thread.html)
- [Concurrent Programming](https://en.wikipedia.org/wiki/Concurrent_computing)

## 🎯 Project Examples

This module includes practical examples like:
- Producer-consumer pattern with threads
- Concurrent file downloading
- Thread-safe counters and collections
- Synchronized resource management

---

**Previous Module**: [Module 02: File Operations](../module-02/README.md)  
**Next Module**: [Module 04: Image Processing](../module-04/README.md)

# Food Delivery Service

**A comprehensive Java-based food ordering system demonstrating enterprise-level development practices**

## Introduction

This Food Delivery Service is a **comprehensive Java project designed to help practice key areas in Java development**. The project showcases modern software engineering principles and implements a complete food ordering workflow from authentication to order processing.

### 🎯 Key Learning Areas Covered:

- **Object-Oriented Programming** - Implementing SOLID principles, inheritance, polymorphism, and design patterns
- **Build Tools (Maven)** - Multi-module project structure with dependency management
- **Clean Code** - Following best practices for readable, maintainable code
- **Unit Testing** - Comprehensive test coverage with JUnit
- **Spring Boot** - Auto-configuration and starter dependencies
- **Spring AOP** - Aspect-oriented programming for cross-cutting concerns
- **Java Persistence** - File-based data storage and CSV processing

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-red?style=for-the-badge&logo=apache-maven&logoColor=white)
![Spring AOP](https://img.shields.io/badge/Spring%20AOP-Aspect%20Oriented-green?style=for-the-badge&logo=spring&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-Testing-blue?style=for-the-badge&logo=junit5&logoColor=white)

## 📋 Project Structure

```
Food-Delivery-Service/
├── application/           # Main application module
│   ├── src/main/java/
│   │   ├── Application.java
│   │   ├── SpringApplicationStarter.java
│   │   ├── LoggingAspect.java
│   │   ├── view/          # User interface layer
│   │   └── values/        # Value objects
│   └── src/test/java/     # Application tests
├── service/               # Business logic module
│   ├── src/main/java/
│   │   ├── service/       # Core business services
│   │   └── aspect/        # AOP annotations
│   └── src/test/java/     # Service tests
├── persistence/           # Data access module
│   └── src/main/java/
│       ├── data/          # Data store implementations
│       └── domain/        # Domain models
└── pom.xml               # Parent Maven configuration
```

## 🚀 Installation & Setup

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Git**

### Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/Food-Delivery-Service.git
   cd Food-Delivery-Service
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run -pl application
   ```

4. **Follow the CLI prompts:**
   - Enter customer credentials (username: `Smith`, password: `SmithSecret`)
   - Browse available foods
   - Add items to cart
   - Complete your order

### Sample Data Files

The application requires CSV files in the project root:

- `foods.csv` - Available food items
- `customers.csv` - Customer credentials and balances
- `orders.csv` - Order history (generated automatically when order is made) 

## 👨‍💻 Developer Instructions

### Module Overview

#### 🏗️ Application Module
- **Entry point**: Contains main application class and Spring Boot configuration
- **View layer**: CLI-based user interface implementation
- **AOP integration**: Logging aspects for method execution tracking

#### ⚙️ Service Module
- **Business logic**: Core food delivery service implementation
- **Exception handling**: Custom exceptions for business rules
- **AOP annotations**: Custom annotations for method-level concerns

#### 💾 Persistence Module
- **Domain models**: Core business entities (Food, Customer, Order, Cart)
- **Data access**: File-based data store implementations
- **CSV processing**: Readers and writers for data persistence

### 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl service

# Generate test coverage report
mvn jacoco:report
```

### 🔧 Key Design Patterns

#### 1. **Dependency Inversion Principle**
```java
// Service depends on abstraction, not concrete implementation
public class DefaultFoodDeliveryService implements FoodDeliveryService {
    private final DataStore dataStore; // Interface, not FileDataStore
}
```

#### 2. **Template Method Pattern**
```java
// Abstract Reader with template method
public abstract class Reader<T> {
    protected abstract T parseLine(String line);
    // Common reading logic implemented in base class
}
```

#### 3. **Strategy Pattern**
- Different reader implementations for various data types
- Pluggable data store implementations

### 🏛️ Architecture Highlights

#### **Layered Architecture**
- **Presentation Layer**: CLI View
- **Business Layer**: Service implementations
- **Data Layer**: File-based persistence

#### **Separation of Concerns**
- Views cannot directly access services
- Services are decoupled from data storage details
- Domain models contain minimal business logic

#### **Spring Integration**
- **Dependency Injection**: Automatic bean wiring
- **AOP**: Method-level logging and monitoring
- **Configuration**: Java-based Spring configuration

### 🔄 Application Flow

1. **Initialization**: Load food and customer data from CSV files
2. **Authentication**: Validate customer credentials
3. **Food Browsing**: Display available food items
4. **Cart Management**: Add/remove items with balance validation
5. **Order Processing**: Convert cart to order and update customer balance
6. **Persistence**: Save order details to CSV file

### 🎯 Key Features

- **Balance Validation**: Prevents orders exceeding customer balance
- **Cart Management**: Add, update, and remove items
- **Order Tracking**: Complete order history with timestamps
- **Error Handling**: Graceful handling of authentication and balance issues
- **Logging**: Comprehensive AOP-based method logging

### 🧩 Extending the Application

#### Adding New Food Types
1. Update `foods.csv` with new entries
2. No code changes required - uses dynamic loading

#### Adding New Data Sources
1. Implement the `DataStore` interface
2. Create new Reader implementations
3. Update Spring configuration

#### Adding New Business Rules
1. Extend service layer with new validation logic
2. Add corresponding exception types
3. Update view layer for new error messages

---

**Happy Coding! 🍕** This project demonstrates enterprise Java development best practices while maintaining clean, testable, and maintainable code structure.

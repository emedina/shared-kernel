# Architecture Kernel for Hexagonal Architecture

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Java Version](https://img.shields.io/badge/Java-25-blue)
![Test Coverage](https://img.shields.io/badge/coverage-90%25-brightgreen)

A comprehensive architecture kernel library implementing Domain-Driven Design (DDD) patterns and hexagonal architecture principles for Java applications.

## 📚 Further Learning

This implementation is part of a comprehensive exploration of Hexagonal Architecture patterns. The concepts are covered in depth in:

**English Version**
*Decoupling by Design: A Pragmatic Approach to Hexagonal Architecture*

- [PDF](https://leanpub.com/decouplingbydesignapractitionersguidetohexagonalarchitecture)  
- [Kindle](https://a.co/d/4KwauyK)  
- [Paperback](https://a.co/d/cGQI8gX)  

**Versión en Español**  
*Desacoplamiento por Diseño: Una Guía Práctica para la Arquitectura Hexagonal*

- [PDF](https://leanpub.com/desacoplamientopordiseounaguaprcticaparalaarquitecturahexagonal)  
- [Kindle](https://amzn.eu/d/ic50CoH)  
- [Tapa blanda](https://amzn.eu/d/1fHOpN6)  

The book provides in-depth coverage of:

- Architecture kernel implementation strategies
- Command and Query pattern variations
- Domain modeling with value objects and entities
- Specification pattern for business rules
- Transaction management in hexagonal systems
- Evolutionary architecture approaches

## 🚀 Recent Upgrades (v1.0.0)

### ✅ JDK 24 & Latest Dependencies

- **Java**: Upgraded from JDK 21 → **JDK 25** (GA release)
- **Maven Compiler Plugin**: 3.12.1 → **3.14.1**
- **Versions Maven Plugin**: 2.17.1 → **2.20.1**
- **JUnit Jupiter**: RELEASE → **6.0.2** (fixed anti-pattern)
- **SLF4J API**: **2.0.17** (latest GA)
- **Vavr**: **0.11.0** (latest stable)

### 🏗️ Improved Dependency Management

- **Centralized Properties**: All versions managed in root POM
- **Provided Scope Strategy**: SLF4J and Vavr use `provided` scope to avoid version conflicts
- **Consumer Flexibility**: Projects using this library can choose their own logging and functional programming library versions

### 🧪 Comprehensive Testing

- **90%+ Test Coverage**: Added extensive unit tests for all non-interface/annotation classes
- **Modern Testing**: JUnit 5, AssertJ, parameterized tests
- **Quality Assurance**: Timer precision tests, specification logic validation, decorator pattern verification

## 📦 Modules

### Core Domain (`domain`)

Base classes and annotations for Domain-Driven Design:

- **Entities & Value Objects**: Type-safe domain modeling
- **Aggregate Roots**: DDD aggregate pattern implementation  
- **Repositories**: Domain repository contracts
- **Domain Services**: Business logic encapsulation
- **Identities**: Strongly-typed identifiers

### Command Pattern (`command`, `command-bus`, `command-either-bus`)

CQRS command handling with decorator support:

- **Command Interface**: Base command contract
- **Command Bus**: Decoupled command execution
- **Either Bus**: Functional error handling with Vavr
- **Logging Decorator**: Execution timing and logging
- **Extensible**: Easy to add custom decorators

### Query Pattern (`query`, `query-bus`, `query-either-bus`)

CQRS query handling with decorator support:

- **Query Interface**: Base query contract  
- **Query Bus**: Decoupled query execution
- **Either Bus**: Functional error handling with Vavr
- **Logging Decorator**: Execution timing and logging
- **Extensible**: Easy to add custom decorators

### Specifications (`specifications`)

Flexible business rule composition:

- **Specification Pattern**: Encapsulated business rules
- **Fluent API**: Chainable logical operations (`and`, `or`, `not`)
- **Composite Operations**: Complex rule combinations
- **Type Safety**: Generic type support

### Transactional (`transactional`)

Technology-agnostic transaction boundaries:

- **@Transactional**: Framework-independent annotation
- **Isolation Levels**: Configurable isolation
- **Propagation**: Transaction propagation control

## 🛠️ Usage

### Maven Dependency

```xml
<dependency>
    <groupId>io.github.emedina</groupId>
    <artifactId>architecture-kernel-domain</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Required Dependencies (Provided Scope)

When using modules with provided dependencies, add these to your project:

```xml
<!-- For command-bus, query-bus, command-either-bus, query-either-bus -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.17</version>
</dependency>

<!-- For command-either-bus only -->
<dependency>
    <groupId>io.vavr</groupId>
    <artifactId>vavr</artifactId>
    <version>0.11.0</version>
</dependency>

<!-- Logging implementation (choose one) -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

### Example: Value Object

```java
@ValueObject
public class Email implements ValueObject<Email> {
    private final String value;
    
    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }
    
    @Override
    public boolean sameValueAs(Email other) {
        return Objects.equals(this.value, other.value);
    }
    
    private boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}
```

### Example: Command with Bus

```java
// Command
public class CreateUserCommand implements Command {
    private final String email;
    private final String name;
    
    // constructors, getters...
}

// Handler
@Component
public class CreateUserHandler implements CommandHandler<CreateUserCommand> {
    @Override
    public void handle(CreateUserCommand command) {
        // Implementation
    }
}

// Usage
@Service
public class UserService {
    private final CommandBus commandBus;
    
    public void createUser(String email, String name) {
        commandBus.execute(new CreateUserCommand(email, name));
    }
}
```

### Example: Specifications

```java
// Business rules
Specification<User> activeUser = user -> user.isActive();
Specification<User> premiumUser = user -> user.isPremium();
Specification<User> recentLogin = user -> user.getLastLogin().isAfter(LocalDate.now().minusDays(30));

// Compose complex rules
Specification<User> eligibleForOffer = activeUser
    .and(premiumUser.or(recentLogin))
    .andNot(user -> user.hasActiveOffer());

// Use in domain logic
if (eligibleForOffer.isSatisfiedBy(user)) {
    // Send offer
}
```

## 🏛️ Architecture Principles

### Hexagonal Architecture

- **Ports & Adapters**: Clear separation of concerns
- **Domain Isolation**: Business logic independent of frameworks
- **Dependency Inversion**: Dependencies point inward

### Domain-Driven Design

- **Ubiquitous Language**: Shared vocabulary between domain experts and developers
- **Bounded Contexts**: Clear module boundaries
- **Strategic Design**: Aggregate, Entity, Value Object patterns

### CQRS (Command Query Responsibility Segregation)

- **Command Side**: State-changing operations
- **Query Side**: Data retrieval operations  
- **Separation**: Different models for reads and writes

## 🔧 Development

### Requirements

- **JDK 25** or higher
- **Maven 3.9+**

### Building

```bash
mvn clean compile
```

### Testing

```bash
mvn test
```

### Dependency Updates

```bash
mvn versions:display-dependency-updates
mvn versions:display-plugin-updates
```

## 📋 Version Compatibility

| Architecture Kernel | Min JDK | SLF4J API | Vavr | Notes |
|---------------|---------|-----------|------|-------|
| 1.0.x         | 25      | 2.0.0+    | 0.11.0+ | Current |

## 🤝 Contributing

1. **Code Style**: Follow existing patterns and conventions
2. **Testing**: Maintain 90%+ test coverage
3. **Documentation**: Update README for significant changes
4. **Dependencies**: Use only GA releases, avoid alpha/beta/snapshots

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🏷️ Tags

`hexagonal-architecture` `domain-driven-design` `ddd` `cqrs` `java` `maven` `specifications` `command-pattern` `value-objects` `architecture-kernel`

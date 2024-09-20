# shared-kernel
Shared concepts, patterns, objects when implementing Hexagonal Architectures (but also others).

This repository contains a Java module for the implementation of the Shared Kernel, the Specification 
and the Validation Result patterns using Domain-Driven Design (DDD) principles.

The Shared Kernel pattern is a way to organize the codebase where the core of the system, also known as the domain model, 
is separated from the infrastructure and other technical concerns. The domain model contains the business logic 
and state of the application, and it is shared across multiple bounded contexts.

The Specification pattern is a way to encapsulate the business rules and constraints of the domain model into reusable and composable objects called Specifications. These specifications are used to filter and validate the entities in the domain model.

DDD follows the principles of a rich domain model, where the core domain concepts and their relationships are explicitly modeled and the behavior is encapsulated in the entities and value objects. This allows for a clear understanding of the business domain and the ability to evolve the model as the requirements change.

The module is organized in the following package structure:
- `architecture` package contains architectural rules to validate the integrity of concepts
- `command` package contains base classes for the Command pattern
- `command-bus` package contains an implementation of a Command Bus using Spring's registry (application context) for dynamic registration of handlers
- `domain` package contains base classes for entities, value objects, aggregates, etc.
- `specifications` package contains the implementation of the Specification pattern (see [Specifications](https://martinfowler.com/apsupp/spec.pdf "Specifications") by Eric Evans & Martin Fowler)
- `transactional` package contains a base annotation for declarative transaction management that allows pluggable implementations (such as Spring Transactional Management)

This structure allows for a clear separation of concerns and a high degree of testability and maintainability.

The module also follows best practices for Java development such as SOLID principles, dependency injection, and clean code.

Please note that this module is meant to be extended by applications adopting Hexagonal Architectures with DDD and used in conjunction with other modules 
that implement the infrastructure and other technical concerns such as persistence, security, and so on.

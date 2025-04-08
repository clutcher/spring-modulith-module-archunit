# Spring Modulith Module ArchUnit

A utility library for enforcing architectural standards within Spring Modulith modules using ArchUnit, with a focus on hexagonal architecture.

---

## Table of Contents

1. [Overview](#overview)
2. [Project Structure](#project-structure)
3. [Getting Started](#getting-started)
    - [Installation](#installation)
    - [Basic Usage](#basic-usage)
4. [Default Rules for Hexagonal Architecture](#default-rules-for-hexagonal-architecture)
    - [Package Structure](#package-structure)
    - [Layers Structure](#layers-structure)
    - [Layer Dependency Rules](#layer-dependency-rules)
    - [Additional Development Standards](#additional-development-standards)
    - [Configuring Default Rules](#configuring-default-rules)
5. [Implementation Details](#implementation-details)

---

## Overview

Spring Modulith is a powerful framework for managing module dependencies and provides integration with jMolecules to enforce modular boundaries. However, there are cases, when additional rules and validations are required to maintain internal structure and compliance with architectural standards, such as the hexagonal architecture.

The **Spring Modulith Module ArchUnit** library is purpose-built to address this gap by offering:

- A set of predefined ArchUnit rules for hexagonal architecture.
- Flexible customization for hexagonal or other architectures.
- Seamless integration with Spring Modulith's module verification capabilities.

---

## Project Structure

The project consists of two libraries:

1. **spring-modulith-module-archunit**  
   Provides implementations of ArchUnit rules and support for Spring Modulith module verification.
2. **spring-modulith-module-archunit-starter**  
   A Spring Boot starter that pre-configures ArchUnit rules and exposes services for architecture validation in tests.

---

## Getting Started

### Installation

Add the following dependencies to your project. These libraries require **Java 17+** and are compatible with Spring Boot 3+.

**Using Gradle:**

``` gradle
dependencies {
    testImplementation("dev.clutcher:spring-modulith-module-archunit:1.0.0")
    testImplementation("dev.clutcher:spring-modulith-module-archunit-starter:1.0.0")
}
```

**Using Maven:**

```xml

<dependencies>
    <dependency>
        <groupId>dev.clutcher</groupId>
        <artifactId>spring-modulith-module-archunit</artifactId>
        <version>1.0.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>dev.clutcher</groupId>
        <artifactId>spring-modulith-module-archunit-starter</artifactId>
        <version>1.0.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Basic Usage

#### Verifying Modules with Spring Boot Test

Set up a test class to verify module architecture using Spring Modulith's module verification tools and Spring Modulith Module ArchUnit.

``` java
import dev.clutcher.modulith.archunit.verifier.app.api.ApiForModuleArchitectureVerification;  
import org.junit.jupiter.api.Test;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.boot.test.context.SpringBootTest;  
import org.springframework.modulith.core.ApplicationModules;  
  
@SpringBootTest  
class ApplicationModulesTests {  
  
    @Autowired  
    private ApiForModuleArchitectureVerification moduleArchitectureVerification;  
  
    @Test  
    void verifyModules() {  
        ApplicationModules applicationModules = ApplicationModules.of(Application.class);  
        applicationModules.forEach(System.out::println);  
  
        applicationModules.verify();  
        moduleArchitectureVerification.verifyAllModules(applicationModules);  
    }  
}
```

#### Creating Custom Architecture Rules

Configuration can be done programmatically to define custom validation rules for your project's unique requirements using the `ApiForCustomizingArchRuleCreation`:

``` java
@Configuration
public class ModuleArchitectureRulesConfiguration {

    @Bean
    public ApiForArchRuleCreation customArchRuleCreation() {
        return ApiForCustomizingArchRuleCreation
                .forApplicabilityChecker((m, allClassesRelatedToModule) -> true)
                .withLayerRule((m) -> null)
                .create();
    }
    
    
    @Bean
    public ApiForArchRuleCreation customArchRuleCreation() {
        HexagonalArchRuleCreationService defaultHexagonalRuleCreationService = new HexagonalArchRuleCreationService(
                new HexagonalPackageSettingsUsingSpringProperties()
        );

        return ApiForCustomizingArchRuleCreation
                .forExistingArchRuleCreation(defaultHexagonalRuleCreationService)
                .withPackageStructureRule((m) -> null)
                .create();
    }
}
```

---

## Default rules for Hexagonal architecture

This library enforces opinionated rules for hexagonal architecture, which can be either adopted as is or customized. Exact rules can be found in `HexagonalArchitectureRulesLibrary` and `HexagonalArchRuleCreationService`.

### Package Structure

```
some-module/
├─ app/
│  ├─ api/                              # Driving ports (primary ports)
│  │  └─ ApiForDoingSomething.java
│  ├─ domain/
│  │  ├─ services/                      # Application services
│  │  │  └─ DoingSomethingService.java
│  │  └─ model/                         # Domain model
│  │     └─ Object.java
│  └─ spi/                              # Driven ports (secondary ports)
│     └─ SomeRepository.java
├─ in/                                  # Driving adapters (primary adapters)
│  └─ rest/                            
│     └─ SomeRestController.java  
└─ out/                                 # Driven adapters (secondary adapters)
   └─ db/                              
      └─ SomeRepositoryUsingMysql.java
another-module/
├─ app/
│  ├─ api/                             
│  │  └─ ApiForDoingSomethingElse.java
```

To conform with hexagonal architecture package structure most ensure that **Adapters** are placed outside application root, while **Ports** are inside application root.

### Layers Structure

1. **Driving Ports (`app.api`)**  
   Defines interfaces for application intent. Example: `ApiForDoingSomething`.
2. **Driven Ports (`app.spi`)**  
   Defines interfaces for external dependencies, such as repositories or external services.
3. **Driving Adapters (`in`)**  
   Entry points for the application, e.g., REST controllers or event listeners.
4. **Driven Adapters (`out`)**  
   Implementations that interact with external systems, such as databases or messaging services.
5. **Application Services (`app.domain.services`)**  
   Implements the business logic of the module and interacts with ports.
6. **Application Configuration (`config`)**  
   Spring beans and module configurations.

### Layer Dependency Rules

The library enforces strict layer dependency rules to ensure clean separation of concerns and adherence to hexagonal architecture.

- **Layer Rules**:
    - **Driving Ports (`app.api`)** can be accessed only by:
        - Driving Adapters (`in`),
        - Application Services (`app.domain.services`),
        - Application Configuration (`config`).
    - **Driven Ports (`app.spi`)** can be accessed only by:
        - Driven Adapters (`out`),
        - Application Services (`app.domain.services`),
        - Application Configuration (`config`).
    - **Application Services (`app.domain.services`)** can be accessed only by:
        - Driving Ports (`app.api`),
        - Driven Ports (`app.spi`),
        - Application Configuration (`config`).
    - **Driving Adapters (`in`)** can be accessed only by:
        - Application Configuration (`config`).
    - **Driven Adapters (`out`)** can be accessed only by:
        - Application Configuration (`config`).
- Dependencies originating outside the module boundaries are ignored by default, ensuring only internal dependencies are considered during validation.
    - Spring Modulith verify checks will ensure that module boundaries are not violated.
    - To use **Driving Ports** in other submodules define it as Named Interface with `package-info.java`

### Additional Development Standards

- Driving Ports (API)
    - Must be interfaces
    - Names must start with `ApiFor`
    - `package-info` class is ignored
- Driven Ports (SPI)
    - Must be interfaces
- Application Services
    - Must be concrete classes (not interfaces)
    - Implements a Driving Port interface
    - Names must end with either:
        - `Service`
        - `ServiceBuilder`
        - `ServiceFactory`
- Driven Adapters (OUT)
    - Implements a Driven Port interface
    - Names must contain the word `Using` (e.g., `RepositoryUsingJPA`)

### Configuring Default Rules

You can configure the architecture rules through Spring properties:

```properties
dev.clutcher.modulith.archunit.rules.hexagonal.package.port.driving              = .api..
dev.clutcher.modulith.archunit.rules.hexagonal.package.port.driven               = .spi..
dev.clutcher.modulith.archunit.rules.hexagonal.package.adapter.driving           = .in..
dev.clutcher.modulith.archunit.rules.hexagonal.package.adapter.driven            = .out..
dev.clutcher.modulith.archunit.rules.hexagonal.package.application.root          = .app
dev.clutcher.modulith.archunit.rules.hexagonal.package.application.services      = .domain.services..
dev.clutcher.modulith.archunit.rules.hexagonal.package.application.configuration = .config..
```

Value of `application.root` is automatically added to `port.driving`, `port.driven`, `application.services` to ensure that package structure after properties changes does not violate hexagonal architecture.


---

## Implementation Details

### spring-modulith-module-archunit

1. **Architecture Rule Creation**:
    - `ApiForArchRuleCreation` - Main entry point for creating ArchUnit rules
    - `ApiForCustomizingArchRuleCreation` - Main entry point for customizing existing instances of `ApiForArchRuleCreation` using `DelegatingArchRuleCreationService`
    - `HexagonalArchRuleCreationService` - Service for creating hexagonal architecture rule sets
    - `DelegatingArchRuleCreationService` - Service implementation to be used for custom rules creation
    - `HexagonalArchitectureRulesLibrary` - Library of predefined rules for hexagonal architecture
2. **Architecture Rule Verification**:
    - `ApiForModuleArchitectureVerification` - Main entry point to verify module architecture
    - `ModuleArchitectureVerificationService` - Service implementation for verifying module architecture

### spring-modulith-module-archunit-starter

`VerificationStrategyAutoconfiguration` creates beans:

1. `HexagonalPackageSettingsUsingSpringProperties`
    - Bean name: `hexagonalArchitectureVerificationProperties`
    - Purpose: Holds hexagonal architecture configuration settings
    - Dependencies: none
2. `HexagonalArchRuleCreationService`
    - Bean name: `hexagonalArchRuleCreationService`
    - Purpose: Creates service to provide set of rules for hexagonal architecture
    - Dependencies: `HexagonalArchitectureSettings`
3. `ModuleArchitectureVerificationService`
    - Bean name: `applicationModulesArchitectureVerifier`
    - Purpose: Verifies module architecture against defined rules
    - Dependencies: `List<ApiForArchRuleCreation>` (requires list as modules can be implemented using different architectures)




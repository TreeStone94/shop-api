# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**shop-api** is a Spring Boot 4.0.2 application built with Java 17, using Gradle 9.3.0 as the build tool. This is a REST API project that uses Spring Data JPA with PostgreSQL as the database, and Lombok for reducing boilerplate code.

**Technology Stack:**
- Spring Boot 4.0.2
- Java 17
- Gradle 9.3.0
- Spring Data JPA
- PostgreSQL
- Lombok
- JUnit Platform for testing

## Development Commands

### Build and Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Clean and build
./gradlew clean build
```

### Testing
```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "com.github.treestone.shop_api.ClassName"

# Run tests with continuous build
./gradlew test --continuous
```

### Other Gradle Tasks
```bash
# View all available tasks
./gradlew tasks

# Build without running tests
./gradlew build -x test

# Check for dependency updates
./gradlew dependencyUpdates
```

## Project Structure

```
src/main/java/com/github/treestone/shop_api/
  └── ShopApiApplication.java          # Main Spring Boot application entry point

src/main/resources/
  ├── application.properties/yml       # Application configuration (to be created)
  ├── static/                          # Static resources
  └── templates/                       # Template files
```

**Base Package:** `com.github.treestone.shop_api`

## Architecture Notes

This is a standard Spring Boot application following the typical layered architecture pattern:

- **Entry Point:** `ShopApiApplication.java` is the main class annotated with `@SpringBootApplication`
- **Database:** PostgreSQL is configured as the runtime dependency
- **ORM:** Spring Data JPA for database interactions
- **Code Generation:** Lombok is used to reduce boilerplate (getters, setters, constructors, etc.)

When adding new features, follow the standard Spring Boot layered architecture:
1. **Controller Layer** - REST endpoints (`@RestController`)
2. **Service Layer** - Business logic (`@Service`)
3. **Repository Layer** - Data access (`@Repository` with JPA)
4. **Domain/Entity Layer** - JPA entities (`@Entity`)

## Configuration

The project uses PostgreSQL, so ensure you have:
- A PostgreSQL instance running
- Application properties configured in `src/main/resources/application.properties` or `application.yml` with database connection details (url, username, password)

## Dependencies

Key dependencies are managed through Gradle:
- `spring-boot-starter-data-jpa` - JPA/Hibernate support
- `spring-boot-starter-webmvc` - Web MVC and REST support
- `postgresql` - PostgreSQL JDBC driver
- `lombok` - Code generation at compile time

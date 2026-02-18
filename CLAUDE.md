# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**shop-api** is a Spring Boot 4.0.2 application built with Java 17, using Gradle 9.3.0 as the build tool. This is a REST API project that uses Spring Data JPA with MySQL as the database, and Lombok for reducing boilerplate code.

**Technology Stack:**
- Spring Boot 4.0.2
- Java 17
- Gradle 9.3.0
- Spring Data JPA
- MySQL
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

## 유즈케이스 + 도메인 기반 폴더 구조

This project follows a use-case and domain-driven architecture with clear layer separation:

### domain
가장 핵심이 되는 비즈니스로직이 담기는 계층
이 계층에는 Spring도 모르고 JPA도 모르는 순수 자바 객체(POJO)로 이루어져 있음

The core business logic layer. Ideally should contain pure Java objects (POJOs) without Spring or JPA dependencies.

### application
유스케이스를 담기는 계층
"주문하기", "주문 취소하기", "주문 목록 조회하기" 처럼 사용자가 시스템에 요청하는 행위 하나하나가 유스케이스
이 계층은 도메인 객체를 조합해서 비즈니스 흐름을 만들고, 외부 시스템(DB, Kafka 등)과는 인터페이스(Port)를 통해서만 소통

The use-case layer. Each use case represents a user action like "place order", "cancel order", or "view order list". This layer orchestrates domain objects to implement business flows and communicates with external systems (DB, Kafka, etc.) only through interfaces (Ports).

### infrastructure
기술 세부사항이 담기는 계층
JPA Repository 구현체, Kafka Producer, 외부 API 클라이언트 등이 여기에 있음

The technical details layer containing JPA Repository implementations, Kafka producers, external API clients, etc.

**Dependency Flow:** [infrastructure] → [application] → [domain]

**Package Structure:**
```
com.github.treestone.shop_api/
├── <feature>/
│   ├── domain/          # Domain entities and business logic
│   ├── application/     # Use case implementations
│   └── infrastructure/  # Repository implementations, adapters
```

## Configuration

The project uses MySQL, so ensure you have:
- A MySQL instance running (default: localhost:3306)
- Database named `shop` created
- Application properties configured in `src/main/resources/application.yaml` with database connection details (url, username, password)

## Dependencies

Key dependencies are managed through Gradle:
- `spring-boot-starter-data-jpa` - JPA/Hibernate support
- `spring-boot-starter-webmvc` - Web MVC and REST support
- `mysql-connector-j` - MySQL JDBC driver
- `lombok` - Code generation at compile time

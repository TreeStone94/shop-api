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

## 아키텍처: 실용적인 유즈케이스 기반 설계

이 프로젝트는 **실용적이고 유즈케이스 중심의 아키텍처**를 따릅니다. 아키텍처의 순수성보다는 단순함과 비즈니스 가치를 우선시하며, 불필요한 추상화를 피해 인지 부하를 최소화합니다.

### 핵심 원칙
- **비즈니스 가치 우선**: 아키텍처는 비즈니스를 위해 존재하며, 그 반대가 아님
- **인지 부하 최소화**: 과도한 추상화와 불필요한 복잡성 지양
- **단일 책임 원칙(SRP)**: 각 UseCase는 하나의 액터/비즈니스 요구사항만 담당
- **추상화 지연**: 인터페이스와 분리는 진짜 필요할 때만 생성
- **일관성 > 완벽함**: 팀 전체의 네이밍/구조 규칙이 가장 중요

### 계층 아키텍처 (안쪽 → 바깥쪽)

#### 1. Domain Layer (가장 안쪽)
**목적**: 외부 의존성 없이 동작할 수 있는 핵심 비즈니스 로직

**특징**:
- **Domain Model Pattern** 따름: 비즈니스 로직이 엔티티 자체에 존재 (예: `user.isActive()`)
- 도메인 엔티티에 JPA 어노테이션 **허용** (실용적 접근 - 진짜 필요할 때만 분리)
- **도메인 정책 검증**이 여기 속함 (예: 이메일 형식, 비즈니스 규칙)
- **일급 컬렉션** 사용 (예: `Users`가 `List<User>`를 감싸는 형태) → 재사용성 향상

**예시**:
```java
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String email;

    // 도메인 로직
    public boolean isActive() { ... }
    public void validateEmail() { ... }
}
```

#### 2. Port Layer
**목적**: 모든 외부 인프라와의 상호작용 (DB, Cache, 외부 API)

**네이밍 규칙**:
- JPA Repository: `UserRepository`
- Cache: `UserCache`
- 외부 API: `UserClient`
- DTO: `UserRequest` / `UserResponse` (파라미터가 많을 때)

**중요**: 인터페이스 강제 금지 - 진짜 필요할 때만 추상화 도입 (예: 구현체가 여러 개 존재할 때)

#### 3. UseCase Layer (애플리케이션 핵심)
**목적**: 비즈니스 요구사항 하나당 클래스 하나

**특징**:
- 하나의 UseCase = 하나의 비즈니스 흐름 (예: `RegisterUserUseCase`, `GetUserDetailUseCase`)
- Domain + Port 계층에 의존
- Input/Output DTO는 UseCase의 **내부 클래스**로 정의
- **SRP 엄격히 준수**: 각 UseCase는 하나의 액터에게만 응답

**네이밍**: `XxxUseCase`

**구조 예시**:
```java
@Service
public class RegisterUserUseCase {

    public record Input(String email, String name) {}
    public record Output(Long userId, String email) {}

    public Output execute(Input input) {
        // 비즈니스 흐름 조율
    }
}
```

**중복 코드 철학**:
- **진짜 중복** (변경 이유가 같음) vs **우연한 중복** (비슷해 보이지만 변경 이유가 다름) 구분
- 공통 로직을 성급하게 추출하지 말 것

#### 4. Processor / ApplicationService Layer (선택 사항)
**목적**: 여러 UseCase에 걸쳐 **진짜로 공유되는** 비즈니스 로직 추출

**중요**: "필요에 의해 탄생해야 함" - 절대 미리 만들지 말 것. 3개 이상의 UseCase에서 검증된 중복이 있을 때만 도입.

#### 5. Controller Layer (가장 바깥쪽)
**목적**: HTTP 요청/응답 처리

**특징**:
- 의존성 주입을 통해 UseCase에 위임
- 하나의 Controller에서 **여러 UseCase 주입 가능** (이건 괜찮음)
- 가능하면 UseCase의 Input/Output을 HTTP DTO로 재사용
  ```java
    @RestController
    @RequiredArgsConstructor
    public class MemberController {
    
        private final RegisterMemberService registerMemberService;
        
        @PostMapping("/members")
        public ResponseEntity<RegisterMemberService.Output> registerMember(
            @RequestBody RegisterMemberService.Input input 
        ) {
            return ResponseEntity
                .ok(registerMemberService.execute(input))
                .build();
        }
    
    }
    ```
- HTTP 구조가 다를 때만 전용 `XxxRequest`/`XxxResponse` 생성 (예: path variable + body)
  ```java
    @RestController
    @RequiredArgsConstructor
    public class MemberController {
    
        private final UpdateMemberService updateMemberService;
        
        @PostMapping("/members/{memberId}")
        public ResponseEntity<UpdateMemberService.Output> updateMember(
            @PathVariable long memberId,
            @RequestBody UpdateMemberBody body
        ) {
            var input = UpdateMemberService.Input(
                memberId,
                body.name(),
                body.email(),
                body.password()
            )
        
            return ResponseEntity
                .ok(updateMemberService.execute(input))
                .build();
        }
    }
    
    record UpdateMemberBody(String name, String email, String password) {
    
    }
    ```
- **입력 데이터 검증** (null, blank, format 체크)은 여기서 Spring Validation으로 처리

**검증 분리**:
- **Controller**: 입력 데이터 유효성 (`@NotNull`, `@NotBlank`, `@Email`)
- **Domain**: 도메인 정책 유효성 (비즈니스 규칙)

### 패키지 구조

```
com.github.treestone.shop_api/
├── user/
│   ├── domain/
│   │   └── User.java                    # 비즈니스 로직을 포함한 도메인 엔티티
│   ├── port/
│   │   ├── UserRepository.java          # JPA Repository
│   │   ├── UserCache.java               # (선택) Cache 계층
│   │   └── UserClient.java              # (선택) 외부 API
│   ├── usecase/
│   │   ├── RegisterUserUseCase.java
│   │   ├── GetUserDetailUseCase.java
│   │   └── UpdateUserUseCase.java
│   ├── processor/                       # (선택, 필요할 때만)
│   │   └── UserProcessor.java
│   └── controller/
│       └── UserController.java
├── product/
│   └── (동일한 구조)
└── order/
    └── (동일한 구조)
```

### 피해야 할 안티패턴
- ❌ "미래의 유연성"을 위한 성급한 인터페이스 추출
- ❌ 명확한 필요 없이 도메인/영속성 엔티티 강제 분리
- ❌ 검증된 중복이 없는데 Processor 계층 미리 만들기
- ❌ 우연한 중복을 진짜 중복으로 취급하기
- ❌ UseCase 하나당 Controller 하나씩 만들기 (불필요한 오버헤드)

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

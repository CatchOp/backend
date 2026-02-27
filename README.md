# CatchOp Backend

채용공고 관리 및 리마인더 서비스의 백엔드 API.

## Tech Stack

- **Kotlin 2.2** / JDK 21
- **Spring Boot 4.0.3**
- **PostgreSQL 16**
- **Gradle Kotlin DSL**

## Architecture

Layered + 선택적 추상화 (외부 서비스만 인터페이스 분리)

```
controller → service → repository
                ↓
              domain
              infra (인터페이스로 주입)
```

## Getting Started

### Prerequisites

- JDK 21
- PostgreSQL 16

### Run

```bash
./gradlew bootRun
```

### Build

```bash
./gradlew build
```

### Test

```bash
./gradlew test
```

## Project Structure

```
src/main/kotlin/com/catchop/backend/
├── domain/          # 순수 Kotlin 도메인 모델 (Aggregate 단위)
├── repository/      # Spring Data JPA
├── service/         # 비즈니스 로직 (feature별)
├── controller/      # REST API + DTO (feature별)
├── infra/           # 외부 서비스 (AI, Crawler, Push)
└── common/          # 설정, 보안
```

## API Documentation

개발 서버 실행 후 `http://localhost:8080/swagger-ui.html`

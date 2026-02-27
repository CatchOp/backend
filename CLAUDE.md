# CLAUDE.md

## Project Overview

CatchOp Backend — 채용공고 관리 및 리마인더 서비스의 백엔드 API.
NestJS/TypeScript/MongoDB에서 **Kotlin/Spring Boot 4/PostgreSQL**로 마이그레이션 진행 중.

## Tech Stack

- **Language**: Kotlin 2.2, JDK 21
- **Framework**: Spring Boot 4.0.3
- **DB**: PostgreSQL 16 (현재 H2 인메모리로 임시 사용)
- **Build**: Gradle Kotlin DSL
- **Test**: JUnit 5 + MockK + AssertJ

## Architecture — Layered + 선택적 추상화

계층형 기반에 **외부 서비스만 인터페이스 분리**, **Service는 feature별 그룹핑**.

```
src/main/kotlin/com/catchop/backend/
├── domain/                      # 순수 Kotlin (Spring import 없음), Aggregate 단위
│   ├── posting/                 # Posting, PostingStatus, PostingNotFoundException
│   ├── user/                    # User, NotificationSettings, AuthProvider
│   ├── userposting/             # UserPosting, UserPostingDetail
│   ├── company/                 # Company
│   ├── position/                # Position
│   ├── analysis/                # Analysis
│   ├── notification/            # DeviceToken
│   └── common/                  # DomainException 베이스, PaginatedResult
├── repository/                  # Spring Data JPA (직접 사용, 추상화 없음)
│   ├── entity/                  # JPA @Entity 클래스
│   └── {Name}Repository.kt     # JpaRepository 인터페이스
├── service/                     # @Service feature별 그룹핑
│   ├── posting/                 # PostingCommandService, PostingQueryService
│   ├── auth/                    # AuthService
│   ├── company/                 # CompanyService
│   ├── position/                # PositionService
│   ├── notification/            # NotificationService
│   └── analysis/                # AnalysisService
├── controller/                  # @RestController, feature별 + DTO 포함
│   ├── posting/                 # PostingController + dto/
│   ├── auth/                    # AuthController + dto/
│   ├── company/ / position/ / notification/
├── infra/                       # 외부 서비스 (인터페이스 + 구현 분리)
│   ├── ai/                      # AiParser(I), AiAnalyzer(I), GeminiAiAdapter
│   ├── crawler/                 # WebCrawler(I), JsoupCrawlerAdapter
│   └── notification/            # PushSender(I), ApnsPushAdapter
├── common/                      # 횡단 관심사
│   ├── config/                  # Spring 설정
│   └── security/                # JWT, OAuth
└── BackendApplication.kt
```

### 의존 방향

```
controller → service → repository (직접)
                ↓
              domain (모두 참조 가능)
              infra (외부 서비스는 인터페이스로 주입)
```

### 핵심 원칙

- **domain**: 순수 Kotlin — Spring, JPA import 없음
- **repository**: Spring Data JPA 직접 사용. 이미 인터페이스이므로 추가 추상화 불필요
- **service**: feature별 그룹핑 + Command/Query 분리. Repository 직접 주입, 외부 서비스는 인터페이스 주입
- **infra**: 교체 가능성 있는 외부 서비스만 인터페이스 분리 (AI, Crawler, Push)
- **controller**: Service 호출만. 비즈니스 로직 없음

## Commands

```bash
./gradlew bootRun          # 개발 서버
./gradlew build            # 빌드
./gradlew test             # 테스트
./gradlew clean build      # 클린 빌드
```

## Migration Plan — 현재 진행 상황

NestJS → Kotlin/Spring Boot 마이그레이션 멘토링 진행 중.
**Claude는 코드를 직접 작성하지 않고, 멘토링/가이드 역할만 한다.**

### Phase 진행 상태

| Phase | 내용 | 상태 |
|-------|------|------|
| 0 | Kotlin 기초 문법 | 완료 |
| 1 | Spring Boot 프로젝트 스캐폴딩 | 완료 |
| 2 | Domain — 모델, Enum, 예외 | 대기 |
| 3 | Repository — Spring Data JPA, JPA Entity, Flyway | 대기 |
| 4 | Service — 비즈니스 로직, @Transactional | 대기 |
| 5 | 외부 서비스 — AI, Crawler, APNs 어댑터 | 대기 |
| 6 | Controller + Spring Security (JWT, OAuth) | 대기 |
| 7 | Scheduler, OAuth, 테스트, Docker | 대기 |

### Phase 1 완료 사항

- [x] Spring Initializr로 프로젝트 생성 (Spring Boot 4.0.3, Gradle Kotlin)
- [x] application.yml 기본 설정 (autoconfigure.exclude로 DB 없이 부팅)
- [x] 디렉토리 구조 생성 (Layered + 선택적 추상화)
- [x] ./gradlew bootRun 성공 확인

### Phase별 핵심 가이드

**Phase 2: Domain**
- 순수 Kotlin data class + companion object 패턴
- enum class (AuthProvider, PostingStatus)
- sealed class 예외 계층 (DomainException → NotFoundException/ConflictException/ValidationException)
- TypeScript readonly → val, T | undefined → T?, Date → LocalDateTime

**Phase 3: Repository**
- JPA @Entity (7개 테이블), @ManyToOne 관계, @Embedded
- JpaRepository 인터페이스 (구현 자동 생성)
- Flyway 마이그레이션 (V1__initial_schema.sql)
- MongoDB ObjectId → PostgreSQL UUID
- Entity ↔ Domain 변환 메서드

**Phase 4: Service**
- @Service + 생성자 주입 (Symbol/Port 불필요)
- @Transactional (MongoDB 수동 보상 트랜잭션 → 자동 롤백)
- SLF4J 로깅

**Phase 5: 외부 서비스**
- Gemini AI: Google AI Java SDK
- Crawler: Jsoup (Cheerio 대체)
- APNs: java-apns 라이브러리
- 교체 가능성 있는 서비스만 인터페이스 분리

**Phase 6: Controller + Security**
- @RestController + Jakarta Validation (@field: 타겟)
- Spring Security JWT filter chain (글로벌)
- @ControllerAdvice 예외 핸들러
- Swagger: springdoc-openapi

**Phase 7: 마무리**
- @Scheduled 리마인더
- Spring Security OAuth2 Client
- JUnit 5 + MockK 테스트
- Multi-stage Dockerfile

## Mentoring Rules

1. **Claude는 코드를 직접 수정하지 않는다** — 방향 제시, 코드 리뷰, 트러블슈팅만
2. **NestJS 대비 설명** — 기존 코드와 비교하며 Spring/Kotlin 패턴 안내
3. **사용자가 코드를 직접 작성** — 학습이 목표
4. **각 Phase 완료 시 검증 방법 안내** (테스트, bootRun 등)

## Original NestJS Reference

기존 NestJS 코드베이스 구조 (마이그레이션 레퍼런스용):

| NestJS 패턴 | Spring 대응 |
|---|---|
| `@Module` + `providers` 와이어링 | `@ComponentScan` 자동 등록 |
| `Symbol('TOKEN')` + `@Inject()` | 타입 기반 생성자 주입 |
| `@InjectModel()` + `Model<T>` | `JpaRepository<T, UUID>` |
| `class-validator` 데코레이터 | Jakarta Validation `@field:` |
| `@UseGuards(JwtAuthGuard)` | Spring Security filter chain |
| `@CurrentUser()` 커스텀 데코레이터 | `@AuthenticationPrincipal` |
| `HttpExceptionFilter` | `@ControllerAdvice` + `@ExceptionHandler` |
| `TransformInterceptor` | `ResponseBodyAdvice` 또는 직접 래핑 |
| `ConfigModule` + `.env` | `application.yml` + `@ConfigurationProperties` |
| `@Cron(EVERY_HOUR)` | `@Scheduled(cron = "0 0 * * * *")` |

### 기존 도메인 모델 (7개)

| 모델 | 핵심 필드 | 비즈니스 로직 |
|------|----------|-------------|
| User | email, provider, notificationSettings | toProfile() |
| Posting | url, title, companyId, positionId, deadline | formatDDay() |
| UserPosting | userId, postingId, status, remindAt, memo | calculateRemindAt(), deriveAppliedAt(), belongsTo() |
| Company | name, logoUrl | matches(), hasLogo() |
| Position | category, subcategory, label | getFullPath(), matches() |
| Analysis | postingId, content, model | getPreview(), isStale() |
| DeviceToken | userId, token, isActive | belongsTo(), canReceivePush() |
| NotificationSettings (VO) | enabled, remindTime, snoozeDays, timezone | withUpdates() → copy() |

### 기존 Feature별 Service

| Service | 역할 | 복잡도 |
|---------|------|--------|
| CreatePostingService | URL 크롤링→파싱→저장→분석 오케스트레이션 | 높음 |
| QueryPostingService | 목록/단건/분석 조회 | 중간 |
| ManagePostingService | 상태/리마인드/메모 수정, 삭제 | 중간 |
| AuthCoreService | 토큰 생성/갱신, Apple 검증, 프로필/설정 | 높음 |
| CompanyService, PositionService | CRUD | 낮음 |
| NotificationService | 디바이스 등록/해제, 푸시 발송 | 중간 |
| ReminderSchedulerService | 매시간 리마인더 체크→푸시 | 중간 |

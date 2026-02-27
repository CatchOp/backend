package com.catchop.backend.domain.common.exception

/**
 * 도메인 예외 베이스 클래스
 * - sealed class: 컴파일러가 모든 하위 타입을 알고 있어 when에서 else 불필요
 * - RuntimeException 상속: Spring @ControllerAdvice에서 잡을 수 있도록
 *
 * 핵심 포인트:
 *   - sealed class — TypeScript의 abstract class와 비슷하지만, 하위 클래스가 같은 패키지에서만 정의 가능. 대신 when에서 모든 케이스를 커버하면 else가 필요 없음
 *   - open class — Kotlin은 기본적으로 모든 클래스가 final(상속 불가). open을 붙여야 상속 가능
 *   - 한 파일에 여러 클래스 작성 가능 — 파일명은 대표 클래스명 DomainException.kt
 *   - statusCode에 val이 없으면 생성자 파라미터일 뿐 클래스 프로퍼티가 아닙니다. 나중에 @ControllerAdvice에서 exception.statusCode로 접근 불가
 */
sealed class DomainException (message: String, val statusCode: Int) : RuntimeException(message)

/**
 *
 * - open class: 각 도메인의 구체 예외가 상속할 수 있도록
 */
open class DomainNotFoundException(message: String) : DomainException(message, 404)
open class DomainConflictException(message: String) : DomainException(message, 409)
open class DomainValidationException(message: String) : DomainException(message, 400)
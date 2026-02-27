package com.catchop.backend.domain.common.dto

/**
 * 페이지네이션 응답 공통 모델
 * @param T 아이템 타입
 *
 * 핵심 포인트
 * - interface → data class — TypeScript interface는 구조만 정의하지만, Kotlin data class는 equals(), hashCode(), toString(), copy()를 자동 생성
 * - T[] → List<T> — Kotlin의 List는 불변(읽기 전용). 수정 가능한 리스트는 MutableList<T>
 */
data class PaginationResult<T> (
    /** 현재 페이지의 아이템 목록 */
    val items: List<T>,
    /** 전체 아이템 수 */
    val total: Long,
    /** 현재 페이지 번호 (1부터 시작) */
    val page: Int,
    /** 전체 페이지 수 */
    val totalPages: Int,
)
package com.catchop.backend.domain.company.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * 회사 도메인 모델
 * - 채용공고와 연결되는 회사 정보
 */
data class Company(
    val id: UUID,
    val name: String,
    val logoUrl: String?,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        /**
         * DB에서 복원할 때 사용
         * @return Company 인스턴스
         */
        fun reconstruct(
            id: UUID,
            name: String,
            logoUrl: String?,
            description: String?,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ): Company = Company(
            id = id,
            name = name,
            logoUrl = logoUrl,
            description = description,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
    /**
     * 검색어와 회사명 부분 일치 여부 (대소문자 무시)
     */
    fun matches(searchTerm: String): Boolean = name.contains(searchTerm, ignoreCase = true)

    /**
     * 로고 URL 존재 여부
     */
    fun hasLogo(): Boolean = logoUrl != null
}

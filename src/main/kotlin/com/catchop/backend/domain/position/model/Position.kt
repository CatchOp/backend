package com.catchop.backend.domain.position.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * 직무 도메인 모델
 * - 채용공고의 직무 분류 정보
 */
data class Position(
    val id: UUID,
    val category: PositionCategory,
    val subCategory: SubPositionCategory,
    val label: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun reconstruct(
            id: UUID,
            category: PositionCategory,
            subCategory: SubPositionCategory,
            label: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ): Position = Position(id, category, subCategory, label, createdAt, updatedAt)
    }

    fun getFullPath(): String = "${category.value} > ${subCategory.value}"

    fun matches(searchTerm: String): Boolean =
        category.value.contains(searchTerm, ignoreCase = true)
                || subCategory.value.contains(searchTerm, ignoreCase = true)
                || label.contains(searchTerm, ignoreCase = true)

}

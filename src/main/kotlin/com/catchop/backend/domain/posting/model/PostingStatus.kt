package com.catchop.backend.domain.posting.model

enum class PostingStatus(val value: String) {
    NOT_APPLIED("미지원"),
    APPLIED("지원 완료"),
    PASS("패스")
}
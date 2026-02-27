package com.catchop.backend.domain.user.model

enum class AuthProvider(val value: String) {
    GOOGLE("google"),
    GITHUB("github"),
    APPLE("apple"),
}
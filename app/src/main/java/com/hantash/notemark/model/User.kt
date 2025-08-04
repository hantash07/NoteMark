package com.hantash.notemark.model

data class User (
    val username: String,
    val email: String,
    val password: String,
    val id: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

package com.hantash.notemark.model

data class User (
    val username: String,
    val email: String,
    val password: String,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

package com.example.pauze.data.model

data class LocalLoginRequestDto(
    val email: String,
    val password: String
)

data class AuthTokenResultDto(
    val accessToken: String,
    val refreshToken: String
)

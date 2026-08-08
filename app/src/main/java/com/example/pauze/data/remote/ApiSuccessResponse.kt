package com.example.pauze.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ApiSuccessResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T
)

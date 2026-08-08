package com.example.pauze.data.model

data class BaseResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T? = null
)

fun <T> BaseResponse<T>.getOrThrow(): T {
    if (!isSuccess) throw IllegalStateException("[$code] $message")
    return result ?: throw IllegalStateException("[$code] $message")
}
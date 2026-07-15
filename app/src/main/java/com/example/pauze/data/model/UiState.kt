package com.example.pauze.data.model

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null,
)
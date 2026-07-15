package com.example.pauze.data.model

data class UiState<T>(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)
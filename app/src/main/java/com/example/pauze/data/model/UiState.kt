package com.example.pauze.data.model

data class UiState (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)
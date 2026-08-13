package com.example.pauze.data.model

abstract class UiState (
    open val isLoading: Boolean = false,
    open val error: Throwable? = null,
)

data class BaseUiState<T>(
    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    val data: T,
): UiState(isLoading = isLoading, error = error)

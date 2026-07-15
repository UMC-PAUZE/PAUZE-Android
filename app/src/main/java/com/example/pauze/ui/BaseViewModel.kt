package com.example.pauze.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.model.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

abstract class BaseViewModel: ViewModel() {
    // UI 상태 클래스를 Flow로 다룸
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.value

    // UI 상태를 업데이트 하는 함수
    protected fun updateState(updater: (UiState) -> UiState){
        _uiState.update { updater(it) }
    }

    // 로딩 및 예외 처리를 자동화한 공통 코루틴 launch 함수
    protected fun launch(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                updateState { it.copy(isLoading = true) }
                block()
            } catch (e: Exception) {
                updateState { it.copy(error = e) }
                println("에러 발생: ${e.message}")
                e.printStackTrace()
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }
}
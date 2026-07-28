package com.example.pauze.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.model.BaseUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import java.sql.DriverManager.println

abstract class BaseViewModel<EFFECT, DATA: Any?>(
    uiState: BaseUiState<DATA>
) : ViewModel() {
    // UI 상태 클래스를 Flow로 다룸
    private val _uiState = MutableStateFlow(uiState)
    val uiState = _uiState.asStateFlow()

    // 단발성 이벤트를 처리하는 함수
    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    // UI 상태를 업데이트 하는 함수
    protected fun updateState(updater: (BaseUiState<DATA>) -> BaseUiState<DATA>){
        _uiState.update { updater(it) }
    }

    // 내부 데이터만 업데이트 하는 함수
    protected fun updateData(updater: (DATA) -> (DATA)){
        _uiState.update {
            it.copy(data = updater(it.data))
        }
    }

    // 로딩 및 예외 처리를 자동화한 공통 코루틴 launch 함수
    protected fun launch(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                updateState { it.copy(isLoading = true, error = null) }
                block()
            } catch (e: CancellationException){
                println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
                throw e
            }
            catch (e: Exception) {
                updateState { it.copy(isLoading = false, error = e) }
                println("예외 발생: ${e.message}")
                e.printStackTrace()
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    protected fun sendEffect(effect: EFFECT){
        _effect.trySend(effect)
    }
}
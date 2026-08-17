package com.example.pauze.ui.pauze.breathing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.BreathPattern
import com.example.pauze.data.model.BreathPhase
import com.example.pauze.data.model.BreathState
import com.example.pauze.data.repository.PauzeUsageRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

sealed interface BreathingEffect {
    object NavigateToBack: BreathingEffect
    object ShowExitDialog: BreathingEffect
}

@HiltViewModel
class PauzeBreathingViewModel @Inject constructor(
    private val pauzeUsageRepository: PauzeUsageRepository
) : BaseViewModel<BreathingEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    val patterns = listOf(
        BreathPattern(4, 7, 8), // 478 호흡
        BreathPattern(4, 4, 4), // 박스 호흡
        BreathPattern(4, 0, 6) // 간단 호흡
    )

    private val totalCycle = 3

    var selectedTabIndex by mutableStateOf(0)
        private set
    var breathState by mutableStateOf(BreathState(BreathPhase.READY, 3))
        private set
    var currentCycle by mutableStateOf(0)
        private set
    var isPlaying by mutableStateOf(true)
        private set
    var isCompleted by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    init{
        startBreathing(resume = false)
    }

    fun selectTab(index: Int){
        selectedTabIndex = index
        startBreathing(resume = false)
    }

    fun togglePlayPause(){
        isPlaying = !isPlaying
    }

    fun reset(){
        startBreathing(resume = false)
    }

    private fun startBreathing(resume: Boolean) {
        timerJob?.cancel()
        currentCycle = 0
        isPlaying = resume
        isCompleted = false

        val pattern = patterns[selectedTabIndex]
        val phases = buildList {
            add(BreathPhase.INHALE to pattern.inhale)
            if (pattern.hold > 0) add(BreathPhase.HOLD to pattern.hold)
            add(BreathPhase.EXHALE to pattern.exhale)
        }

        timerJob = viewModelScope.launch {
            breathState = BreathState(BreathPhase.READY, 3)
            for (sec in 3 downTo 1) { // 3 - 2 - 1 카운터
                waitWhilePaused()
                breathState = BreathState(BreathPhase.READY, sec)
                delayAndWaitIfPaused()
            }

            // 실제 호흡 루프
            while (currentCycle < totalCycle) {
                for ((phase, duration) in phases) {
                    for (sec in 1..duration) {
                        waitWhilePaused()
                        breathState = BreathState(phase, sec)
                        delayAndWaitIfPaused()
                    }
                }
                currentCycle++
            }

            ensureActive()
            if (!isCompleted) {
                isCompleted = true
                pauzeUsageRepository.recordCompletedUsage() // pauze 기록 처리
            }
            delay(1000)
            sendEffect(BreathingEffect.NavigateToBack)
        }
    }

    private suspend fun delayAndWaitIfPaused() {
        delay(1000)
        waitWhilePaused()
    }

    private suspend fun waitWhilePaused() {
        while(!isPlaying){
            delay(100)
        }
    }

    fun onBackClick() {
        if (isCompleted || breathState.phase == BreathPhase.READY) {
            cancelTimerAndNavigateBack()
        } else {
            isPlaying = false
            sendEffect(BreathingEffect.ShowExitDialog)
        }
    }

    fun onExitCancel() {
        isPlaying = true
    }

    fun onExitConfirm() {
        cancelTimerAndNavigateBack()
    }

    private fun cancelTimerAndNavigateBack() {
        timerJob?.cancel()
        timerJob = null
        isPlaying = false
        sendEffect(BreathingEffect.NavigateToBack)
    }
}

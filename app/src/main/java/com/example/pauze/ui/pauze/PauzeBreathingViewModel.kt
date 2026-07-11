package com.example.pauze.ui.pauze

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class BreathPhase {INHALE, HOLD, EXHALE} // 들숨, 참기, 날숨

data class BreathPattern(val inhale: Int, val hold: Int, val exhale: Int)
data class BreathState(val phase: BreathPhase, val second: Int)

class PauzeBreathingViewModel: ViewModel() {
    val patterns = listOf(
        BreathPattern(4, 7, 8), // 478 호흡
        BreathPattern(4, 4, 4), // 박스 호흡
        BreathPattern(4, 0, 6) // 간단 호흡
    )

    private val totalCycle = 3

    var selectedTabIndex by mutableStateOf(0)
        private set
    var breathState by mutableStateOf(BreathState(BreathPhase.INHALE, 1))
        private set
    var currentCycle by mutableStateOf(0)
        private set
    var isPlaying by mutableStateOf(true)
        private set
    var isFinished by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    init{ // 재생바 추가 시 변경
        startBreathing()
    }

    fun selectTab(index: Int){
        selectedTabIndex = index
        startBreathing()
    }

    fun togglePlayPause(){
        isPlaying = !isPlaying
    }

    fun reset(){
        startBreathing()
    }

    private fun startBreathing() {
        timerJob?.cancel()
        currentCycle = 0
        isFinished = false

        val pattern = patterns[selectedTabIndex]
        val phases = buildList {
            add(BreathPhase.INHALE to pattern.inhale)
            if (pattern.hold > 0) add(BreathPhase.HOLD to pattern.hold)
            add(BreathPhase.EXHALE to pattern.exhale)
        }

        timerJob = viewModelScope.launch {
            while (currentCycle < totalCycle) {
                for ((phase, duration) in phases) {
                    for (sec in 1..duration) {
                        waitWhilePaused()
                        breathState = BreathState(phase, sec)
                        delay(1000)
                    }
                }
                currentCycle++
            }
            isFinished = true // 메인 화면 이동
        }
    }

    private suspend fun waitWhilePaused() {
        while(!isPlaying){
            delay(100)
        }
    }
}
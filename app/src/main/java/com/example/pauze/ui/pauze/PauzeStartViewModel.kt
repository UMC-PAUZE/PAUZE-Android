package com.example.pauze.ui.pauze

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.PauzeUsageRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface PauzeStartEffect {
    object NavigateToBreathing : PauzeStartEffect
    object NavigateToAuditory : PauzeStartEffect
    object NavigateToVisual : PauzeStartEffect
    object NavigateToGuide : PauzeStartEffect
    object NavigateToHome : PauzeStartEffect
}

@HiltViewModel
class PauzeStartViewModel @Inject constructor(
    private val pauzeUsageRepository: PauzeUsageRepository
) : BaseViewModel<PauzeStartEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    private var isGuideNavigationRequested = false

    fun onStartBreathingClick() = sendEffect(PauzeStartEffect.NavigateToBreathing)
    fun onAuditoryClick() = sendEffect(PauzeStartEffect.NavigateToAuditory)
    fun onVisualClick() = sendEffect(PauzeStartEffect.NavigateToVisual)
    fun onGuideClick() {
        if (isGuideNavigationRequested) return
        isGuideNavigationRequested = true
        pauzeUsageRepository.recordCompletedUsage()
        sendEffect(PauzeStartEffect.NavigateToGuide)
    }
    fun onBackClick() = sendEffect(PauzeStartEffect.NavigateToHome)
}

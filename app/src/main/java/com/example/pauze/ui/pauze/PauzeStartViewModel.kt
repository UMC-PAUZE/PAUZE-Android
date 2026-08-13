package com.example.pauze.ui.pauze

import com.example.pauze.data.model.BaseUiState
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
class PauzeStartViewModel @Inject constructor() : BaseViewModel<PauzeStartEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    private var isGuideNavigationRequested = false

    fun onStartBreathingClick() = sendEffect(PauzeStartEffect.NavigateToBreathing)
    fun onAuditoryClick() = sendEffect(PauzeStartEffect.NavigateToAuditory)
    fun onVisualClick() = sendEffect(PauzeStartEffect.NavigateToVisual)
    fun onGuideClick() {
        if (isGuideNavigationRequested) return
        isGuideNavigationRequested = true
        sendEffect(PauzeStartEffect.NavigateToGuide)
    }

    fun onGuideNavigationHandled() {
        isGuideNavigationRequested = false
    }

    fun onBackClick() = sendEffect(PauzeStartEffect.NavigateToHome)
}

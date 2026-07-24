package com.example.pauze.ui.pauze

import com.example.pauze.ui.BaseViewModel

sealed interface PauzeStartEffect {
    object NavigateToBreathing : PauzeStartEffect
    object NavigateToAuditory : PauzeStartEffect
    object NavigateToVisual : PauzeStartEffect
    object NavigateToGuide : PauzeStartEffect
    object NavigateToHome : PauzeStartEffect
}

class PauzeStartViewModel : BaseViewModel<PauzeStartEffect>() {
    fun onStartBreathingClick() = sendEffect(PauzeStartEffect.NavigateToBreathing)
    fun onAuditoryClick() = sendEffect(PauzeStartEffect.NavigateToAuditory)
    fun onVisualClick() = sendEffect(PauzeStartEffect.NavigateToVisual)
    fun onGuideClick() = sendEffect(PauzeStartEffect.NavigateToGuide)
    fun onBackClick() = sendEffect(PauzeStartEffect.NavigateToHome)
}
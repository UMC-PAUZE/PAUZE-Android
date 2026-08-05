package com.example.pauze.ui.pauze

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.BaseViewModel

sealed interface PauzeVisualEffect {
    data object ShowStopDialog : PauzeVisualEffect
    data object HideStopDialog : PauzeVisualEffect
}

class PauzeVisualViewModel : BaseViewModel<PauzeVisualEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    fun showStopDialog() {
        sendEffect(PauzeVisualEffect.ShowStopDialog)
    }

    fun hideStopDialog() {
        sendEffect(PauzeVisualEffect.HideStopDialog)
    }
}

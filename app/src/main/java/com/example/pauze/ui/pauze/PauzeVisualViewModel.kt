package com.example.pauze.ui.pauze

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.VisualGuideRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PauzeVisualState(
    val visualUrl: String? = null
)

sealed interface PauzeVisualEffect {
    data object ShowStopDialog : PauzeVisualEffect
    data object HideStopDialog : PauzeVisualEffect
}

@HiltViewModel
class PauzeVisualViewModel @Inject constructor(
    private val visualGuideRepository: VisualGuideRepository
) : BaseViewModel<PauzeVisualEffect, PauzeVisualState>(
    uiState = BaseUiState(data = PauzeVisualState())
) {
    fun loadVisualGuide() {
        if (uiState.value.isLoading || uiState.value.data.visualUrl != null) return

        launch {
            PauzeVisualState(
                visualUrl = visualGuideRepository.getVisualUrl()
            )
        }
    }

    fun showStopDialog() {
        sendEffect(PauzeVisualEffect.ShowStopDialog)
    }

    fun hideStopDialog() {
        sendEffect(PauzeVisualEffect.HideStopDialog)
    }
}

package com.example.pauze.ui.pauze.visual

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.VisualGuideRepository
import com.example.pauze.data.repository.PauzeUsageRepository
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
    private val visualGuideRepository: VisualGuideRepository,
    private val pauzeUsageRepository: PauzeUsageRepository
) : BaseViewModel<PauzeVisualEffect, PauzeVisualState>(
    uiState = BaseUiState(data = PauzeVisualState())
) {
    fun loadVisualGuide(onSuccess: () -> Unit) {
        val currentUrl = uiState.value.data.visualUrl

        if (!currentUrl.isNullOrBlank()) {
            onSuccess()
            return
        }

        if (uiState.value.isLoading) return

        updateState { state ->
            state.copy(error = null)
        }

        launch(
            onSuccess = { visualUrl ->
                updateData { state ->
                    state.copy(visualUrl = visualUrl)
                }
                onSuccess()
            },
        ) {
            visualGuideRepository.getVisualUrl()
                .trim()
                .takeIf { it.isNotEmpty() }
                ?: throw IllegalStateException("시각 안정 가이드 URL이 비어 있습니다.")
        }
    }

    fun showStopDialog() {
        sendEffect(PauzeVisualEffect.ShowStopDialog)
    }

    fun hideStopDialog() {
        sendEffect(PauzeVisualEffect.HideStopDialog)
    }

    fun recordCompletedUsage() {
        pauzeUsageRepository.recordCompletedUsage()
    }
}

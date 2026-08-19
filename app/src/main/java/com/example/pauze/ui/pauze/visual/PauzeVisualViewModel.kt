package com.example.pauze.ui.pauze.visual

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.VisualGuideRepository
import com.example.pauze.data.repository.PauzeUsageRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PauzeVisualState(
    val visualUrl: String? = null,
    val breatheUrl: String? = null
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
    fun loadVisualGuide(onReady: () -> Unit) {
        loadGuide(
            currentUrl = uiState.value.data.visualUrl,
            emptyUrlMessage = "시각 안정 가이드 URL이 비어 있습니다.",
            getUrl = visualGuideRepository::getVisualUrl,
            updateUrl = { state, url -> state.copy(visualUrl = url) },
            onReady = onReady
        )
    }

    fun loadBreatheGuide(onReady: () -> Unit) {
        loadGuide(
            currentUrl = uiState.value.data.breatheUrl,
            emptyUrlMessage = "호흡 가이드 URL이 비어 있습니다.",
            getUrl = visualGuideRepository::getBreatheUrl,
            updateUrl = { state, url -> state.copy(breatheUrl = url) },
            onReady = onReady
        )
    }

    private fun loadGuide(
        currentUrl: String?,
        emptyUrlMessage: String,
        getUrl: suspend () -> String,
        updateUrl: (PauzeVisualState, String) -> PauzeVisualState,
        onReady: () -> Unit
    ) {
        // 방식별로 이미 받은 URL을 재사용해 단계 이동 시 API를 반복 호출하지 않는다.
        if (!currentUrl.isNullOrBlank()) {
            onReady()
            return
        }

        // 빠른 연속 탭으로 동일한 가이드 요청이 중복 실행되는 것을 막는다.
        if (uiState.value.isLoading) return

        updateState { state ->
            state.copy(error = null)
        }

        launch(
            onSuccess = { guideUrl ->
                updateData { state ->
                    updateUrl(state, guideUrl)
                }
                onReady()
            },
            onFailure = {
                // 오디오를 불러오지 못해도 선택한 가이드 화면은 무음으로 제공한다.
                updateState { state ->
                    state.copy(error = null)
                }
                onReady()
            }
        ) {
            getUrl()
                .trim()
                .takeIf { it.isNotEmpty() }
                ?: throw IllegalStateException(emptyUrlMessage)
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

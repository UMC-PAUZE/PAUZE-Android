package com.example.pauze.ui.mypage

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.MyPageState
import com.example.pauze.data.model.NotificationsUpdate
import com.example.pauze.data.model.StabilityContentUpdate
import com.example.pauze.data.model.UpdateSettingsRequest
import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.data.repository.PauzeUsageRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface MyPageEffect {
    object NavigateToEdit: MyPageEffect
    object NavigateToAccount: MyPageEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val pauzeUsageRepository: PauzeUsageRepository
) : BaseViewModel<MyPageEffect, MyPageState>(
    uiState = BaseUiState(data = MyPageState())
) {
    fun refresh() {
        launch(onFailure = { e ->
            updateData { it.copy(loadError = e.message ?: "정보를 불러오지 못했습니다") }
        }) {
            val profile = myPageRepository.getMyPage()
            val resolvedUsageCount = profile.pauzeUsageCount
                ?: runCatching {
                    pauzeUsageRepository.getStatistics().usageCount
                }.getOrNull()

            uiState.value.data.copy(
                profile = profile.copy(pauzeUsageCount = resolvedUsageCount),
                loadError = null
            )
        }
        launch(onFailure = { e ->
            updateData { it.copy(loadError = e.message ?: "정보를 불러오지 못했습니다") }
        }) {
            val stats = myPageRepository.getProfile().stats
            uiState.value.data.copy(stats = stats, loadError = null)
        }
    }

    val dailyReminder: Boolean
        get() = uiState.value.data.profile?.settings?.notifications?.reminderAlarmActive ?: true
    val riskAlert: Boolean
        get() = uiState.value.data.profile?.settings?.notifications?.sensitiveAlarmActive ?: true
    val breathingGuide: Boolean
        get() = uiState.value.data.profile?.settings?.stabilityContent?.breathingGuideEnabled ?: true
    val stabilitySound: Boolean
        get() = uiState.value.data.profile?.settings?.stabilityContent?.stabilitySoundEnabled ?: true
    val offlineContent: Boolean
        get() = uiState.value.data.profile?.settings?.stabilityContent?.offlineContentEnabled ?: false
    fun onProfileClick() = sendEffect(MyPageEffect.NavigateToEdit)
    fun onAccountInfoClick() = sendEffect(MyPageEffect.NavigateToAccount)

    fun toggleDailyReminder() = updateSettings(
        UpdateSettingsRequest(notifications = NotificationsUpdate(reminderAlarmActive = !dailyReminder))
    )
    fun toggleRiskAlert() = updateSettings(
        UpdateSettingsRequest(notifications = NotificationsUpdate(sensitiveAlarmActive = !riskAlert))
    )
    fun toggleBreathingGuide() = updateSettings(
        UpdateSettingsRequest(stabilityContent = StabilityContentUpdate(breathingGuideEnabled = !breathingGuide))
    )
    fun toggleStabilitySound() = updateSettings(
        UpdateSettingsRequest(stabilityContent = StabilityContentUpdate(stabilitySoundEnabled = !stabilitySound))
    )
    fun toggleOfflineContent() = updateSettings(
        UpdateSettingsRequest(stabilityContent = StabilityContentUpdate(offlineContentEnabled = !offlineContent))
    )

    private fun updateSettings(request: UpdateSettingsRequest) {
        val currentProfile = uiState.value.data.profile ?: return
        if (uiState.value.isLoading) return
        launch {
            uiState.value.data.copy(profile = currentProfile.copy(settings = myPageRepository.updateSettings(request)))
        }
    }
}

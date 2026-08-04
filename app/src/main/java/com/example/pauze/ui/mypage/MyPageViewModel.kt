package com.example.pauze.ui.mypage

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.MyPageState
import com.example.pauze.data.model.NotificationsUpdate
import com.example.pauze.data.model.StabilityContentUpdate
import com.example.pauze.data.model.UpdateSettingsRequest
import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface MyPageEffect {
    object NavigateToEdit: MyPageEffect
    object NavigateToAccount: MyPageEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : BaseViewModel<MyPageEffect, MyPageState>(
    uiState = BaseUiState(data = MyPageState())
) {
    init {
        refresh()
    }

    fun refresh() {
        launch {
            try {
                val profile = myPageRepository.getMyPage()
                updateData { it.copy(profile = profile, profileError = null) }
            } catch (e: Exception) {
                updateData { it.copy(profileError = e.message ?: "프로필을 불러오지 못했습니다") }
            }
        }
        launch {
            try {
                val detail = myPageRepository.getProfile()
                updateData { it.copy(stats = detail.stats) }
            } catch (e: Exception) { }
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
        launch {
            try {
                val updated = myPageRepository.updateSettings(request)
                updateData { it.copy(profile = it.profile?.copy(settings = updated)) }
            } catch (e: Exception) {
                updateData { it.copy(profileError = e.message ?: "설정 저장에 실패했습니다") }
            }
        }
    }
}
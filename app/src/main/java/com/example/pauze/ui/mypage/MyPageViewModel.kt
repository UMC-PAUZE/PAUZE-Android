package com.example.pauze.ui.mypage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.ui.BaseViewModel

sealed interface MyPageEffect {
    object NavigateToBack: MyPageEffect
    object NavigateToEdit: MyPageEffect
    object NavigateToAccount: MyPageEffect
}

class MyPageViewModel: BaseViewModel<MyPageEffect>() {
    var dailyReminder by mutableStateOf(true)
        private set
    var riskAlert by mutableStateOf(true)
        private set
    var breathingGuide by mutableStateOf(true)
        private set
    var stabilitySound by mutableStateOf(true)
        private set
    var offlineContent by mutableStateOf(false)
        private set

    fun onBackClick() = sendEffect(MyPageEffect.NavigateToBack)
    fun onProfileClick() = sendEffect(MyPageEffect.NavigateToEdit)
    fun onAccountInfoClick() = sendEffect(MyPageEffect.NavigateToAccount)

    fun toggleDailyReminder() {
        dailyReminder = !dailyReminder
    }
    fun toggleRiskAlert() {
        riskAlert = !riskAlert
    }
    fun toggleBreathingGuide() {
        breathingGuide = !breathingGuide
    }
    fun toggleStabilitySound() {
        stabilitySound = !stabilitySound
    }
    fun toggleOfflineContent() {
        offlineContent = !offlineContent
    }
}
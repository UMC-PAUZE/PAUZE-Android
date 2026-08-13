package com.example.pauze.ui.splash

import androidx.lifecycle.viewModelScope
import com.example.pauze.data.datasource.OnboardingDataSource
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingDataSource: OnboardingDataSource
) : BaseViewModel<Unit, Boolean?>(
    uiState = BaseUiState(data = null)
) {
    init {
        launch {
            onboardingDataSource.hasSeenOnboarding.first()
        }
    }

    fun markOnboardingSeen() {
        launch<Unit> {
            onboardingDataSource.markOnboardingSeen()
        }
    }
}
package com.example.pauze.ui.login.completed

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.BaseViewModel
import com.example.pauze.ui.login.LoginNavDestination

sealed interface SignUpCompletedEffect {
    object NavigateToHome: SignUpCompletedEffect
}

class SignUpCompletedViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel<SignUpCompletedEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    val name = savedStateHandle.toRoute<LoginNavDestination.Completed>().name
    fun navigateToHome(){
        sendEffect(SignUpCompletedEffect.NavigateToHome)
    }
}
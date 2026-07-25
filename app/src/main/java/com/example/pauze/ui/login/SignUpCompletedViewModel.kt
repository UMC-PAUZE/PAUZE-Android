package com.example.pauze.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel

sealed interface SignUpCompletedEffect {
    object NavigateToHome: SignUpCompletedEffect
}

class SignUpCompletedViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel<SignUpCompletedEffect>() {
    val name = savedStateHandle.toRoute<LoginNavDestination.Completed>().name
    fun navigateToHome(){
        sendEffect(SignUpCompletedEffect.NavigateToHome)
    }
}
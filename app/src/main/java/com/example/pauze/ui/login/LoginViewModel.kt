package com.example.pauze.ui.login

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.BaseViewModel

sealed interface LoginEffect{
    object ShowDialog: LoginEffect
    object NavigateToHome : LoginEffect
    object NavigateToAdditionalScreen: LoginEffect
    object NavigateToSignUp : LoginEffect
}
class LoginViewModel(): BaseViewModel<LoginEffect, Boolean>(
    uiState = BaseUiState(data = false)
) {
        fun loginWithKakao(){
        launch {
            // todo: 카카오 로그인 구현
            val result = true
            if (result) sendEffect(LoginEffect.NavigateToAdditionalScreen)
            else sendEffect(LoginEffect.ShowDialog)
            result
        }
    }

    fun login(email: String, pwd: String){
        launch {
            // todo: 로그인 로직 구현
            val result = false
            if (result) sendEffect(LoginEffect.NavigateToHome)
            else sendEffect(LoginEffect.ShowDialog)
            result
        }
    }

    fun toGuestMode(){
        sendEffect(LoginEffect.NavigateToHome)
    }
    fun toSignUp(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }
}

package com.example.pauze.ui.login

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.LocalLoginRequestDto
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.data.service.AuthService
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface LoginEffect{
    object ShowDialog: LoginEffect
    object NavigateToHome : LoginEffect
    object NavigateToAdditionalScreen: LoginEffect
    object NavigateToSignUp : LoginEffect
}
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
): BaseViewModel<LoginEffect, Boolean>(
    uiState = BaseUiState(data = false)
) {
    fun loginWithKakao(){
        launch(
            onSuccess = { result: Boolean ->
                updateData { result }
                sendEffect(LoginEffect.NavigateToAdditionalScreen)
            },
            onFailure = {
                updateData { false }
                sendEffect(LoginEffect.ShowDialog)
            }
        ) {
            // todo: 카카오 로그인 구현
            true
        }
    }

    fun login(email: String, pwd: String){
        TokenRepository.accessToken = null
        launch(
            onSuccess = { accessToken: String ->
                TokenRepository.accessToken = accessToken
                updateData { true }
                sendEffect(LoginEffect.NavigateToHome)
            },
            onFailure = {
                TokenRepository.accessToken = null
                updateData { false }
                sendEffect(LoginEffect.ShowDialog)
            }
        ) {
            authService.login(
                LocalLoginRequestDto(
                    email = email.trim(),
                    password = pwd
                )
            ).getOrThrow().accessToken
        }
    }

    fun toGuestMode(){
        TokenRepository.accessToken = null
        sendEffect(LoginEffect.NavigateToHome)
    }
    fun toSignUp(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }
}

package com.example.pauze.ui.login

import android.util.Log
import android.util.Log.println
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LoginState
import com.example.pauze.data.repository.AuthRepository
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
    private val repository: AuthRepository
): BaseViewModel<LoginEffect, LoginState>(
    uiState = BaseUiState(data = LoginState())
) {
        fun loginWithKakao(){
        launch {
           // todo: 카카오 로그인 구현
        }
    }

    fun login(email: String, pwd: String){
        launch {
            val result = repository.login(email, pwd)
            when(result){
                is LocalLoginResult.Success -> {
                    sendEffect(LoginEffect.NavigateToHome)
                }
                is LocalLoginResult.KakaoExists -> {
                    // todo: 추후 구현
                }
                else -> {
                    sendEffect(LoginEffect.ShowDialog)
                }
            }
        }
    }
    fun toGuestMode(){
        sendEffect(LoginEffect.NavigateToHome)
    }
    fun toSignUp(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }
}
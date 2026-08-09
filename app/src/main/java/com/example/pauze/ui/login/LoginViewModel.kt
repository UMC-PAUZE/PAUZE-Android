package com.example.pauze.ui.login

import android.content.Context
import android.util.Log
import android.util.Log.println
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.KakaoLoginRequest
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LoginState
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

sealed interface LoginEffect{
    object ShowDialog: LoginEffect
    object NavigateToHome : LoginEffect
    object NavigateToAdditionalScreen: LoginEffect
    object NavigateToSignUp : LoginEffect
}
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthRepository
): BaseViewModel<LoginEffect, LoginState>(
    uiState = BaseUiState(data = LoginState())
) {
        fun loginWithKakao(){
        launch {
            val result = repository.kakaoLogin(context)
            when(result){
                is KakaoLoginResult.Success -> {
                    sendEffect(LoginEffect.NavigateToHome)
                }
                is KakaoLoginResult.SignUp -> {
                    sendEffect(LoginEffect.NavigateToAdditionalScreen)
                }
                is KakaoLoginResult.HasLocalAccount -> {
                    // todo: 관련 다이얼로그가 필요할 듯

                }
                else -> {
                    sendEffect(LoginEffect.ShowDialog)
                }
            }
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
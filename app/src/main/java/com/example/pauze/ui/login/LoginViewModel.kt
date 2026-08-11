package com.example.pauze.ui.login

import android.content.Context
import android.util.Log
import android.util.Log.println
import androidx.compose.runtime.mutableStateOf
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
    object NavigateToHome : LoginEffect
    object NavigateToAdditionalScreen: LoginEffect
    object NavigateToSignUp : LoginEffect
    object IsLoginFailed: LoginEffect
}
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthRepository
): BaseViewModel<LoginEffect, LoginState>(
    uiState = BaseUiState(data = LoginState())
) {
        fun loginWithKakao(){
        launch(
            onSuccess = { sendEffect(LoginEffect.NavigateToHome) },
        ) {
            repository.kakaoLogin(context)
        }
    }

    fun login(email: String, pwd: String){
        launch(
            onSuccess = { sendEffect(LoginEffect.NavigateToHome) },
            onFailure = { sendEffect(LoginEffect.IsLoginFailed) }
        ) {
            repository.login(email, pwd)
        }
    }
    fun toGuestMode(){
        sendEffect(LoginEffect.NavigateToHome)
    }
    fun toSignUp(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }
}
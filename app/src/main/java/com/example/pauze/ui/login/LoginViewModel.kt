package com.example.pauze.ui.login

import android.content.Context
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

sealed interface LoginEffect{
    object NavigateToHome : LoginEffect
    object NavigateToAdditionalScreen: LoginEffect
    object NavigateToSignUp : LoginEffect
    object NavigateToLinkPage: LoginEffect
    object IsLoginFailed: LoginEffect
    object ShowLinkDialog: LoginEffect
}
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthRepository
): BaseViewModel<LoginEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    fun loginWithKakao(){
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                when(result){
                    is KakaoLoginResult.LoginSuccess -> {
                        sendEffect(LoginEffect.NavigateToHome)
                    }
                    is KakaoLoginResult.SignUp -> {
                        sendEffect(LoginEffect.NavigateToAdditionalScreen)
                    }
                    is KakaoLoginResult.HasLocalAccount -> {
                        sendEffect(LoginEffect.ShowLinkDialog)
                    }
                    else -> {
                        return@launch
                    }
                }
            }
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

    fun showLinkDialog(){
        sendEffect(LoginEffect.ShowLinkDialog)
    }
    fun toGuestMode(){
        sendEffect(LoginEffect.NavigateToHome)
    }
    fun toSignUp(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }

    fun toLinkPage(){
        sendEffect(LoginEffect.NavigateToLinkPage)
    }
}
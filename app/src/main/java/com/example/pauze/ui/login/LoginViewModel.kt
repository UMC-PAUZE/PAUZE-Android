package com.example.pauze.ui.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginEffect{
    object NavigateToHome : LoginEffect
    object NavigateToAdditionalScreen: LoginEffect
    object NavigateToSignUp : LoginEffect
    object NavigateToLinkPage: LoginEffect
}
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: AuthDataStore,
    private val repository: AuthRepository
): BaseViewModel<LoginEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoginFailed by mutableStateOf(false)
    var showLinkDialog by mutableStateOf(false)

    fun loginWithKakao(){
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                when(result){
                    is KakaoLoginResult.LoginSuccess -> {
                        viewModelScope.launch {
                            saveTokens(
                                dataStore,
                                result.accessToken,
                                result.refreshToken
                            )

                            sendEffect(LoginEffect.NavigateToHome)
                        }
                    }
                    is KakaoLoginResult.SignUp -> {
                        sendEffect(LoginEffect.NavigateToAdditionalScreen)
                    }
                    is KakaoLoginResult.HasLocalAccount -> {
                        showLinkDialog = true
                    }
                    else -> {
                        return@launch
                    }
                }
            },
            onFailure = {
                return@launch
            }
        ) {
            repository.kakaoLogin(context)
        }
    }

    fun login(email: String, pwd: String){
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                viewModelScope.launch {
                    saveTokens(
                        dataStore,
                        result.accessToken,
                        result.refreshToken
                    )

                    sendEffect(LoginEffect.NavigateToHome)
                }

            },
            onFailure = {
                isLoginFailed = true
            }
        ) {
            repository.login(email, pwd)
        }
    }

    fun updateEmail(value: String) {
        email = value
    }
    fun updatePwd(value: String) {
        password = value
    }
    fun showLinkDialog(showOrNot: Boolean){
        showLinkDialog = showOrNot
    }

    fun isLoginFailed(result: Boolean){
        isLoginFailed = result
    }

    fun toGuestMode(){
        // 로그인 기록이 있다면 로그아웃 후 홈 화면으로 진입
        launch(
            onSuccess = {
                viewModelScope.launch {
                    // 토큰 초기화
                    clearToken(dataStore)
                    sendEffect(LoginEffect.NavigateToHome)
                }
            }
        ) {
            repository.logout()
        }
    }
    fun toSignUp(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }

    fun toLinkPage(){
        sendEffect(LoginEffect.NavigateToLinkPage)
    }
}
package com.example.pauze.ui.login

import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.sql.DriverManager.println

sealed interface LoginEffect{
    object ShowDialog: LoginEffect
    object NavigateToHome : LoginEffect
    object NavigateToSignUp : LoginEffect
}
class LoginViewModel: BaseViewModel<LoginEffect>() {
    fun loginWithKakao(){
        launch {
            // todo: 카카오 로그인 구현
            println("카카오 로그인 클릭")
        }
    }

    fun login(email: String, pwd: String){
        launch {
            // todo: 로그인 로직 구현
            println("email: $email, password: $pwd")
            val isSuccess = false
            if(isSuccess){
                sendEffect(LoginEffect.NavigateToHome)
            } else {
                sendEffect(LoginEffect.ShowDialog)
            }
        }
    }

    fun toGuestMode(){
        sendEffect(LoginEffect.NavigateToSignUp)
    }
}
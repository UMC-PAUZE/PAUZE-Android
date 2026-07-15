package com.example.pauze.ui.login

import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LoginViewModel: BaseViewModel<Boolean>() {
    private val _isSuccess = MutableSharedFlow<Boolean>()
    val isSuccess = _isSuccess.asSharedFlow()
    fun loginWithKakao(){
        // todo: 카카오 로그인 구현
        println("카카오 로그인 클릭")
    }

    fun login(email: String, pwd: String){
        launch {
            // todo: 로그인 로직 구현
            println("로그인 클릭 - email: $email, password: $pwd")
            _isSuccess.emit(false)
        }
    }
}
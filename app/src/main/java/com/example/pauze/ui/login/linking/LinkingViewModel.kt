package com.example.pauze.ui.login.linking

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.BaseViewModel

sealed interface LinkingEffect {
    object RestartVerifTimer: LinkingEffect
    object BackStack: LinkingEffect
    object NavigateToLogin: LinkingEffect
}

class LinkingViewModel: BaseViewModel<LinkingEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var email by mutableStateOf("")
    var phase by mutableIntStateOf(0)
    var code by mutableStateOf("")
    var isVerified by mutableStateOf(true)
    var time by mutableStateOf("00:00")
    private var countDownTimer: CountDownTimer? = null

    // todo: 데이터 연결 시 uiState로 처리
    var isEmailNoExisted by mutableStateOf(true)
    fun checkEmailAlreadyExistOrNot(): Boolean = isEmailNoExisted
    fun toggleEmailExist(){
        isEmailNoExisted = !isEmailNoExisted
    }

    fun updatePhase(){
        phase = phase + 1
        if(phase == 1){
            startTimer()
        }
    }

    fun startTimer(){
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(300000L, 1000L){
            override fun onFinish() {
                time = "00:00"
            }

            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = millisUntilFinished / 1000
                val minuteLeft = totalSeconds / 60
                val secondLeft = totalSeconds % 60
                val formattedSeconds = if(secondLeft < 10) "0${secondLeft}" else secondLeft
                time = "0${minuteLeft} : $formattedSeconds"
            }
        }.start()
    }

    fun sendEffectForTimer(){
        sendEffect(LinkingEffect.RestartVerifTimer)
    }

    fun backStack(){
        sendEffect(LinkingEffect.BackStack)
    }
    fun navigateToLogin(){
        sendEffect(LinkingEffect.NavigateToLogin)
    }
}
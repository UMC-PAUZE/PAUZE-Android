package com.example.pauze.ui.login.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel
import kotlinx.datetime.LocalDate
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableIntStateOf
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.login.LoginNavDestination

sealed interface SignUpEffect {
    object RestartVerifTimer: SignUpEffect
    object BackStack: SignUpEffect
    object NavigateToPolicy: SignUpEffect
    object NavigateToCompleted: SignUpEffect
    object ShowBirthdayPicker: SignUpEffect
}

class SignUpViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel<SignUpEffect, Unit>(
    uiState = BaseUiState(data = Unit)
){
    val initialAgreed = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreed
    var isAgreed by mutableStateOf(initialAgreed)
        private set

    var phase by mutableIntStateOf(0)
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var birthday by mutableStateOf<LocalDate?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var pwdCheck by mutableStateOf("")
    var showBirthdayPicker by mutableStateOf(false)
    var verifCode by mutableStateOf("")
    var time by mutableStateOf("00:00")
    private var countDownTimer: CountDownTimer? = null

    // todo: 데이터 연결 시 uiState로 처리
    var isEmailNoExisted by mutableStateOf(true)
    fun checkEmailAlreadyExistOrNot(): Boolean = isEmailNoExisted
    fun toggleEmailExist(){
        isEmailNoExisted = !isEmailNoExisted
    }
    fun checkVerifCodeRight(): Boolean = verifCode == "64359"

    fun updateIsAgreed(isAgreed: Boolean){
        this.isAgreed = isAgreed
    }

    fun updatePhase(){
        phase = phase + 1
        if(phase == 2){
            Handler(Looper.getMainLooper()).post {
                startTimer()
            }
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
        sendEffect(SignUpEffect.RestartVerifTimer)
    }
    fun backStack(){
        sendEffect(SignUpEffect.BackStack)
    }

    fun signUp(){
        sendEffect(SignUpEffect.NavigateToCompleted)
    }

    fun checkPolicy(){
        sendEffect(SignUpEffect.NavigateToPolicy)
    }
    fun showBirthdayPicker(){
        sendEffect(SignUpEffect.ShowBirthdayPicker)
    }
}
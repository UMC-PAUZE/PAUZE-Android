package com.example.pauze.ui.login.kakao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableIntStateOf
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.login.LoginNavDestination
import com.example.pauze.ui.login.signup.SignUpEffect

sealed interface KakaoSignUpEffect {
    object RestartVerifTimer: KakaoSignUpEffect
    object BackStack: KakaoSignUpEffect
    data class NavigateToPolicy(val isTermOfUse: Boolean): KakaoSignUpEffect
    object NavigateToCompleted: KakaoSignUpEffect
}

class KakaoSignUpViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel<KakaoSignUpEffect, Unit>(
    uiState = BaseUiState(data = Unit)
){
    private val isInitiallyAgreedToTerm = savedStateHandle.toRoute<LoginNavDestination.Kakao>().isAgreedToTerm
    var isAgreedToTerm by mutableStateOf(isInitiallyAgreedToTerm)
        private set
    private val isInitiallyAgreedToPolicy = savedStateHandle.toRoute<LoginNavDestination.Kakao>().isAgreedToPolicy
    var isAgreedToPolicy by mutableStateOf(isInitiallyAgreedToPolicy)
        private set

    var phase by mutableIntStateOf(0)
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var email by mutableStateOf("")
    var verifCode by mutableStateOf("")
    var time by mutableStateOf("00:00")
    private var countDownTimer: CountDownTimer? = null

    // todo: 데이터 연결 시 uiState로 처리
    var isEmailNoExisted by mutableStateOf(true)
    fun checkEmailAlreadyExistOrNot(): Boolean = isEmailNoExisted
    fun toggleEmailExist(){
        isEmailNoExisted = !isEmailNoExisted
    }
    fun checkVerifCodeRight(): Boolean = verifCode == "643590"

    fun updateIsAgreedToTerm(isAgreed: Boolean){
        isAgreedToTerm = isAgreed
    }

    fun updateIsAgreedToPolicy(isAgreed: Boolean){
        isAgreedToPolicy = isAgreed
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
        sendEffect(KakaoSignUpEffect.RestartVerifTimer)
    }
    fun backStack(){
        sendEffect(KakaoSignUpEffect.BackStack)
    }

    fun signUp(){
        sendEffect(KakaoSignUpEffect.NavigateToCompleted)
    }

    fun checkPolicy(isTermOfUse: Boolean){
        sendEffect(KakaoSignUpEffect.NavigateToPolicy(isTermOfUse))
    }
}
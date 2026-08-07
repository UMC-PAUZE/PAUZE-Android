package com.example.pauze.ui.login.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel
import kotlinx.datetime.LocalDate
import android.os.CountDownTimer
import androidx.compose.runtime.mutableIntStateOf
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.LocalSignUpRequest
import com.example.pauze.data.model.LocalSignUpResult
import com.example.pauze.data.model.SignUpState
import com.example.pauze.data.model.TermsAgreement
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.ui.login.LoginNavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface SignUpEffect {
    object RestartVerifTimer: SignUpEffect
    object BackStack: SignUpEffect
    data class NavigateToPolicy(val isTermOfUse: Boolean): SignUpEffect
    object NavigateToCompleted: SignUpEffect
    object ShowBirthdayPicker: SignUpEffect
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository
): BaseViewModel<SignUpEffect, SignUpState>(
    uiState = BaseUiState(data = SignUpState())
){
    private val isInitiallyAgreedToTerm = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreedToTerm
    var isAgreedToTerm by mutableStateOf(isInitiallyAgreedToTerm)
        private set
    private val isInitiallyAgreedToPolicy = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreedToPolicy
    var isAgreedToPolicy by mutableStateOf(isInitiallyAgreedToPolicy)
        private set

    var phase by mutableIntStateOf(0)
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var birthday by mutableStateOf<LocalDate?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var pwdCheck by mutableStateOf("")
    var showBirthdayPicker by mutableStateOf(false)
    var code by mutableStateOf("")
    var isVerified by mutableStateOf(false)
    var time by mutableStateOf("00:00")
    private var countDownTimer: CountDownTimer? = null

    // todo: 데이터 연결 시 uiState로 처리
    var isEmailNoExisted by mutableStateOf(true)
    fun checkEmailAlreadyExistOrNot(): Boolean = isEmailNoExisted
    fun toggleEmailExist(){
        isEmailNoExisted = !isEmailNoExisted
    }
    fun checkVerifCodeRight(): Boolean = code == "643590"
    fun signUp(){
        launch {
            val result = repository.localSignUp(
                LocalSignUpRequest(
                    name = name,
                    nickname = nickname,
                    birth = birthday.toString().replace("-", ""),
                    email = email,
                    password = password,
                    termAgreement = listOf<TermsAgreement>(
                        TermsAgreement(0, isAgreedToTerm),
                        TermsAgreement(1, isAgreedToPolicy)
                    )
                )
            )
            if(result != null){
                when(result){
                    is LocalSignUpResult.Success -> {
                        verifyEmail()
                        if(isVerified){
                            sendEffect(SignUpEffect.NavigateToCompleted)
                        }
                    }
                    is LocalSignUpResult.KakaoExists -> {
                        // todo: 이후 구현
                    }
                }
            }
        }
    }

    fun verifyEmail(){
        launch {
            val result = repository.verifyEmail(email, code)
            isVerified = result != null
        }
    }
    fun updateIsAgreedToTerm(isAgreed: Boolean){
        isAgreedToTerm = isAgreed
    }

    fun updateIsAgreedToPolicy(isAgreed: Boolean){
        isAgreedToPolicy = isAgreed
    }

    fun updatePhase(){
        phase = phase + 1
        if(phase == 2){
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
        sendEffect(SignUpEffect.RestartVerifTimer)
    }
    fun backStack(){
        sendEffect(SignUpEffect.BackStack)
    }

    fun checkPolicy(isTermOfUse: Boolean){
        sendEffect(SignUpEffect.NavigateToPolicy(isTermOfUse))
    }
    fun showBirthdayPicker(){
        sendEffect(SignUpEffect.ShowBirthdayPicker)
    }
}
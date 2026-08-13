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
import com.example.pauze.data.model.SendCodeForSignUpResult
import com.example.pauze.data.model.TermAgreement
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.ui.login.LoginNavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface SignUpEffect {
    object RestartVerifTimer: SignUpEffect
    object BackStack: SignUpEffect
    data class NavigateToPolicy(val isTermOfUse: Boolean): SignUpEffect
    object NavigateToCompleted: SignUpEffect
    object NavigateToLink: SignUpEffect
    object ShowLinkDialog: SignUpEffect
    object ShowBirthdayPicker: SignUpEffect
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository
): BaseViewModel<SignUpEffect, Unit>(
    uiState = BaseUiState(data = Unit)
){
    var phase by mutableIntStateOf(0)

    // phase 0
    var email by mutableStateOf("")
    var isEmailExists by mutableStateOf<Boolean?>(null)
    var emailAvailableStatus by mutableStateOf("")

    // phase 1
    var code by mutableStateOf("")
    var time by mutableStateOf("00:00")
    private var countDownTimer: CountDownTimer? = null
    var isVerified by mutableStateOf<Boolean?>(null)
    var showLinkDialog by mutableStateOf(false)

    // phase 2
    var password by mutableStateOf("")
    var pwdCheck by mutableStateOf("")

    // phase 3
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var isNicknameAvailable by mutableStateOf<Boolean?>(null)

    // phase 4
    var birthday by mutableStateOf<LocalDate?>(null)
    var showBirthdayPicker by mutableStateOf(false)
    private val isInitiallyAgreedToTerm = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreedToTerm
    var isAgreedToTerm by mutableStateOf(isInitiallyAgreedToTerm)
        private set
    private val isInitiallyAgreedToPolicy = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreedToPolicy
    var isAgreedToPolicy by mutableStateOf(isInitiallyAgreedToPolicy)
        private set

    fun checkEmailAvailable(){
        launch(
            onSuccess = { result ->
                if(result == null){
                    return@launch
                }
                when(result.status){
                    "AVAILABLE" -> {
                        isEmailExists = false
                    }
                    "LOCAL" -> {
                        isEmailExists = true
                    }
                    "KAKAO" -> {
                        isEmailExists = true
                    }
                }
                emailAvailableStatus = result.status
            },
            onFailure = {
                return@launch
            }
        ) {
            repository.isEmailAvailable(email)
        }
    }

    fun sendCodeForSignUp(){
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                when(result){
                    is SendCodeForSignUpResult.KakaoExists -> {
                        isVerified = true
                        sendEffect(SignUpEffect.ShowLinkDialog)
                    }
                    is SendCodeForSignUpResult.Success -> {
                        isVerified = true
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
            repository.sendCodeForSignUp(email)
        }
    }

    fun verifyEmail(){
        launch(
            onSuccess = { result ->
                if(result == null){
                    isVerified = false
                    return@launch
                }
                isVerified = true
            },
            onFailure = {
                return@launch
            }
        ) {
            repository.verifyEmail(email, code)
        }
    }

    fun checkNicknameAvailable(){
        launch(
            onSuccess = { result ->
                if(result == null){
                    isNicknameAvailable = false
                    return@launch
                }
                isNicknameAvailable = true
                nickname = result.nickname
            },
            onFailure = {
                return@launch
            }
        ) {
            repository.isNicknameAvailable(nickname)
        }
    }

    fun signUp(){
        launch(
            onSuccess = {
                sendEffect(SignUpEffect.NavigateToCompleted)
            }
        ) {
            repository.localSignUp(
                name = name,
                nickname = nickname,
                birth = birthday.toString().replace("-", ""),
                email = email,
                password = password,
                termAgreements = listOf(
                    TermAgreement(2, isAgreedToTerm),
                    TermAgreement(3, isAgreedToPolicy)
                )
            )
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
        sendEffect(SignUpEffect.RestartVerifTimer)
    }
    fun backStack(){
        sendEffect(SignUpEffect.BackStack)
    }
    fun checkPolicy(isTermOfUse: Boolean){
        sendEffect(SignUpEffect.NavigateToPolicy(isTermOfUse))
    }
    fun navigateToLink(){
        sendEffect(SignUpEffect.NavigateToLink)
    }
    fun showLinkDialog(){
        sendEffect(SignUpEffect.ShowLinkDialog)
    }
    fun showBirthdayPicker(){
        sendEffect(SignUpEffect.ShowBirthdayPicker)
    }
}
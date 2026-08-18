package com.example.pauze.ui.login.signup

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel
import kotlinx.datetime.LocalDate
import android.os.CountDownTimer
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.SendCodeForSignUpResult
import com.example.pauze.data.model.TermAgreement
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.login.LoginNavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SignUpEffect {
    object RestartVerifTimer: SignUpEffect
    object BackStack: SignUpEffect
    data class NavigateToPolicy(val isTermOfUse: Boolean): SignUpEffect
    object NavigateToCompleted: SignUpEffect
    object NavigateToHome: SignUpEffect
    object ShowLinkDialog: SignUpEffect
    object ShowBirthdayPicker: SignUpEffect
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStore: AuthDataStore,
    private val repository: AuthRepository
): BaseViewModel<SignUpEffect, Unit>(
    uiState = BaseUiState(data = Unit)
){
    var phase by mutableIntStateOf(0)

    // phase 0
    var email by mutableStateOf("")
    var isEmailExists by mutableStateOf<Boolean?>(null)
    var isKakaoAccountExists by mutableStateOf(false)
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
                        isKakaoAccountExists = true
                        sendEffect(SignUpEffect.ShowLinkDialog)
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
                        sendEffect(SignUpEffect.ShowLinkDialog)
                    }
                    is SendCodeForSignUpResult.Success -> {
                        println("인증 코드 전송됨")
                    }
                    else -> {
                        println("인증코드 전송 실패")
                        return@launch
                    }
                }
            },
            onFailure = {
                println("인증코드 전송 실패")
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
                isVerified = false
                return@launch
            }
        ) {
            repository.verifyEmail(email, code)
        }
    }

    fun checkNicknameAvailable(){
        launch(
            onSuccess = { result ->
                if(result == null || !result.available){
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

    fun linkAccount() {
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                viewModelScope.launch {
                    // 토큰 저장
                    dataStore.saveAccessToken(result.accessToken)
                    dataStore.saveRefreshToken(result.refreshToken)
                    TokenRepository.updateAccessToken(result.accessToken)

                    navigateToHome()
                }
            },
            onFailure = {
                return@launch
            }
        ) {
            val kakaoAccessToken = dataStore.getKakaoAccessToken() ?: ""
            repository.linkAccount("KAKAO_TO_LOCAL", kakaoAccessToken, email, password)
        }
    }

    fun signUp(){
        launch(
            onSuccess = { result ->
                if(result == null) {
                    return@launch
                }
                viewModelScope.launch {
                    // 토큰 저장
                    dataStore.saveAccessToken(result.accessToken)
                    dataStore.saveRefreshToken(result.refreshToken)
                    TokenRepository.updateAccessToken(result.accessToken)

                    sendEffect(SignUpEffect.NavigateToCompleted)
                }
            },
            onFailure = {
                return@launch
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

    fun kakaoLogin(context: Context) {
        launch(
            onSuccess = {
                confirmKakaoAccount()
                updatePhase()
            },
            onFailure = {
                return@launch
            }
        ) {
            val kakaoAccessToken = dataStore.kakaoLoginAndGetToken(context).firstOrNull() ?: return@launch
            dataStore.saveKakaoAccessToken(kakaoAccessToken)
        }
    }

    fun confirmKakaoAccount(){
        launch(
            onSuccess = { result ->
                if(result == null) {
                    println("인증코드 전송 실패")
                }
                println("인증코드 전송")
            },
            onFailure = {
                println("인증코드 전송 실패")
                return@launch
            }
        ) {
            repository.confirmKakaoAccount(email)
        }
    }

    fun updateCode(value: String) {
        code = value
    }

    fun updateIsVerified(value: Boolean?) {
        isVerified = value
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
    fun navigateToHome(){
        sendEffect(SignUpEffect.NavigateToHome)
    }
    fun showBirthdayPicker(){
        sendEffect(SignUpEffect.ShowBirthdayPicker)
    }
}
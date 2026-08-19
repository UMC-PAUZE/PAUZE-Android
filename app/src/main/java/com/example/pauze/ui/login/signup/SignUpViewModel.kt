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
import com.example.pauze.ui.login.saveTokens
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

    // 페이즈 1
    var email by mutableStateOf("")
    var isEmailExists by mutableStateOf<Boolean?>(null)
    var isKakaoAccountExists by mutableStateOf(false)

    // 페이즈 2
    var code by mutableStateOf("")
    var time by mutableStateOf("00:00")
    private var countDownTimer: CountDownTimer? = null
    var isVerified by mutableStateOf<Boolean?>(null)
    var showLinkDialog by mutableStateOf(false)

    // 페이즈 3
    var password by mutableStateOf("")
    var pwdCheck by mutableStateOf("")

    // 페이즈 4
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var isNicknameAvailable by mutableStateOf<Boolean?>(null)

    // 페이즈 5
    var birthday by mutableStateOf<LocalDate?>(null)
    var showBirthdayPicker by mutableStateOf(false)
    private val isInitiallyAgreedToTerm = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreedToTerm
    var isAgreedToTerm by mutableStateOf(isInitiallyAgreedToTerm)
        private set
    private val isInitiallyAgreedToPolicy = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreedToPolicy
    var isAgreedToPolicy by mutableStateOf(isInitiallyAgreedToPolicy)
        private set

    // 이메일 사용 가능 여부
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
                        showLinkDialog(true)
                    }
                }
            },
            onFailure = {
                return@launch
            }
        ) {
            repository.isEmailAvailable(email)
        }
    }

    // 회원가입 이메일 인증코드 발송
    fun sendCodeForSignUp(){
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                when(result){
                    is SendCodeForSignUpResult.KakaoExists -> {
                        println("카카오 계정 존재")
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

    // 이메일 인증코드 검증
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

    // 닉네임 사용 가능 여부
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
    // 로컬 회원가입
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
    // 클라이언트 카카오 로그인
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

    // 카카오 계정 확인(로컬 가입 연동)
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

    // 계정 연동
    fun linkAccount() {
        launch(
            onSuccess = { result ->
                if(result == null) return@launch
                viewModelScope.launch {
                    saveTokens(
                        dataStore,
                        result.accessToken,
                        result.refreshToken
                    )
                    navigateToHome()
                }
            },
            onFailure = {
                return@launch
            }
        ) {
            val kakaoAccessToken = dataStore.getKakaoAccessToken() ?: return@launch null
            repository.linkAccount("KAKAO_TO_LOCAL", kakaoAccessToken, email, password)
        }
    }

    // 데이터 업데이트 함수
    fun updateEmail(value: String) {
        email = value
        isKakaoAccountExists = false
        showLinkDialog(false)
    }
    fun updateEmailExists(value: Boolean?){
        isEmailExists = value
    }
    fun updateCode(value: String) {
        code = value
    }
    fun updateName(value: String) {
        name = value
    }
    fun updateNickname(value: String) {
        nickname = value
    }
    fun updateNicknameAvailability(value: Boolean?){
        isNicknameAvailable = value
    }
    fun updatePwd(value: String){
        password = value
    }
    fun updatePwdCheck(value: String){
        pwdCheck = value
    }
    fun updateBirthday(value: LocalDate?){
        birthday = value
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
    fun showLinkDialog(value: Boolean){
        showLinkDialog = value
    }
    fun showBirthdayPicker(value: Boolean){
        showBirthdayPicker = value
    }
    // 이펙트 전송
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
}
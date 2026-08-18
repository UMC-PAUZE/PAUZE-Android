package com.example.pauze.ui.login.kakao

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.datastore.dataStore
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.TermAgreement
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.login.LoginNavDestination
import com.example.pauze.ui.login.saveTokens
import com.example.pauze.ui.login.signup.SignUpEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

sealed interface KakaoSignUpEffect {
    object BackStack: KakaoSignUpEffect
    data class NavigateToPolicy(val isTermOfUse: Boolean): KakaoSignUpEffect
    object NavigateToCompleted: KakaoSignUpEffect
}

@HiltViewModel
class KakaoSignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStore: AuthDataStore,
    private val repository: AuthRepository
): BaseViewModel<KakaoSignUpEffect, Unit>(
    uiState = BaseUiState(data = Unit)
){
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var isNicknameAvailable by mutableStateOf<Boolean?>(null)
    var birthday by mutableStateOf<LocalDate?>(null)
    var showBirthdayPicker by mutableStateOf(false)
    private val isInitiallyAgreedToTerm = savedStateHandle.toRoute<LoginNavDestination.Kakao>().isAgreedToTerm
    var isAgreedToTerm by mutableStateOf(isInitiallyAgreedToTerm)
        private set
    private val isInitiallyAgreedToPolicy = savedStateHandle.toRoute<LoginNavDestination.Kakao>().isAgreedToPolicy
    var isAgreedToPolicy by mutableStateOf(isInitiallyAgreedToPolicy)
        private set

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

    // 카카오 회원가입
    fun kakaoSignUp(){
        launch(
            onSuccess = { result ->
                if(result == null) {
                    return@launch
                }
                viewModelScope.launch {
                    saveTokens(
                        dataStore,
                        result.accessToken,
                        result.refreshToken
                    )
                    sendEffect(KakaoSignUpEffect.NavigateToCompleted)
                }
            },
            onFailure = {
                return@launch
            }
        ) {
            val accessToken = dataStore.getKakaoAccessToken() ?: return@launch null
            repository.kakaoSignUp(
                name = name,
                nickname = nickname,
                birth = birthday.toString().replace("-", ""),
                kakaoAccessToken = accessToken,
                termAgreements = listOf(
                    TermAgreement(2, isAgreedToTerm),
                    TermAgreement(3, isAgreedToPolicy)
                )
            )
        }
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
    fun updateBirthday(value: LocalDate?) {
        birthday = value
    }

    fun updateIsAgreedToTerm(isAgreed: Boolean){
        isAgreedToTerm = isAgreed
    }

    fun updateIsAgreedToPolicy(isAgreed: Boolean){
        isAgreedToPolicy = isAgreed
    }

    fun showBirthdayPicker(value: Boolean) {
        showBirthdayPicker = value
    }

    fun backStack(){
        sendEffect(KakaoSignUpEffect.BackStack)
    }

    fun checkPolicy(isTermOfUse: Boolean){
        sendEffect(KakaoSignUpEffect.NavigateToPolicy(isTermOfUse))
    }
}
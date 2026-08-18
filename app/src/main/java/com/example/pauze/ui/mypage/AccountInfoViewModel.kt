package com.example.pauze.ui.mypage

import androidx.datastore.dataStore
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.model.AccountInfoState
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AccountInfoEffect {
    object NavigateToBack : AccountInfoEffect
    object ShowWithdrawDialog : AccountInfoEffect
    object NavigateToLogout : AccountInfoEffect
    object NavigateToWithdraw : AccountInfoEffect
}

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val authRepository: AuthRepository,
    private val myPageRepository: MyPageRepository
) : BaseViewModel<AccountInfoEffect, AccountInfoState>(
    uiState = BaseUiState(data = AccountInfoState())
) {

    init {
        launch {
            val profile = myPageRepository.getProfile()
            uiState.value.data.copy(
                email = profile.email,
                joinedAt = profile.joinedAt,
                socialTypes = profile.socialTypes
            )
        }
    }

    fun onBackClick() = sendEffect(AccountInfoEffect.NavigateToBack)
    fun onLogoutClick(){
        launch(
            onSuccess = {
                viewModelScope.launch {
                    // 토큰 초기화
                    authDataStore.clearToken()
                    TokenRepository.updateAccessToken(null)

                    sendEffect(AccountInfoEffect.NavigateToLogout)
                }
            }
        ) {
            authRepository.logout()
        }
    }
    fun onWithdrawClick() = sendEffect(AccountInfoEffect.ShowWithdrawDialog)
    fun onWithdrawConfirm() {
        launch(
            onSuccess = { sendEffect(AccountInfoEffect.NavigateToWithdraw) },
            onFailure = { e -> updateData { it.copy(loadError = e.message ?: "탈퇴 요청에 실패했습니다") } }
        ) {
            myPageRepository.withdraw()
        }
    }
}
package com.example.pauze.ui.mypage

import com.example.pauze.data.model.AccountInfoState
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface AccountInfoEffect {
    object NavigateToBack : AccountInfoEffect
    object ShowWithdrawDialog : AccountInfoEffect
    object NavigateToLogout : AccountInfoEffect
    object NavigateToWithdraw : AccountInfoEffect
}

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : BaseViewModel<AccountInfoEffect, AccountInfoState>(
    uiState = BaseUiState(data = AccountInfoState())
) {

    init {
        launch {
            try{
                val profile = myPageRepository.getProfile()
                updateData {
                    it.copy(
                        email = profile.email,
                        joinedAt = profile.joinedAt,
                        socialTypes = profile.socialTypes
                    )
                }
            }catch (e: Exception){
                updateData { it.copy(loadError = e.message ?: "정보를 불러오지 못했습니다.") }
            }
        }
    }

    fun onBackClick() = sendEffect(AccountInfoEffect.NavigateToBack)
    fun onLogoutClick() = sendEffect(AccountInfoEffect.NavigateToLogout)
    fun onWithdrawClick() = sendEffect(AccountInfoEffect.ShowWithdrawDialog)
    fun onWithdrawConfirm() {
        launch {
            try {
                myPageRepository.withdraw()
                sendEffect(AccountInfoEffect.NavigateToWithdraw)
            }catch (e: Exception){
                updateData { it.copy(loadError = e.message ?: "탈퇴에 실패했습니다.") }
            }
        }
    }
}
package com.example.pauze.ui.mypage

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.ui.BaseViewModel

sealed interface AccountInfoEffect {
    object NavigateToBack : AccountInfoEffect
    object ShowWithdrawDialog : AccountInfoEffect
    object NavigateToLogout : AccountInfoEffect
    object NavigateToWithdraw : AccountInfoEffect
}

class AccountInfoViewModel : BaseViewModel<AccountInfoEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    fun onBackClick() = sendEffect(AccountInfoEffect.NavigateToBack)
    fun onLogoutClick() = sendEffect(AccountInfoEffect.NavigateToLogout)
    fun onWithdrawClick() = sendEffect(AccountInfoEffect.ShowWithdrawDialog)
    fun onWithdrawConfirm() = sendEffect(AccountInfoEffect.NavigateToWithdraw)
}
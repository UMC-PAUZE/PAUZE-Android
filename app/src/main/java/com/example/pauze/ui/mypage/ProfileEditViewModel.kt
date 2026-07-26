package com.example.pauze.ui.mypage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.ui.BaseViewModel
import kotlinx.datetime.LocalDate

sealed interface ProfileEditEffect {
    object NavigateToBack : ProfileEditEffect
}

class ProfileEditViewModel : BaseViewModel<ProfileEditEffect>() {
    var nickname by mutableStateOf("")
    var bio by mutableStateOf("")
    var birthday by mutableStateOf<LocalDate?>(null)

    fun onBackClick() = sendEffect(ProfileEditEffect.NavigateToBack)
}
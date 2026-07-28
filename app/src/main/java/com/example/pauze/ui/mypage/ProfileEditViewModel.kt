package com.example.pauze.ui.mypage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.UserProfileRepository
import com.example.pauze.ui.BaseViewModel

sealed interface ProfileEditEffect {
    object NavigateToBack : ProfileEditEffect
}

class ProfileEditViewModel : BaseViewModel<ProfileEditEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var nickname by mutableStateOf(UserProfileRepository.nickname)
    var bio by mutableStateOf(UserProfileRepository.bio)
    var birthday by mutableStateOf(UserProfileRepository.birthday)
    var profileImageUri by mutableStateOf(UserProfileRepository.profileImageUri)

    fun onBackClick() = sendEffect(ProfileEditEffect.NavigateToBack)

    fun onSaveClick() {
        UserProfileRepository.nickname = nickname
        UserProfileRepository.bio = bio
        UserProfileRepository.birthday = birthday
        UserProfileRepository.profileImageUri = profileImageUri
        sendEffect(ProfileEditEffect.NavigateToBack)
    }
}
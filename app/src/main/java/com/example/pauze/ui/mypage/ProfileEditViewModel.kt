package com.example.pauze.ui.mypage

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.UserProfileRepository
import com.example.pauze.ui.BaseViewModel

sealed interface ProfileEditEffect {
    object NavigateToBack : ProfileEditEffect
}

class ProfileEditViewModel : BaseViewModel<ProfileEditEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var nickname
        get() = UserProfileRepository.nickname
        set(value) { UserProfileRepository.nickname = value }

    var bio
        get() = UserProfileRepository.bio
        set(value) { UserProfileRepository.bio = value }

    var birthday
        get() = UserProfileRepository.birthday
        set(value) { UserProfileRepository.birthday = value }

    var profileImageUri
        get() = UserProfileRepository.profileImageUri
        set(value) { UserProfileRepository.profileImageUri = value }

    fun onBackClick() = sendEffect(ProfileEditEffect.NavigateToBack)

    fun onSaveClick() {
        sendEffect(ProfileEditEffect.NavigateToBack)
    }
}
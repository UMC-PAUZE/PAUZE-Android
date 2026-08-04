package com.example.pauze.ui.mypage

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.MyPageState
import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.data.repository.UserProfileRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

sealed interface ProfileEditEffect {
    object NavigateToBack : ProfileEditEffect
}

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<ProfileEditEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var nickname by mutableStateOf("")
    var bio by mutableStateOf("")
    var profileImageUrl by mutableStateOf<String?>(null)
    var newProfileImageUri by mutableStateOf<Uri?>(null)
    var loadError by mutableStateOf<String?>(null)
    val birthday: kotlinx.datetime.LocalDate?
        get() = UserProfileRepository.birthday

    init {
        launch {
            try {
                val profile = myPageRepository.getProfile()
                nickname = profile.nickname
                bio = profile.introduction ?: ""
                profileImageUrl = profile.profileImageUrl
            } catch (e:Exception){
                loadError = e.message ?: "프로필을 불러오지 못했습니다."
            }
        }
    }

    fun onBackClick() = sendEffect(ProfileEditEffect.NavigateToBack)

    fun onSaveClick() {
        launch {
            try {
                val imageFile = newProfileImageUri?.let { uriToFile(it) }
                myPageRepository.updateProfile(
                    nickname = nickname,
                    introduction = bio,
                    profileImage = imageFile
                )
                sendEffect(ProfileEditEffect.NavigateToBack)
            } catch (e: Exception) {
                loadError = e.message ?: "저장에 실패했습니다."
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val tempFile = File.createTempFile("profile_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output -> inputStream.copyTo(output) }
        return tempFile
    }
}
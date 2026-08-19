package com.example.pauze.ui.mypage

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.ui.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

private const val MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024L
private val ALLOWED_IMAGE_MIME_TYPES = setOf("image/png", "image/jpeg")

sealed interface ProfileEditEffect {
    object NavigateToBack : ProfileEditEffect
}

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<ProfileEditEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var nickname by mutableStateOf("")
        private set
    private var originalNickname: String = ""
    var isNicknameAvailable by mutableStateOf<Boolean?>(null)
        private set
    var bio by mutableStateOf("")
        private set
    var profileImageUrl by mutableStateOf<String?>(null)
    var newProfileImageUri by mutableStateOf<Uri?>(null)
    var birthday by mutableStateOf<String?>(null)
        private set
    var loadError by mutableStateOf<String?>(null)
    var imageError by mutableStateOf<String?>(null)
        private set

    // 확인 다이얼로그
    var showPhotoUploadedDialog by mutableStateOf(false)
        private set
    var showSavedDialog by mutableStateOf(false)
        private set

    init {
        launch {
            val profile = myPageRepository.getProfile()
            nickname = profile.nickname
            originalNickname = profile.nickname
            bio = profile.introduction ?: ""
            profileImageUrl = profile.profileImageUrl
            birthday = profile.birth.takeIf { it.length == 8 }
                ?.let { "${it.take(4)}.${it.substring(4, 6)}.${it.substring(6, 8)}" }
        }
    }

    fun onBackClick() = sendEffect(ProfileEditEffect.NavigateToBack)

    fun updateNickname(newNickname: String) {
        nickname = newNickname
        isNicknameAvailable = null
    }

    fun checkNicknameAvailable(){
        launch(
            onSuccess = { result -> isNicknameAvailable = result?.available ?: false },
            onFailure = { isNicknameAvailable = false }
        ) {
            authRepository.isNicknameAvailable(nickname)
        }
    }

    val isNicknameValid: Boolean
        get() = nickname == originalNickname || isNicknameAvailable == true

    val isNicknameChanged: Boolean
        get() = nickname != originalNickname

    fun updateBio(newBio: String) { bio = newBio }

    fun onImagePicked(uri: Uri) {
        val mimeType = context.contentResolver.getType(uri)
        if (mimeType !in ALLOWED_IMAGE_MIME_TYPES) {
            imageError = "PNG, JPEG 형식의 이미지만 업로드할 수 있습니다."
            return
        }
        val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.use { it.statSize } ?: 0L
        if (fileSize > MAX_IMAGE_SIZE_BYTES) {
            imageError = "이미지는 5MB 이하로 업로드해주세요."
            return
        }
        imageError = null
        newProfileImageUri = uri
        showPhotoUploadedDialog = true
    }
    fun dismissPhotoUploadedDialog() { showPhotoUploadedDialog = false }

    fun onSaveClick() {
        if (uiState.value.isLoading) return
        launch(
            onSuccess = { showSavedDialog = true },
            onFailure = { e ->
                loadError = (e as? HttpException)?.let(::parseErrorMessage) ?: e.message ?: "저장에 실패했습니다."
            }
        ) {
            val imageFile = newProfileImageUri?.let { uriToFile(it) }
            myPageRepository.updateProfile(nickname = nickname, introduction = bio, profileImage = imageFile)
        }
    }
    fun onSavedDialogConfirm(){
        showSavedDialog = false
        sendEffect(ProfileEditEffect.NavigateToBack)
    }

    private fun uriToFile(uri: Uri): File {
        val extension = if (context.contentResolver.getType(uri) == "image/png") "png" else "jpg"
        val tempFile = File.createTempFile("profile_", ".$extension", context.cacheDir)
        context.contentResolver.openInputStream(uri)!!.use { input ->
            tempFile.outputStream().use { output -> input.copyTo(output) }
        }
        return tempFile
    }

    private fun parseErrorMessage(e: HttpException): String? {
        return try {
            val errorBody = e.response()?.errorBody()?.string() ?: return null
            Gson().fromJson(errorBody, BaseResponse::class.java).message
        } catch (parseError: Exception) {
            null
        }
    }
}
package com.example.pauze.data.repository

import com.example.pauze.data.model.Settings
import com.example.pauze.data.model.UpdateSettingsRequest
import com.example.pauze.data.model.UserMeResultDto
import com.example.pauze.data.model.UserProfileResultDto
import com.example.pauze.data.model.UserProfileUpdateResultDto
import com.example.pauze.data.model.WithdrawRequest
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.MyPageService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageService: MyPageService
): MyPageRepository {
    override suspend fun getMyPage(): UserMeResultDto {
        return myPageService.getMyPage().getOrThrow()
    }

    override suspend fun getProfile(): UserProfileResultDto {
        return myPageService.getProfile().getOrThrow()
    }

    override suspend fun updateProfile(
        name: String?,
        nickname: String?,
        introduction: String?,
        profileImage: File?,
        removeProfileImage: Boolean?
    ): UserProfileUpdateResultDto {
        val textType = "text/plain".toMediaTypeOrNull()
        val imagePart = profileImage?.let {
            val mediaType = if (it.extension.lowercase() == "png") "image/png" else "image/jpeg"
            MultipartBody.Part.createFormData(
                "profileImage", it.name, it.asRequestBody(mediaType.toMediaTypeOrNull())
            )
        }

        return myPageService.updateProfile(
            name = name?.toRequestBody(textType),
            nickname = nickname?.toRequestBody(textType),
            introduction = introduction?.toRequestBody(textType),
            profileImage = imagePart,
            removeProfileImage = removeProfileImage?.toString()?.toRequestBody(textType)
        ).getOrThrow()
    }

    override suspend fun updateSettings(request: UpdateSettingsRequest): Settings {
        return myPageService.updateSettings(request).getOrThrow()
    }

    override suspend fun withdraw() {
        val response = myPageService.withdraw(WithdrawRequest(confirm = true))
        if (!response.isSuccess) {
            throw IllegalStateException("[${response.code}] ${response.message}")
        }
    }
}
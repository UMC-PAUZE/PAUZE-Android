package com.example.pauze.data.repository

import com.example.pauze.data.model.Settings
import com.example.pauze.data.model.UpdateSettingsRequest
import com.example.pauze.data.model.UserMeResultDto
import com.example.pauze.data.model.UserProfileResultDto
import com.example.pauze.data.model.UserProfileUpdateResultDto
import com.example.pauze.data.model.WithdrawRequest
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
        val response = myPageService.getMyPage()
        return response.result ?: throw IllegalStateException("[${response.code}] ${response.message}")
    }

    override suspend fun getProfile(): UserProfileResultDto {
        val response = myPageService.getProfile()
        return response.result ?: throw IllegalStateException("[${response.code}] ${response.message}")
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
            MultipartBody.Part.createFormData(
                "profileImage", it.name, it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
        }

        val response = myPageService.updateProfile(
            name = name?.toRequestBody(textType),
            nickname = nickname?.toRequestBody(textType),
            introduction = introduction?.toRequestBody(textType),
            profileImage = imagePart,
            removeProfileImage = removeProfileImage?.toString()?.toRequestBody(textType)
        )
        return response.result ?: throw IllegalStateException("[${response.code}] ${response.message}")
    }

    override suspend fun updateSettings(request: UpdateSettingsRequest): Settings {
        val response = myPageService.updateSettings(request)
        return response.result ?: throw IllegalStateException("[${response.code}] ${response.message}")
    }

    override suspend fun withdraw() {
        val response = myPageService.withdraw(WithdrawRequest(confirm = true))
        if (!response.isSuccess) {
            throw IllegalStateException("[${response.code}] ${response.message}")
        }
    }
}
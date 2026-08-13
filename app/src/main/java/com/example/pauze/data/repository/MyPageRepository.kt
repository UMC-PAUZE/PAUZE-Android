package com.example.pauze.data.repository

import com.example.pauze.data.model.Settings
import com.example.pauze.data.model.UpdateSettingsRequest
import com.example.pauze.data.model.UserMeResultDto
import com.example.pauze.data.model.UserProfileResultDto
import com.example.pauze.data.model.UserProfileUpdateResultDto
import java.io.File

interface MyPageRepository {
    suspend fun getMyPage(): UserMeResultDto
    suspend fun getProfile(): UserProfileResultDto
    suspend fun updateProfile(
        name: String? = null,
        nickname: String? = null,
        introduction: String? = null,
        profileImage: File? = null,
        removeProfileImage: Boolean? = null
    ): UserProfileUpdateResultDto
    suspend fun updateSettings(request: UpdateSettingsRequest): Settings
    suspend fun withdraw()
}
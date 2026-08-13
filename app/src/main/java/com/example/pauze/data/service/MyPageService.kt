package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.Settings
import com.example.pauze.data.model.UpdateSettingsRequest
import com.example.pauze.data.model.UserMeResultDto
import com.example.pauze.data.model.UserProfileResultDto
import com.example.pauze.data.model.UserProfileUpdateResultDto
import com.example.pauze.data.model.WithdrawRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface MyPageService {
    @GET("users/me")
    suspend fun getMyPage(): BaseResponse<UserMeResultDto>

    @GET("users/me/profile")
    suspend fun getProfile(): BaseResponse<UserProfileResultDto>

    @Multipart
    @PATCH("users/me/profile")
    suspend fun updateProfile(
        @Part("name") name: RequestBody?,
        @Part("nickname") nickname: RequestBody?,
        @Part("introduction") introduction: RequestBody?,
        @Part profileImage: MultipartBody.Part?,
        @Part("removeProfileImage") removeProfileImage: RequestBody?,
    ): BaseResponse<UserProfileUpdateResultDto>

    @PATCH("users/me/settings")
    suspend fun updateSettings(@Body request: UpdateSettingsRequest): BaseResponse<Settings>

    @HTTP(method = "DELETE", path = "users/me", hasBody = true)
    suspend fun withdraw(@Body request: WithdrawRequest): BaseResponse<Unit?>
}
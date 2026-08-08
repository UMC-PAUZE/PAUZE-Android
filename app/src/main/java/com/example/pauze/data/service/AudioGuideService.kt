package com.example.pauze.data.service

import com.example.pauze.data.remote.ApiSuccessResponse
import com.example.pauze.data.remote.audio.AudioGuideDto
import com.example.pauze.data.remote.audio.AudioLikeToggleResultDto
import com.example.pauze.data.remote.audio.AudioSaveResultDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AudioGuideService {
    @GET("audio-guides")
    suspend fun getAllGuides(
        @Header("Authorization") authorization: String?
    ): ApiSuccessResponse<List<AudioGuideDto>>

    @GET("audio-guides/categories")
    suspend fun getGuidesByCategory(
        @Query("categoryCode") categoryCode: String,
        @Header("Authorization") authorization: String?
    ): ApiSuccessResponse<List<AudioGuideDto>>

    @POST("audio-guides/{audioId}/saves")
    suspend fun saveGuide(
        @Path("audioId") audioId: String,
        @Header("Authorization") authorization: String
    ): ApiSuccessResponse<AudioSaveResultDto>

    @PATCH("audio-guides/{audioId}/likes")
    suspend fun toggleLike(
        @Path("audioId") audioId: String,
        @Header("Authorization") authorization: String
    ): ApiSuccessResponse<AudioLikeToggleResultDto>
}

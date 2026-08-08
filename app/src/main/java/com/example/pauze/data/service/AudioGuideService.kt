package com.example.pauze.data.service

import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.AudioSaveResultDto
import com.example.pauze.data.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AudioGuideService {
    @GET("audio-guides")
    suspend fun getAllGuides(): BaseResponse<List<AudioGuideDto>>

    @GET("audio-guides/categories")
    suspend fun getGuidesByCategory(
        @Query("categoryCode") categoryCode: String
    ): BaseResponse<List<AudioGuideDto>>

    @POST("audio-guides/{audioId}/saves")
    suspend fun saveGuide(
        @Path("audioId") audioId: String
    ): BaseResponse<AudioSaveResultDto>

    @PATCH("audio-guides/{audioId}/likes")
    suspend fun toggleLike(
        @Path("audioId") audioId: String
    ): BaseResponse<AudioLikeToggleResultDto>
}

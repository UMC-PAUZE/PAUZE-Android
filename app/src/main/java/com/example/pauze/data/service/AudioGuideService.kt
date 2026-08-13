package com.example.pauze.data.service

import com.example.pauze.data.model.AudioGuidePageDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface AudioGuideService {
    @GET("audio-guides")
    suspend fun getGuides(
        @Query("categoryCode") categoryCode: String? = null,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 8
    ): BaseResponse<AudioGuidePageDto>

    @GET("audio-guides/likes")
    suspend fun getLikedGuides(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 8
    ): BaseResponse<AudioGuidePageDto>

    @PATCH("audio-guides/{audioId}/likes")
    suspend fun toggleLike(
        @Path("audioId") audioId: String
    ): BaseResponse<AudioLikeToggleResultDto>
}

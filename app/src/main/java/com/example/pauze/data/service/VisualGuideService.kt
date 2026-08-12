package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.VisualGuideDto
import retrofit2.http.GET

interface VisualGuideService {
    @GET("visual-guides/file")
    suspend fun getVisualGuide(): BaseResponse<VisualGuideDto>
}
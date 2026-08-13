package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.PauzeUsageRecordRequest
import com.example.pauze.data.model.PauzeUsageRecordResultDto
import com.example.pauze.data.model.PauzeUsageStatisticsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PauzeUsageService {
    @POST("pauze-usage")
    suspend fun recordUsage(
        @Body request: PauzeUsageRecordRequest
    ): BaseResponse<PauzeUsageRecordResultDto>

    @GET("pauze-usage/statistics")
    suspend fun getStatistics(
        @Query("period") period: String
    ): BaseResponse<PauzeUsageStatisticsDto>
}

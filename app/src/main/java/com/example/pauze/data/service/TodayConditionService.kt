package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult
import retrofit2.http.Body
import retrofit2.http.POST

interface TodayConditionService {
    @POST("conditions/today")
    suspend fun createTodayCondition(
        @Body request: CreateTodayConditionRequest
    ): BaseResponse<CreateTodayConditionResult>
}

package com.example.pauze.data.repository

import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult
import com.example.pauze.data.model.GetTodayConditionResponseDto

interface TodayConditionRepository {
    suspend fun createTodayCondition(
        request: CreateTodayConditionRequest
    ): CreateTodayConditionResult

    suspend fun getTodayCondition(): GetTodayConditionResponseDto?
}

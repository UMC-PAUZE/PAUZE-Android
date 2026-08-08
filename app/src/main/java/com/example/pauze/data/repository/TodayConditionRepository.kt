package com.example.pauze.data.repository

import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult

interface TodayConditionRepository {
    suspend fun createTodayCondition(
        request: CreateTodayConditionRequest
    ): CreateTodayConditionResult
}

package com.example.pauze.data.repository

import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult
import com.example.pauze.data.service.TodayConditionService
import javax.inject.Inject

class TodayConditionRepositoryImpl @Inject constructor(
    private val service: TodayConditionService
) : TodayConditionRepository {
    override suspend fun createTodayCondition(
        request: CreateTodayConditionRequest
    ): CreateTodayConditionResult {
        val response = service.createTodayCondition(request)
        if (!response.isSuccess) {
            throw IllegalStateException("[${response.code}] ${response.message}")
        }

        return requireNotNull(response.result) {
            "[${response.code}] 응답 결과가 없습니다."
        }
    }
}

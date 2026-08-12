package com.example.pauze.data.repository

import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult
import com.example.pauze.data.model.GetTodayConditionResponseDto
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.TodayConditionService
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

class TodayConditionRepositoryImpl @Inject constructor(
    private val service: TodayConditionService
) : TodayConditionRepository {
    override suspend fun createTodayCondition(
        request: CreateTodayConditionRequest
    ): CreateTodayConditionResult {
        return try {
            val response = service.createTodayCondition(request)
            if (!response.isSuccess) {
                throw IllegalStateException("[${response.code}] ${response.message}")
            }

            requireNotNull(response.result) {
                "[${response.code}] 응답 결과가 없습니다."
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw IllegalStateException(
                "오늘의 컨디션 등록 중 오류가 발생했습니다: ${e.message}",
                e
            )
        }
    }

    override suspend fun getTodayCondition(): GetTodayConditionResponseDto? {
        return try {
            service.getTodayCondition().getOrThrow()
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
    }
}

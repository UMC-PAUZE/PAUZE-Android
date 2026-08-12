package com.example.pauze.data.repository

import android.util.Log
import com.example.pauze.data.model.PauzeUsagePeriod
import com.example.pauze.data.model.PauzeUsageRecordRequest
import com.example.pauze.data.model.PauzeUsageStatisticsDto
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.PauzeUsageService
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Singleton
class PauzeUsageRepositoryImpl @Inject constructor(
    private val service: PauzeUsageService
) : PauzeUsageRepository {
    private val recordingScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun recordCompletedUsage() {
        val completionId = UUID.randomUUID().toString()

        recordingScope.launch {
            recordWithRetry(completionId)
        }
    }

    override suspend fun getStatistics(
        period: PauzeUsagePeriod
    ): PauzeUsageStatisticsDto =
        service.getStatistics(period.name).getOrThrow()

    private suspend fun recordWithRetry(completionId: String) {
        repeat(MAX_RECORD_ATTEMPTS) { attempt ->
            try {
                service.recordUsage(
                    PauzeUsageRecordRequest(completionId = completionId)
                ).getOrThrow()
                return
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                val isLastAttempt = attempt == MAX_RECORD_ATTEMPTS - 1
                if (isLastAttempt || !error.isRetryable()) {
                    Log.w(TAG, "PAUZE 사용 기록 전송에 실패했습니다.", error)
                    return
                }
                delay(RETRY_DELAY_MILLIS * (attempt + 1))
            }
        }
    }

    private fun Exception.isRetryable(): Boolean =
        this is IOException || (this is HttpException && code() >= 500)

    private companion object {
        const val TAG = "PauzeUsageRepository"
        const val MAX_RECORD_ATTEMPTS = 3
        const val RETRY_DELAY_MILLIS = 500L
    }
}

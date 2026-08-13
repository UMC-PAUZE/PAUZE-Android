package com.example.pauze.data.repository

import com.example.pauze.data.model.PauzeUsagePeriod
import com.example.pauze.data.model.PauzeUsageStatisticsDto

interface PauzeUsageRepository {
    fun recordCompletedUsage()

    suspend fun getStatistics(
        period: PauzeUsagePeriod = PauzeUsagePeriod.ALL
    ): PauzeUsageStatisticsDto
}

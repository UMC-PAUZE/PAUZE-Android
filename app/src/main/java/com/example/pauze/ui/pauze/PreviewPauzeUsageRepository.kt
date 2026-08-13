package com.example.pauze.ui.pauze

import com.example.pauze.data.model.PauzeUsagePeriod
import com.example.pauze.data.model.PauzeUsageStatisticsDto
import com.example.pauze.data.repository.PauzeUsageRepository

internal object PreviewPauzeUsageRepository : PauzeUsageRepository {
    override fun recordCompletedUsage() = Unit

    override suspend fun getStatistics(
        period: PauzeUsagePeriod
    ): PauzeUsageStatisticsDto = PauzeUsageStatisticsDto(
        period = period.name,
        usageCount = 0,
        currentStreakDays = 0
    )
}

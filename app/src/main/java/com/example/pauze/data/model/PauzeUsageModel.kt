package com.example.pauze.data.model

data class PauzeUsageRecordRequest(
    val completionId: String
)

data class PauzeUsageRecordResultDto(
    val usageId: String,
    val completionId: String,
    val completedAt: String
)

data class PauzeUsageStatisticsDto(
    val period: String,
    val usageCount: Long,
    val currentStreakDays: Long
)

enum class PauzeUsagePeriod {
    WEEK,
    MONTH,
    ALL
}

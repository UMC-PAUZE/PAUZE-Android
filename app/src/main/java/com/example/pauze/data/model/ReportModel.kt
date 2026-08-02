package com.example.pauze.data.model

enum class ReportPeriod { WEEKLY, MONTHLY }

enum class TriggerColorToken { NOISE, SLEEP, SOCIAL, ENERGY, VISUAL_OVERLOAD } // 소음, 수면, 사회, 에너지 소진, 과한 시각 정보
data class ChartBar(val label: String, val score: Int)

// api - dto
data class WeeklyReportDto(
    val averageScore: Double,
    val hardestDay: String,
    val hardestScore: Double,
    val pauzeCount: Long,
    val scoreChange: Double?,
    val dailyScores: List<DailyScore>,
    val topTriggers: List<TopTrigger>,
    val insights: List<String>,
)

data class DailyScore(
    val day: String,
    val score: Long,
)

data class TopTrigger(
    val rank: Long,
    val trigger: String,
    val count: Long,
)

data class MonthlyReportDto(
    val averageScore: Double,
    val hardestWeek: String,
    val hardestScore: Double,
    val pauzeCount: Long,
    val scoreChange: Double?,
    val weeklyScores: List<WeeklyScore>,
    val topTriggers: List<TopTrigger>,
    val insights: List<String>,
)

data class WeeklyScore(
    val week: String,
    val averageScore: Double,
)

// State
data class ReportState(
    val weekly: WeeklyReportDto? = null,
    val monthly: MonthlyReportDto? = null
)

data class AverageScoreUiState(
    val title: String,
    val score: Double,
    val bars: List<ChartBar>,
    val bestLabel: String,
    val bestValue: String,
    val executionCount: Int
)

data class InsightUiState(
    val title: String,
    val paragraphs: List<String>
)

data class TriggerUiState(
    val label: String,
    val percent: Float,
    val colorToken: TriggerColorToken
)
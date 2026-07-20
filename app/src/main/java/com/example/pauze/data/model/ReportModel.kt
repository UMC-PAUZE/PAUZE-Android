package com.example.pauze.data.model

enum class ReportPeriod { DAILY, WEEKLY }

enum class TriggerColorToken { NOISE, SLEEP, SOCIAL, WORK, CROWDED } // 소음, 피로, 사회, 업무, 혼잡한 공간
data class ChartBar(val label: String, val score: Int)

data class InsightSegment(val text: String, val bold: Boolean)

data class AverageScoreUiState(
    val title: String,
    val score: Int,
    val bars: List<ChartBar>,
    val bestDay: String,
    val attendanceCount: Int
)

data class InsightUiState(
    val title: String,
    val paragraphs: List<List<InsightSegment>>
)

data class TriggerUiState(
    val label: String,
    val percent: Float,
    val colorToken: TriggerColorToken
)
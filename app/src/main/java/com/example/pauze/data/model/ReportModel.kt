package com.example.pauze.data.model

enum class ReportPeriod { DAILY, WEEKLY }

enum class TriggerColorToken { NOISE, SLEEP, SOCIAL, WORK, OVERSTIMULATION } // 소음, 피로, 사회, 업무, 과한 정보 자극
data class ChartBar(val label: String, val score: Int)

data class InsightSegment(val text: String, val bold: Boolean)

data class AverageScoreUiState(
    val title: String,
    val score: Int,
    val bars: List<ChartBar>,
    val bestLabel: String,
    val bestValue: String,
    val executionCount: Int
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
package com.example.pauze.data.model

enum class ReportPeriod { WEEKLY, MONTHLY }

enum class TriggerColorToken { NOISE, SLEEP, SOCIAL, ENERGY, VISUAL_OVERLOAD } // 소음, 수면, 사회, 에너지 소진, 과한 시각 정보
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
package com.example.pauze.data.model

enum class ReportPeriod { DAILY, WEEKLY }

data class ChartBar(val label: String, val score: Int)

data class AverageScoreUiState(
    val title: String,
    val score: Int,
    val bars: List<Pair<String, Int>>,
    val bestDay: String,
    val attendanceCount: Int
)


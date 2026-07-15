package com.example.pauze.ui.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class ReportPeriod { DAILY, WEEKLY }

data class AverageScoreUiState(
    val title: String,
    val scoreText: String,
    val bars: List<Pair<String, Int>>,
    val bestDay: String,
    val attendanceCount: String
)

class ReportViewModel : ViewModel() {
    var selectedPeriod by mutableStateOf(ReportPeriod.DAILY)
        private set

    fun selectPeriod(period: ReportPeriod) {
        selectedPeriod = period
    }

    val averageScore: AverageScoreUiState
        get() = if (selectedPeriod == ReportPeriod.DAILY) {
            AverageScoreUiState(
                title = "이번 주 평균 민감 지수",
                scoreText = "55점",
                bars = listOf("일" to 73, "월" to 123, "화" to 153, "수" to 113, "목" to 123, "금" to 84, "토" to 105),
                bestDay = "금요일",
                attendanceCount = "7회"
            )
        } else {
            AverageScoreUiState(
                title = "이번 달 평균 민감 지수",
                scoreText = "56점",
                bars = listOf("1주" to 73, "2주" to 123, "3주" to 153, "4주" to 113, "5주" to 123),
                bestDay = "금요일",
                attendanceCount = "7회"
            )
        }

    val insightTitle: String
        get() = if (selectedPeriod == ReportPeriod.DAILY) "이번 주 인사이트" else "이번 달 인사이트"
}
package com.example.pauze.ui.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.dummies.ReportDummyData
import com.example.pauze.data.model.AverageScoreUiState
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.ChartBar
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.InsightUiState
import com.example.pauze.data.model.MonthlyReportDto
import com.example.pauze.data.model.ReportPeriod
import com.example.pauze.data.model.ReportState
import com.example.pauze.data.model.TopTrigger
import com.example.pauze.data.model.TriggerColorToken
import com.example.pauze.data.model.TriggerUiState
import com.example.pauze.data.model.WeeklyReportDto
import com.example.pauze.data.repository.ReportRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface ReportEffect  {
    object NavigateToConditionInput: ReportEffect
    object NavigateToLogin: ReportEffect
}
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : BaseViewModel<ReportEffect, ReportState>(
    uiState = BaseUiState(data = ReportState())
) {
    var selectedPeriod by mutableStateOf(ReportPeriod.WEEKLY)
        private set

    init {
        launch {
            val weekly = reportRepository.getWeeklyReport()
            updateData { it.copy(weekly = weekly) }
        }

        launch {
            val monthly = reportRepository.getMonthlyReport()
            updateData { it.copy(monthly = monthly) }
        }
    }
    fun selectPeriod(period: ReportPeriod) {
        selectedPeriod = period
    }

    val todayCondition: Condition? = ReportDummyData.todayCondition // todo: 오늘의 컨디션 api 연동 시 교체

    val averageScore: AverageScoreUiState?
        get() = if (selectedPeriod == ReportPeriod.WEEKLY) {
            uiState.value.data.weekly?.toAverageScoreUiState()
        } else {
            uiState.value.data.monthly?.toAverageScoreUiState()
        }

    val insight: InsightUiState?
        get() = if (selectedPeriod == ReportPeriod.WEEKLY){
            uiState.value.data.weekly?.toInsightUiState()
        } else{
            uiState.value.data.monthly?.toInsightUiState()
        }

    val triggers: List<TriggerUiState>
        get() = if (selectedPeriod == ReportPeriod.WEEKLY) {
            uiState.value.data.weekly?.topTriggers?.toTriggerUiStateList() ?: emptyList()
        } else {
            uiState.value.data.monthly?.topTriggers?.toTriggerUiStateList() ?: emptyList()
        }

    fun onConditionInputClick(){
        sendEffect(ReportEffect.NavigateToConditionInput)
    }

    fun onGuestLoginClick(){
        sendEffect(ReportEffect.NavigateToLogin)
    }
}

private fun WeeklyReportDto.toAverageScoreUiState() = AverageScoreUiState(
    title = "이번 주 평균 민감 지수",
    score = averageScore,
    bars = dailyScores.map { ChartBar(it.day, it.score.toInt()) },
    bestLabel = "최고 민감 요일",
    bestValue = hardestDay,
    executionCount = pauzeCount.toInt()
)

private fun MonthlyReportDto.toAverageScoreUiState() = AverageScoreUiState(
    title = "이번 달 평균 민감 지수",
    score = averageScore,
    bars = weeklyScores.map { ChartBar(it.week, it.averageScore.toInt()) },
    bestLabel = "최고 민감 주차",
    bestValue = hardestWeek,
    executionCount = pauzeCount.toInt()
)

private fun WeeklyReportDto.toInsightUiState() = InsightUiState(
    title = "이번 주 인사이트",
    paragraphs = insights
)

private fun MonthlyReportDto.toInsightUiState() = InsightUiState(
    title = "이번 달 인사이트",
    paragraphs = insights
)

private fun List<TopTrigger>.toTriggerUiStateList(): List<TriggerUiState> {
    val total = sumOf {it.count}
    if (total == 0L) return emptyList()
    return map {
        TriggerUiState(
            label = it.trigger,
            percent = it.count / total.toFloat(),
            colorToken = it.trigger.toTriggerColorToken()
        )
    }
}

private fun String.toTriggerColorToken(): TriggerColorToken = when(this){
    "소음 노출" -> TriggerColorToken.NOISE
    "수면 부족" -> TriggerColorToken.SLEEP
    "사회피로" -> TriggerColorToken.SOCIAL
    "에너지 소진" -> TriggerColorToken.ENERGY
    "과한 시각 정보" -> TriggerColorToken.VISUAL_OVERLOAD
    else -> TriggerColorToken.NOISE
}
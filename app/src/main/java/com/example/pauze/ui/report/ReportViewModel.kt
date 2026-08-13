package com.example.pauze.ui.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.pauze.data.model.isToday
import com.example.pauze.data.model.toCondition
import com.example.pauze.data.repository.ReportRepository
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface ReportEffect  {
    object NavigateToConditionInput: ReportEffect
    object NavigateToLogin: ReportEffect
}
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val todayConditionRepository: TodayConditionRepository
) : BaseViewModel<ReportEffect, ReportState>(
    uiState = BaseUiState(data = ReportState())
) {
    var selectedPeriod by mutableStateOf(ReportPeriod.WEEKLY)
        private set

    fun refresh() {
        fetchWeekly()
        fetchMonthly()
        fetchTodayCondition()
    }

    fun selectPeriod(period: ReportPeriod) {
        selectedPeriod = period
    }

    private fun fetchWeekly() {
        launch(onFailure = { e -> updateData { it.copy(weeklyError = e.message ?: "주간 리포트를 불러오지 못했습니다") } }) {
            val weekly = reportRepository.getWeeklyReport()
            uiState.value.data.copy(weekly = weekly, weeklyError = null)
        }
    }

    private fun fetchMonthly() {
        launch(onFailure = { e -> updateData { it.copy(monthlyError = e.message ?: "월간 리포트를 불러오지 못했습니다") } }) {
            val monthly = reportRepository.getMonthlyReport()
            uiState.value.data.copy(monthly = monthly, monthlyError = null)
        }
    }

    fun fetchTodayCondition() {
        launch(onFailure = {}) {
            val dto = todayConditionRepository.getTodayCondition()
            uiState.value.data.copy(todayCondition = dto?.takeIf { it.isToday() }?.toCondition())
        }
    }

    val todayCondition: Condition?
        get() = uiState.value.data.todayCondition

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

    fun onGuestLoginClick() {
        sendEffect(ReportEffect.NavigateToLogin)
    }
}

private val weekDayOrder = listOf("월", "화", "수", "목", "금", "토", "일")

private fun WeeklyReportDto.toAverageScoreUiState(): AverageScoreUiState {
    val scoreByDay = dailyScores.associateBy { it.day }
    return AverageScoreUiState(
        title = "이번 주 평균 민감 지수",
        score = averageScore,
        bars = weekDayOrder.map { day -> ChartBar(day, scoreByDay[day]?.score?.toInt() ?: 0) },
        bestLabel = "최고 민감 요일",
        bestValue = hardestDay,
        executionCount = pauzeCount.toInt()
    )
}

private val monthWeekOrder = listOf("1주차", "2주차", "3주차", "4주차", "5주차")

private fun MonthlyReportDto.toAverageScoreUiState(): AverageScoreUiState {
    val scoreByWeek = weeklyScores.associateBy { it.week }
    return AverageScoreUiState(
        title = "이번 달 평균 민감 지수",
        score = averageScore,
        bars = monthWeekOrder.map { week -> ChartBar(week, scoreByWeek[week]?.averageScore?.toInt() ?: 0) },
        bestLabel = "최고 민감 주차",
        bestValue = hardestWeek,
        executionCount = pauzeCount.toInt()
    )
}

private fun WeeklyReportDto.toInsightUiState() = InsightUiState(
    title = "이번 주 인사이트",
    paragraphs = insights
)

private fun MonthlyReportDto.toInsightUiState() = InsightUiState(
    title = "이번 달 인사이트",
    paragraphs = insights
)

private val allTriggerCategories = listOf(
    "소음 노출" to TriggerColorToken.NOISE,
    "수면 부족" to TriggerColorToken.SLEEP,
    "사회피로" to TriggerColorToken.SOCIAL,
    "에너지 소진" to TriggerColorToken.ENERGY,
    "과한 시각 정보" to TriggerColorToken.VISUAL_OVERLOAD
)

private fun List<TopTrigger>.toTriggerUiStateList(): List<TriggerUiState> {
    val total = sumOf { it.count }
    val countByLabel = associate { it.trigger to it.count }
    return allTriggerCategories.map { (label, colorToken) ->
        val count = countByLabel[label] ?: 0L
        TriggerUiState(
            label = label,
            percent = if (total == 0L) 0f else count / total.toFloat(),
            colorToken = colorToken
        )
    }
}
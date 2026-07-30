package com.example.pauze.ui.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.dummies.ReportDummyData
import com.example.pauze.data.model.AverageScoreUiState
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.InsightUiState
import com.example.pauze.data.model.ReportPeriod
import com.example.pauze.data.model.TriggerUiState
import com.example.pauze.ui.BaseViewModel

sealed interface ReportEffect  {
    object NavigateToConditionInput: ReportEffect
    object NavigateToLogin: ReportEffect
}
class ReportViewModel : BaseViewModel<ReportEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    var selectedPeriod by mutableStateOf(ReportPeriod.WEEKLY)
        private set

    fun selectPeriod(period: ReportPeriod) {
        selectedPeriod = period
    }

    val todayCondition: Condition? = ReportDummyData.todayCondition // todo: 오늘의 컨디션 api 연동 시 교체

    val averageScore: AverageScoreUiState
        get() = if (selectedPeriod == ReportPeriod.WEEKLY) {
            ReportDummyData.dailyAverageScore
        } else {
            ReportDummyData.weeklyAverageScore
        }

    val insight: InsightUiState
        get() = if (selectedPeriod == ReportPeriod.WEEKLY){
            ReportDummyData.dailyInsight
        } else{
            ReportDummyData.weeklyInsight
        }

    val triggers: List<TriggerUiState>
        get() = if (selectedPeriod == ReportPeriod.WEEKLY) {
            ReportDummyData.dailyTriggers
        } else {
            ReportDummyData.weeklyTriggers
        }

    fun onConditionInputClick(){
        sendEffect(ReportEffect.NavigateToConditionInput)
    }

    fun onGuestLoginClick(){
        sendEffect(ReportEffect.NavigateToLogin)
    }
}
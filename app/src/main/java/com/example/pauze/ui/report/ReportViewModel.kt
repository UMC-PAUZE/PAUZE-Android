package com.example.pauze.ui.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pauze.data.datasource.ReportMockDataSource
import com.example.pauze.data.model.AverageScoreUiState
import com.example.pauze.data.model.InsightUiState
import com.example.pauze.data.model.ReportPeriod


class ReportViewModel : ViewModel() {
    var selectedPeriod by mutableStateOf(ReportPeriod.DAILY)
        private set

    fun selectPeriod(period: ReportPeriod) {
        selectedPeriod = period
    }

    val averageScore: AverageScoreUiState
        get() = if (selectedPeriod == ReportPeriod.DAILY) {
            ReportMockDataSource.dailyAverageScore
        } else {
            ReportMockDataSource.weeklyAverageScore
        }

    val insight: InsightUiState
        get() = if (selectedPeriod == ReportPeriod.DAILY){
            ReportMockDataSource.dailyInsight
        } else{
            ReportMockDataSource.weeklyInsight
        }
}
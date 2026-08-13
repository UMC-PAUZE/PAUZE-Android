package com.example.pauze.data.repository

import com.example.pauze.data.model.MonthlyReportDto
import com.example.pauze.data.model.WeeklyReportDto

interface ReportRepository {
    suspend fun getWeeklyReport(): WeeklyReportDto
    suspend fun getMonthlyReport(): MonthlyReportDto
}
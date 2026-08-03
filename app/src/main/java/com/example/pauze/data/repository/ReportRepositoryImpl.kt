package com.example.pauze.data.repository

import com.example.pauze.data.model.MonthlyReportDto
import com.example.pauze.data.model.WeeklyReportDto
import com.example.pauze.data.service.ReportService
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    val reportService: ReportService
) : ReportRepository {
    override suspend fun getWeeklyReport(): WeeklyReportDto {
        val response = reportService.getWeeklyReport()
        return response.result ?: throw IllegalStateException("[${response.code}] ${response.message}")
    }

    override suspend fun getMonthlyReport(): MonthlyReportDto {
        val response = reportService.getMonthlyReport()
        return response.result ?: throw IllegalStateException("[${response.code}] ${response.message}")
    }
}
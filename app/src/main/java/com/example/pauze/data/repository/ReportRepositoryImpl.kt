package com.example.pauze.data.repository

import com.example.pauze.data.model.MonthlyReportDto
import com.example.pauze.data.model.WeeklyReportDto
import com.example.pauze.data.service.ReportService
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    val reportService: ReportService
) : ReportRepository {
    override suspend fun getWeeklyReport(): WeeklyReportDto {
        return reportService.getWeeklyReport().result!!
    }

    override suspend fun getMonthlyReport(): MonthlyReportDto {
        return reportService.getMonthlyReport().result!!
    }
}
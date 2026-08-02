package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.MonthlyReportDto
import com.example.pauze.data.model.WeeklyReportDto
import retrofit2.http.GET

interface ReportService {
    @GET("reports/weekly")
    suspend fun getWeeklyReport(): BaseResponse<WeeklyReportDto>

    @GET("reports/monthly")
    suspend fun getMonthlyReport(): BaseResponse<MonthlyReportDto>
}
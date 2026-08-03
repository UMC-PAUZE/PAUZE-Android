package com.example.pauze.data.module

import com.example.pauze.data.repository.ReportRepository
import com.example.pauze.data.repository.ReportRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReportRepositoryModule {
    @Binds
    abstract fun bindReportRepository(impl: ReportRepositoryImpl): ReportRepository
}
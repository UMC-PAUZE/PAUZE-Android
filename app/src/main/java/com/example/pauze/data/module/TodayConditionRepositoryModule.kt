package com.example.pauze.data.module

import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.data.repository.TodayConditionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayConditionRepositoryModule {
    @Binds
    abstract fun bindTodayConditionRepository(
        implementation: TodayConditionRepositoryImpl
    ): TodayConditionRepository
}

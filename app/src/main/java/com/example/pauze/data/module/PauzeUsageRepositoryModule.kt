package com.example.pauze.data.module

import com.example.pauze.data.repository.PauzeUsageRepository
import com.example.pauze.data.repository.PauzeUsageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PauzeUsageRepositoryModule {
    @Binds
    abstract fun bindPauzeUsageRepository(
        implementation: PauzeUsageRepositoryImpl
    ): PauzeUsageRepository
}

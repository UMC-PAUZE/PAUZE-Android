package com.example.pauze.data.module

import com.example.pauze.data.repository.VisualGuideRepository
import com.example.pauze.data.repository.VisualGuideRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class VisualGuideRepositoryModule {

    @Binds
    abstract fun bindVisualGuideRepository(
        implementation: VisualGuideRepositoryImpl
    ): VisualGuideRepository
}

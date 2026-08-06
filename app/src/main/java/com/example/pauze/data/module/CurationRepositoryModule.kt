package com.example.pauze.data.module

import com.example.pauze.data.repository.CurationRepository
import com.example.pauze.data.repository.CurationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CurationRepositoryModule {

    @Binds
    abstract fun bindCurationRepository(
        impl: CurationRepositoryImpl,
    ): CurationRepository
}
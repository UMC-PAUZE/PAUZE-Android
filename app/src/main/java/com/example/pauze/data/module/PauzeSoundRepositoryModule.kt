package com.example.pauze.data.module

import com.example.pauze.data.repository.DefaultPauzeSoundRepository
import com.example.pauze.data.repository.PauzeSoundRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PauzeSoundRepositoryModule {
    @Binds
    abstract fun bindPauzeSoundRepository(
        implementation: DefaultPauzeSoundRepository
    ): PauzeSoundRepository
}

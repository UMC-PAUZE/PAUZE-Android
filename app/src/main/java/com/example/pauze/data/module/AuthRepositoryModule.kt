package com.example.pauze.data.module

import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    abstract fun bindAuthRepositoryModule(impl: AuthRepositoryImpl): AuthRepository
}
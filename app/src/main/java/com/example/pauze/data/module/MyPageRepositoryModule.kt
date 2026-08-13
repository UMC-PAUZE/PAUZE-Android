package com.example.pauze.data.module

import com.example.pauze.data.repository.MyPageRepository
import com.example.pauze.data.repository.MyPageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MyPageRepositoryModule {
    @Binds
    abstract fun bindMyPageRepository(impl: MyPageRepositoryImpl): MyPageRepository
}
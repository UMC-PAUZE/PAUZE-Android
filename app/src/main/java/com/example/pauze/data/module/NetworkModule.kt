package com.example.pauze.data.module

import com.example.pauze.BuildConfig
import com.example.pauze.data.service.AudioGuideService
import com.example.pauze.data.service.AuthService
import com.example.pauze.data.service.CurationService
import com.example.pauze.data.service.MyPageService
import com.example.pauze.data.service.ReportService
import com.example.pauze.data.service.TodayConditionService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PauzeBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://pauze.cloud/api/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    @PauzeBaseUrl
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(client: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    //리포트
    @Provides
    @Singleton
    fun provideReportService(@PauzeBaseUrl retrofit: Retrofit) : ReportService = retrofit.create(
        ReportService::class.java)

    //마이페이지
    @Provides
    @Singleton
    fun provideMyPageService(@PauzeBaseUrl retrofit: Retrofit) : MyPageService = retrofit.create(
        MyPageService::class.java)

    // 큐레이션
    @Provides
    @Singleton
    fun provideCurationService(
        @PauzeBaseUrl retrofit: Retrofit
    ): CurationService = retrofit.create(CurationService::class.java)

    // 오늘의 컨디션
    @Provides
    @Singleton
    fun provideTodayConditionService(
        @PauzeBaseUrl retrofit: Retrofit
    ): TodayConditionService = retrofit.create(TodayConditionService::class.java)

    // 청각 안정
    @Provides
    @Singleton
    fun provideAudioGuideService(
        @PauzeBaseUrl retrofit: Retrofit
    ): AudioGuideService = retrofit.create(AudioGuideService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(@AuthRetrofit retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
}

package com.example.pauze.data.remote

import com.example.pauze.data.service.AudioGuideService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object PauzeApiClient {
    private const val BASE_URL = "https://pauze.cloud/api/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val audioGuideService: AudioGuideService by lazy {
        retrofit.create(AudioGuideService::class.java)
    }
}

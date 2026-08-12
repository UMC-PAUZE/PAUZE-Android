package com.example.pauze.data.repository

interface VisualGuideRepository {
    suspend fun getVisualUrl(): String
}
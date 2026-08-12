package com.example.pauze.data.repository

import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.VisualGuideService
import javax.inject.Inject

class VisualGuideRepositoryImpl @Inject constructor(
    private val service: VisualGuideService
) : VisualGuideRepository {

    override suspend fun getVisualUrl(): String =
        service.getVisualGuide()
            .getOrThrow()
            .visualUrl
}
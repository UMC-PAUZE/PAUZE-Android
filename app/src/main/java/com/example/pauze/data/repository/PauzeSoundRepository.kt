package com.example.pauze.data.repository

import com.example.pauze.R
import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.remote.PauzeApiClient
import com.example.pauze.data.remote.PauzeAuthSession
import com.example.pauze.data.service.AudioGuideService

data class SoundLikeResult(
    val soundId: String,
    val isLiked: Boolean
)

data class SoundSaveResult(
    val soundId: String,
    val isSaved: Boolean,
    val audioUrl: String
)

interface PauzeSoundRepository {
    suspend fun getAllSounds(): List<SoundItem>
    suspend fun getSoundsByCategory(category: SoundCategory): List<SoundItem>
    suspend fun toggleLike(soundId: String): SoundLikeResult
    suspend fun saveSound(soundId: String): SoundSaveResult
}

class DefaultPauzeSoundRepository(
    private val service: AudioGuideService = PauzeApiClient.audioGuideService
) : PauzeSoundRepository {
    override suspend fun getAllSounds(): List<SoundItem> =
        service.getAllGuides(PauzeAuthSession.optionalBearerToken())
            .getOrThrow()
            .map(AudioGuideDto::toSoundItem)

    override suspend fun getSoundsByCategory(category: SoundCategory): List<SoundItem> {
        require(category != SoundCategory.ALL) { "전체 카테고리는 전체 조회 API를 사용해야 합니다." }

        return service.getGuidesByCategory(
            categoryCode = category.name,
            authorization = PauzeAuthSession.optionalBearerToken()
        ).getOrThrow().map(AudioGuideDto::toSoundItem)
    }

    override suspend fun toggleLike(soundId: String): SoundLikeResult {
        val result = service.toggleLike(
            audioId = soundId,
            authorization = PauzeAuthSession.requireBearerToken()
        ).getOrThrow()

        return SoundLikeResult(
            soundId = result.audioId.toSoundId(),
            isLiked = result.isLiked
        )
    }

    override suspend fun saveSound(soundId: String): SoundSaveResult {
        val result = service.saveGuide(
            audioId = soundId,
            authorization = PauzeAuthSession.requireBearerToken()
        ).getOrThrow()

        return SoundSaveResult(
            soundId = result.audioId.toSoundId(),
            isSaved = result.isSaved,
            audioUrl = result.audioUrl
        )
    }
}

private fun AudioGuideDto.toSoundItem(): SoundItem = SoundItem(
    id = audioId.toSoundId(),
    title = audioTitle,
    category = categoryName,
    isLiked = isLiked,
    isBookmarked = false,
    imageResId = if (audioTitle.contains("비", ignoreCase = true)) {
        R.drawable.ic_rain
    } else {
        R.drawable.ic_empty_image
    },
    audioUrl = fileUrl
)

private fun Double.toSoundId(): String =
    if (this % 1.0 == 0.0) toLong().toString() else toString()

package com.example.pauze.data.repository

import com.example.pauze.R
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.remote.ApiSuccessResponse
import com.example.pauze.data.remote.PauzeApiClient
import com.example.pauze.data.remote.PauzeAuthSession
import com.example.pauze.data.remote.audio.AudioGuideApi
import com.example.pauze.data.remote.audio.AudioGuideDto

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
    private val api: AudioGuideApi = PauzeApiClient.audioGuideApi
) : PauzeSoundRepository {
    override suspend fun getAllSounds(): List<SoundItem> =
        api.getAllGuides(PauzeAuthSession.optionalBearerToken())
            .requireResult()
            .map(AudioGuideDto::toSoundItem)

    override suspend fun getSoundsByCategory(category: SoundCategory): List<SoundItem> {
        require(category != SoundCategory.ALL) { "전체 카테고리는 전체 조회 API를 사용해야 합니다." }

        return api.getGuidesByCategory(
            categoryCode = category.name,
            authorization = PauzeAuthSession.optionalBearerToken()
        ).requireResult().map(AudioGuideDto::toSoundItem)
    }

    override suspend fun toggleLike(soundId: String): SoundLikeResult {
        val result = api.toggleLike(
            audioId = soundId,
            authorization = PauzeAuthSession.requireBearerToken()
        ).requireResult()

        return SoundLikeResult(
            soundId = result.audioId.toSoundId(),
            isLiked = result.isLiked
        )
    }

    override suspend fun saveSound(soundId: String): SoundSaveResult {
        val result = api.saveGuide(
            audioId = soundId,
            authorization = PauzeAuthSession.requireBearerToken()
        ).requireResult()

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

private fun <T> ApiSuccessResponse<T>.requireResult(): T {
    if (!isSuccess) throw PauzeApiException(code = code, message = message)
    return result
}

class PauzeApiException(
    val code: String,
    override val message: String
) : IllegalStateException(message)

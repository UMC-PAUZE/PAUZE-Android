package com.example.pauze.data.repository

import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.AudioGuidePageDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.AudioGuideService
import javax.inject.Inject

class PauzeSoundRepositoryImpl @Inject constructor(
    private val service: AudioGuideService,
    private val localDataSource: PauzeSoundLocalDataSource
) : PauzeSoundRepository {
    override suspend fun getSounds(
        category: SoundCategory,
        cursor: String?
    ): AudioGuidePageDto = service.getGuides(
        categoryCode = category.takeUnless { it == SoundCategory.ALL }?.name,
        cursor = cursor,
        size = PAGE_SIZE
    ).getOrThrow()

    override suspend fun getLikedSounds(cursor: String?): AudioGuidePageDto {
        requireAuthentication()
        return service.getLikedGuides(
            cursor = cursor,
            size = PAGE_SIZE
        ).getOrThrow()
    }

    override suspend fun toggleLike(
        soundId: String
    ): AudioLikeToggleResultDto {
        requireAuthentication()
        return service.toggleLike(audioId = soundId).getOrThrow().also { result ->
            localDataSource.updateDownloadedLike(soundId, result.isLiked)
        }
    }

    override suspend fun getDownloadedSounds(): List<SoundItem> =
        localDataSource.getDownloadedSounds()

    override suspend fun downloadSound(sound: SoundItem): String =
        localDataSource.downloadSound(sound)

    override suspend fun deleteDownloadedSound(soundId: String) =
        localDataSource.deleteDownloadedSound(soundId)

    private fun requireAuthentication() {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            throw AuthenticationRequiredException()
        }
    }

    private companion object {
        const val PAGE_SIZE = 8
    }
}

class AuthenticationRequiredException :
    IllegalStateException("로그인이 필요한 기능입니다.")

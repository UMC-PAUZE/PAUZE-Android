package com.example.pauze.data.repository

import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.AudioGuideService
import javax.inject.Inject

class PauzeSoundRepositoryImpl @Inject constructor(
    private val service: AudioGuideService,
    private val localDataSource: PauzeSoundLocalDataSource
) : PauzeSoundRepository {
    override suspend fun getAllSounds(): List<AudioGuideDto> =
        service.getAllGuides().getOrThrow()

    override suspend fun getSoundsByCategory(
        category: SoundCategory
    ): List<AudioGuideDto> {
        require(category != SoundCategory.ALL) {
            "전체 카테고리는 전체 조회 API를 사용해야 합니다."
        }

        return service.getGuidesByCategory(categoryCode = category.name)
            .getOrThrow()
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
}

class AuthenticationRequiredException :
    IllegalStateException("로그인이 필요한 기능입니다.")

package com.example.pauze.data.repository

import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.AudioSaveResultDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.getOrThrow
import com.example.pauze.data.service.AudioGuideService
import javax.inject.Inject

class PauzeSoundRepositoryImpl @Inject constructor(
    private val service: AudioGuideService
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
        return service.toggleLike(audioId = soundId).getOrThrow()
    }

    override suspend fun saveSound(
        soundId: String
    ): AudioSaveResultDto {
        requireAuthentication()
        return service.saveGuide(audioId = soundId).getOrThrow()
    }

    private fun requireAuthentication() {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            throw AuthenticationRequiredException()
        }
    }
}

class AuthenticationRequiredException :
    IllegalStateException("로그인이 필요한 기능입니다.")

package com.example.pauze.data.repository

import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.AudioSaveResultDto
import com.example.pauze.data.model.SoundCategory

interface PauzeSoundRepository {
    suspend fun getAllSounds(): List<AudioGuideDto>
    suspend fun getSoundsByCategory(category: SoundCategory): List<AudioGuideDto>
    suspend fun toggleLike(soundId: String): AudioLikeToggleResultDto
    suspend fun saveSound(soundId: String): AudioSaveResultDto
}

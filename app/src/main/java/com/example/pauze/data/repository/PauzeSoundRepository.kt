package com.example.pauze.data.repository

import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem

interface PauzeSoundRepository {
    suspend fun getAllSounds(): List<AudioGuideDto>
    suspend fun getSoundsByCategory(category: SoundCategory): List<AudioGuideDto>
    suspend fun toggleLike(soundId: String): AudioLikeToggleResultDto
    suspend fun getDownloadedSounds(): List<SoundItem>
    suspend fun downloadSound(sound: SoundItem): String
    suspend fun deleteDownloadedSound(soundId: String)
}

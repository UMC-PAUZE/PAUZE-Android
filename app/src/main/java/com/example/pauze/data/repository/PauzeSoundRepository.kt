package com.example.pauze.data.repository

import com.example.pauze.data.model.AudioGuidePageDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem

interface PauzeSoundRepository {
    suspend fun getSounds(
        category: SoundCategory,
        cursor: String? = null
    ): AudioGuidePageDto
    suspend fun getLikedSounds(cursor: String? = null): AudioGuidePageDto
    suspend fun toggleLike(soundId: String): AudioLikeToggleResultDto
    suspend fun getDownloadedSounds(): List<SoundItem>
    suspend fun downloadSound(sound: SoundItem): String
    suspend fun deleteDownloadedSound(soundId: String)
}

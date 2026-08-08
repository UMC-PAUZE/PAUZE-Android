package com.example.pauze.data.remote.audio

import kotlinx.serialization.Serializable

@Serializable
data class AudioGuideDto(
    val audioId: Double,
    val audioTitle: String,
    val categoryId: Double,
    val categoryName: String,
    val fileUrl: String,
    val isLiked: Boolean = false
)

@Serializable
data class AudioSaveResultDto(
    val audioId: Double,
    val isSaved: Boolean,
    val audioUrl: String
)

@Serializable
data class AudioLikeToggleResultDto(
    val audioId: Double,
    val isLiked: Boolean
)

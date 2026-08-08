package com.example.pauze.data.model

import kotlinx.serialization.Serializable

data class SoundItem(
    val id: String,
    val title: String,
    val category: String,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val imageResId: Int,
    val audioUrl: String = ""
)

enum class SoundCategory(val displayName: String) {
    ALL("전체"),
    NATURE_SOUND("자연소리"),
    ASMR("ASMR"),
    NOISE("노이즈")
}

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

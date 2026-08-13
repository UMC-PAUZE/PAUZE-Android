package com.example.pauze.data.model

import kotlinx.serialization.Serializable

data class SoundItem(
    val id: String,
    val title: String,
    val category: String,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val imageResId: Int,
    val audioUrl: String = "",
    val localFilePath: String? = null
)

enum class SoundCategory(val displayName: String) {
    ALL("전체"),
    NATURE_SOUND("자연소리"),
    ASMR("ASMR"),
    NOISE("노이즈")
}

@Serializable
data class AudioGuideDto(
    val audioId: Long,
    val audioTitle: String,
    val categoryCode: SoundCategory,
    val audioUrl: String,
    val isLiked: Boolean = false
)

@Serializable
data class AudioGuidePageDto(
    val content: List<AudioGuideDto>,
    val nextCursor: String?,
    val hasNext: Boolean
)

@Serializable
data class AudioLikeToggleResultDto(
    val audioId: Long,
    val isLiked: Boolean
)

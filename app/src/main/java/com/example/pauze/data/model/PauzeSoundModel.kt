package com.example.pauze.data.model

import com.example.pauze.R
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
    NOISE("노이즈");

    companion object {
        fun fromCode(code: String): SoundCategory? = entries.firstOrNull { category ->
            category != ALL && category.name.equals(code.trim(), ignoreCase = true)
        }
    }
}

@Serializable
data class AudioGuideDto(
    val audioId: Long,
    val audioTitle: String,
    val categoryCode: String,
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

fun soundImageResource(audioId: Long): Int = when (audioId) {
    1L -> R.drawable.sound_rain
    2L -> R.drawable.sound_beach_wave
    3L -> R.drawable.sound_forest
    4L -> R.drawable.sound_city
    5L -> R.drawable.sound_rain_thunder
    6L -> R.drawable.sound_campfire
    7L -> R.drawable.sound_stream
    8L -> R.drawable.sound_campfire_night
    9L -> R.drawable.sound_night_bugs
    10L -> R.drawable.sound_forest_birds
    11L -> R.drawable.sound_deep_sea
    12L -> R.drawable.sound_country_night
    13L -> R.drawable.sound_cafe
    14L -> R.drawable.sound_wind
    else -> R.drawable.ic_empty_image
}

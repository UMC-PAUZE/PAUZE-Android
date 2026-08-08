package com.example.pauze.data.model

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

package com.example.pauze.data.model

import com.example.pauze.data.dummies.Sounds

enum class SoundStashTab {
    LIKED,
    SAVED
}

data class PauzeSoundState(
    val sounds: List<SoundItem> = Sounds.items,
    val searchQuery: String = "",
    val selectedCategory: String = "전체",
    val stashSearchQuery: String = "",
    val selectedStashTab: SoundStashTab = SoundStashTab.LIKED
) {
    val filteredSounds: List<SoundItem>
        get() = sounds.filter { sound ->
            val matchesCategory = selectedCategory == "전체" || sound.category == selectedCategory
            val matchesSearch = sound.title.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    val filteredStashSounds: List<SoundItem>
        get() = sounds.filter { sound ->
            val matchesTab = when (selectedStashTab) {
                SoundStashTab.LIKED -> sound.isLiked
                SoundStashTab.SAVED -> sound.isBookmarked
            }
            matchesTab && sound.title.contains(stashSearchQuery, ignoreCase = true)
        }
}

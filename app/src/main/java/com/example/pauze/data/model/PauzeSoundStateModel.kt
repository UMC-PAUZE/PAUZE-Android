package com.example.pauze.data.model

enum class SoundStashTab {
    LIKED,
    SAVED
}

data class PauzeSoundState(
    val sounds: List<SoundItem> = emptyList(),
    val categorySounds: List<SoundItem>? = null,
    val searchQuery: String = "",
    val selectedCategory: SoundCategory = SoundCategory.ALL,
    val stashSearchQuery: String = "",
    val selectedStashTab: SoundStashTab = SoundStashTab.LIKED,
    val downloadingSoundIds: Set<String> = emptySet()
) {
    val filteredSounds: List<SoundItem>
        get() = (categorySounds ?: sounds).filter { sound ->
            sound.title.contains(searchQuery, ignoreCase = true)
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

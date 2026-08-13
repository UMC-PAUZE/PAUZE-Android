package com.example.pauze.data.model

enum class SoundStashTab {
    LIKED,
    SAVED
}

data class PauzeSoundState(
    val sounds: List<SoundItem> = emptyList(),
    val categorySounds: List<SoundItem>? = null,
    val likedSounds: List<SoundItem> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: SoundCategory = SoundCategory.ALL,
    val stashSearchQuery: String = "",
    val selectedStashTab: SoundStashTab = SoundStashTab.LIKED,
    val downloadingSoundIds: Set<String> = emptySet(),
    val isSoundListLoading: Boolean = false,
    val isLoadingMoreSounds: Boolean = false,
    val soundListNextCursor: String? = null,
    val hasNextSoundsPage: Boolean = false,
    val isLikedListLoading: Boolean = false,
    val isLoadingMoreLikedSounds: Boolean = false,
    val likedListNextCursor: String? = null,
    val hasNextLikedSoundsPage: Boolean = false,
    val likedListError: String? = null
) {
    val filteredSounds: List<SoundItem>
        get() = (categorySounds ?: sounds).filter { sound ->
            sound.title.contains(searchQuery, ignoreCase = true)
        }

    val filteredStashSounds: List<SoundItem>
        get() = when (selectedStashTab) {
            SoundStashTab.LIKED -> likedSounds
            SoundStashTab.SAVED -> (
                sounds + categorySounds.orEmpty() + likedSounds
                ).distinctBy(SoundItem::id).filter(SoundItem::isBookmarked)
        }.filter { sound ->
            sound.title.contains(stashSearchQuery, ignoreCase = true)
        }
}

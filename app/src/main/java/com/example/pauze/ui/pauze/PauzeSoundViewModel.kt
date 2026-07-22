package com.example.pauze.ui.pauze

import com.example.pauze.data.dummies.Sounds
import com.example.pauze.data.model.SoundItem
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

enum class SoundDestination {
    LIST,
    STASH
}

sealed interface PauzeSoundEffect {
    data object NavigateBack : PauzeSoundEffect
    data object NavigateToStash : PauzeSoundEffect
    data class NavigateToDetail(
        val soundId: String,
        val origin: SoundDestination
    ) : PauzeSoundEffect

    data class NavigateTo(val destination: SoundDestination) : PauzeSoundEffect
}

class PauzeSoundViewModel : BaseViewModel<PauzeSoundEffect>() {
    private val _state = MutableStateFlow(PauzeSoundState())
    val state = _state.asStateFlow()

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun updateStashSearchQuery(query: String) {
        _state.update { it.copy(stashSearchQuery = query) }
    }

    fun selectStashTab(tab: SoundStashTab) {
        _state.update { it.copy(selectedStashTab = tab) }
    }

    fun toggleLike(soundId: String) {
        updateSound(soundId) { sound -> sound.copy(isLiked = !sound.isLiked) }
    }

    fun toggleBookmark(soundId: String) {
        updateSound(soundId) { sound -> sound.copy(isBookmarked = !sound.isBookmarked) }
    }

    fun openStash() {
        sendEffect(PauzeSoundEffect.NavigateToStash)
    }

    fun openDetail(soundId: String, origin: SoundDestination) {
        sendEffect(PauzeSoundEffect.NavigateToDetail(soundId, origin))
    }

    fun navigateTo(destination: SoundDestination) {
        sendEffect(PauzeSoundEffect.NavigateTo(destination))
    }

    fun requestBack() {
        sendEffect(PauzeSoundEffect.NavigateBack)
    }

    private fun updateSound(soundId: String, transform: (SoundItem) -> SoundItem) {
        _state.update { currentState ->
            currentState.copy(
                sounds = currentState.sounds.map { sound ->
                    if (sound.id == soundId) transform(sound) else sound
                }
            )
        }
    }
}

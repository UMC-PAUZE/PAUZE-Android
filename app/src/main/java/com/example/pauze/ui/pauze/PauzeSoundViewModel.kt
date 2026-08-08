package com.example.pauze.ui.pauze

import androidx.lifecycle.viewModelScope
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.PauzeSoundState
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.model.SoundStashTab
import com.example.pauze.data.repository.AuthenticationRequiredException
import com.example.pauze.data.repository.PauzeSoundRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

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

@HiltViewModel
class PauzeSoundViewModel @Inject constructor(
    private val repository: PauzeSoundRepository
) : BaseViewModel<PauzeSoundEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    private val _state = MutableStateFlow(PauzeSoundState())
    val state = _state.asStateFlow()
    private var allSoundsJob: Job? = null
    private var categorySoundsJob: Job? = null

    init {
        loadSounds(SoundCategory.ALL)
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: SoundCategory) {
        if (_state.value.selectedCategory == category) return

        if (category == SoundCategory.ALL) {
            categorySoundsJob?.cancel()
        }

        _state.update {
            it.copy(
                selectedCategory = category,
                categorySounds = if (category == SoundCategory.ALL) null else emptyList(),
                isLoading = true,
                errorMessage = null
            )
        }
        loadSounds(category)
    }

    fun updateStashSearchQuery(query: String) {
        _state.update { it.copy(stashSearchQuery = query) }
    }

    fun selectStashTab(tab: SoundStashTab) {
        _state.update { it.copy(selectedStashTab = tab) }
    }

    fun toggleLike(soundId: String) {
        viewModelScope.launch {
            try {
                val result = repository.toggleLike(soundId)
                updateSound(result.soundId) { sound ->
                    sound.copy(isLiked = result.isLiked)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                showError(error)
            }
        }
    }

    fun toggleBookmark(soundId: String) {
        val sound = findSound(soundId) ?: return
        if (sound.isBookmarked) return

        viewModelScope.launch {
            try {
                val result = repository.saveSound(soundId)
                updateSound(result.soundId) { currentSound ->
                    currentSound.copy(
                        isBookmarked = result.isSaved,
                        audioUrl = result.audioUrl.ifBlank { currentSound.audioUrl }
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                showError(error)
            }
        }
    }

    fun retry() {
        loadSounds(_state.value.selectedCategory)
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

    private fun loadSounds(category: SoundCategory) {
        if (category == SoundCategory.ALL && allSoundsJob?.isActive == true) {
            return
        }
        if (category != SoundCategory.ALL) {
            categorySoundsJob?.cancel()
        }

        val job = viewModelScope.launch {
            _state.update { currentState ->
                if (currentState.selectedCategory == category) {
                    currentState.copy(isLoading = true, errorMessage = null)
                } else {
                    currentState
                }
            }

            try {
                val remoteSounds = if (category == SoundCategory.ALL) {
                    repository.getAllSounds()
                } else {
                    repository.getSoundsByCategory(category)
                }

                _state.update { currentState ->
                    val localSounds = currentState.sounds + currentState.categorySounds.orEmpty()
                    val mergedSounds = mergeRemoteWithLocal(remoteSounds, localSounds)

                    if (category == SoundCategory.ALL) {
                        currentState.copy(
                            sounds = mergedSounds,
                            categorySounds = if (
                                currentState.selectedCategory == SoundCategory.ALL
                            ) {
                                null
                            } else {
                                currentState.categorySounds
                            },
                            isLoading = if (
                                currentState.selectedCategory == SoundCategory.ALL
                            ) {
                                false
                            } else {
                                currentState.isLoading
                            }
                        )
                    } else {
                        val isCurrentCategory = currentState.selectedCategory == category
                        currentState.copy(
                            sounds = mergeIntoAll(currentState.sounds, mergedSounds),
                            categorySounds = if (isCurrentCategory) {
                                mergedSounds
                            } else {
                                currentState.categorySounds
                            },
                            isLoading = if (isCurrentCategory) {
                                false
                            } else {
                                currentState.isLoading
                            }
                        )
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                _state.update { currentState ->
                    if (currentState.selectedCategory == category) {
                        currentState.copy(
                            isLoading = false,
                            errorMessage = error.toUserMessage()
                        )
                    } else {
                        currentState
                    }
                }
            }
        }

        if (category == SoundCategory.ALL) {
            allSoundsJob = job
        } else {
            categorySoundsJob = job
        }
    }

    private fun updateSound(soundId: String, transform: (SoundItem) -> SoundItem) {
        _state.update { currentState ->
            currentState.copy(
                sounds = currentState.sounds.map { sound ->
                    if (sound.id == soundId) transform(sound) else sound
                },
                categorySounds = currentState.categorySounds?.map { sound ->
                    if (sound.id == soundId) transform(sound) else sound
                },
                errorMessage = null
            )
        }
    }

    private fun findSound(soundId: String): SoundItem? =
        (_state.value.categorySounds.orEmpty() + _state.value.sounds)
            .firstOrNull { it.id == soundId }

    private fun showError(error: Throwable) {
        _state.update { it.copy(errorMessage = error.toUserMessage()) }
    }
}

private fun mergeRemoteWithLocal(
    remoteSounds: List<SoundItem>,
    localSounds: List<SoundItem>
): List<SoundItem> {
    val localById = localSounds.associateBy(SoundItem::id)

    return remoteSounds.map { remote ->
        val local = localById[remote.id]
        remote.copy(
            isLiked = local?.isLiked ?: remote.isLiked,
            isBookmarked = local?.isBookmarked ?: remote.isBookmarked,
            audioUrl = remote.audioUrl.ifBlank { local?.audioUrl.orEmpty() }
        )
    }
}

private fun mergeIntoAll(
    currentSounds: List<SoundItem>,
    updatedSounds: List<SoundItem>
): List<SoundItem> {
    val updatedById = updatedSounds.associateBy(SoundItem::id)
    val merged = currentSounds.map { sound -> updatedById[sound.id] ?: sound }
    val existingIds = currentSounds.mapTo(mutableSetOf(), SoundItem::id)
    return merged + updatedSounds.filterNot { it.id in existingIds }
}

private fun Throwable.toUserMessage(): String = when (this) {
    is AuthenticationRequiredException -> message ?: "로그인이 필요한 기능입니다."
    is HttpException -> when (code()) {
        400 -> "잘못된 요청입니다."
        401 -> "로그인이 필요한 기능입니다."
        404 -> "요청한 소리를 찾을 수 없습니다."
        else -> "서버 요청에 실패했습니다. (${code()})"
    }
    is IOException -> "서버에 연결할 수 없습니다. 네트워크 상태를 확인해주세요."
    else -> message ?: "알 수 없는 오류가 발생했습니다."
}

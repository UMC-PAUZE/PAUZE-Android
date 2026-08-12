package com.example.pauze.ui.pauze

import com.example.pauze.R
import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
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
) : BaseViewModel<PauzeSoundEffect, PauzeSoundState>(
    uiState = BaseUiState(data = PauzeSoundState())
) {
    private var isAllSoundsRequestRunning = false
    private var categoryRequestVersion = 0
    private var activeSoundListRequestCount = 0

    init {
        loadSounds(
            category = SoundCategory.ALL,
            restoreDownloads = true
        )
    }

    fun updateSearchQuery(query: String) {
        updateData { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: SoundCategory) {
        if (uiState.value.data.selectedCategory == category) return

        if (category == SoundCategory.ALL) {
            categoryRequestVersion++
        }

        updateData {
            it.copy(
                selectedCategory = category,
                categorySounds = if (category == SoundCategory.ALL) null else emptyList()
            )
        }
        loadSounds(category)
    }

    fun updateStashSearchQuery(query: String) {
        updateData { it.copy(stashSearchQuery = query) }
    }

    fun selectStashTab(tab: SoundStashTab) {
        updateData { it.copy(selectedStashTab = tab) }
    }

    fun toggleLike(soundId: String) {
        clearError()
        launch<AudioLikeToggleResultDto>(
            onSuccess = { result ->
                updateSound(soundId) { sound ->
                    sound.copy(isLiked = result.isLiked)
                }
            },
            block = {
                repository.toggleLike(soundId)
            }
        )
    }

    private fun finishBookmarkOperation(soundId: String) {
        updateData { currentState ->
            currentState.copy(
                downloadingSoundIds = currentState.downloadingSoundIds - soundId
            )
        }
    }

    fun toggleBookmark(soundId: String) {
        val sound = findSound(soundId) ?: return
        if (soundId in uiState.value.data.downloadingSoundIds) return

        updateData { currentState ->
            currentState.copy(
                downloadingSoundIds = currentState.downloadingSoundIds + soundId
            )
        }
        clearError()

        if (sound.isBookmarked) {
            launch<Unit>(
                onSuccess = {
                    updateSound(soundId) { currentSound ->
                        currentSound.copy(
                            isBookmarked = false,
                            localFilePath = null
                        )
                    }
                    finishBookmarkOperation(soundId)
                },
                onFailure = {
                    finishBookmarkOperation(soundId)
                },
                block = {
                    repository.deleteDownloadedSound(soundId)
                }
            )
        } else {
            launch<String>(
                onSuccess = { localFilePath ->
                    updateSound(soundId) { currentSound ->
                        currentSound.copy(
                            isBookmarked = true,
                            localFilePath = localFilePath
                        )
                    }
                    finishBookmarkOperation(soundId)
                },
                onFailure = {
                    finishBookmarkOperation(soundId)
                },
                block = {
                    repository.downloadSound(sound)
                }
            )
        }
    }

    fun retry() {
        loadSounds(uiState.value.data.selectedCategory)
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

    private fun loadSounds(
        category: SoundCategory,
        restoreDownloads: Boolean = false
    ) {
        if (category == SoundCategory.ALL && isAllSoundsRequestRunning) {
            return
        }

        val requestVersion = if (category == SoundCategory.ALL) {
            isAllSoundsRequestRunning = true
            0
        } else {
            ++categoryRequestVersion
        }
        beginSoundListRequest()
        clearError()

        launch(
            block = {
                val result = try {
                    if (restoreDownloads) {
                        val downloadedSounds = try {
                            repository.getDownloadedSounds()
                        } catch (error: CancellationException) {
                            throw error
                        } catch (_: Exception) {
                            emptyList()
                        }
                        updateData { currentState ->
                            currentState.copy(
                                sounds = mergeDownloadedIntoAll(
                                    currentSounds = currentState.sounds,
                                    downloadedSounds = downloadedSounds
                                )
                            )
                        }
                    }

                    val remoteSounds = if (category == SoundCategory.ALL) {
                        repository.getAllSounds()
                    } else {
                        repository.getSoundsByCategory(category)
                    }.map(AudioGuideDto::toSoundItem)

                    val currentState = uiState.value.data
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
                            }
                        )
                    } else {
                        val isCurrentCategory =
                            requestVersion == categoryRequestVersion &&
                                currentState.selectedCategory == category
                        currentState.copy(
                            sounds = mergeIntoAll(currentState.sounds, mergedSounds),
                            categorySounds = if (isCurrentCategory) {
                                mergedSounds
                            } else {
                                currentState.categorySounds
                            }
                        )
                    }
                } finally {
                    if (category == SoundCategory.ALL) {
                        isAllSoundsRequestRunning = false
                    }
                    finishSoundListRequest()
                }
                result.copy(isSoundListLoading = activeSoundListRequestCount > 0)
            }
        )
    }

    private fun beginSoundListRequest() {
        activeSoundListRequestCount++
        updateData { currentState ->
            currentState.copy(isSoundListLoading = true)
        }
    }

    private fun finishSoundListRequest() {
        activeSoundListRequestCount = (activeSoundListRequestCount - 1).coerceAtLeast(0)
        updateData { currentState ->
            currentState.copy(
                isSoundListLoading = activeSoundListRequestCount > 0
            )
        }
    }

    private fun clearError() {
        if (uiState.value.error != null) {
            updateState { currentState ->
                currentState.copy(error = null)
            }
        }
    }

    private fun updateSound(soundId: String, transform: (SoundItem) -> SoundItem) {
        updateData { currentState ->
            currentState.copy(
                sounds = currentState.sounds.map { sound ->
                    if (sound.id == soundId) transform(sound) else sound
                },
                categorySounds = currentState.categorySounds?.map { sound ->
                    if (sound.id == soundId) transform(sound) else sound
                }
            )
        }
    }

    private fun findSound(soundId: String): SoundItem? =
        (uiState.value.data.categorySounds.orEmpty() + uiState.value.data.sounds)
            .firstOrNull { it.id == soundId }
}

internal fun mergeRemoteWithLocal(
    remoteSounds: List<SoundItem>,
    localSounds: List<SoundItem>
): List<SoundItem> {
    val localById = localSounds.associateBy(SoundItem::id)
    val remoteIds = remoteSounds.mapTo(mutableSetOf(), SoundItem::id)

    val mergedRemoteSounds = remoteSounds.map { remote ->
        val local = localById[remote.id]
        remote.copy(
            isLiked = local?.isLiked ?: remote.isLiked,
            isBookmarked = local?.isBookmarked ?: remote.isBookmarked,
            audioUrl = remote.audioUrl.ifBlank { local?.audioUrl.orEmpty() },
            localFilePath = local?.localFilePath
        )
    }

    val downloadedLocalOnlySounds = localById.values.filter { local ->
        local.id !in remoteIds && local.localFilePath != null
    }

    return mergedRemoteSounds + downloadedLocalOnlySounds
}

private fun mergeDownloadedIntoAll(
    currentSounds: List<SoundItem>,
    downloadedSounds: List<SoundItem>
): List<SoundItem> {
    val downloadedById = downloadedSounds.associateBy(SoundItem::id)
    val merged = currentSounds.map { currentSound ->
        val downloaded = downloadedById[currentSound.id]
        if (downloaded == null) {
            currentSound
        } else {
            currentSound.copy(
                isBookmarked = true,
                localFilePath = downloaded.localFilePath,
                audioUrl = currentSound.audioUrl.ifBlank { downloaded.audioUrl }
            )
        }
    }
    val currentIds = currentSounds.mapTo(mutableSetOf(), SoundItem::id)
    return merged + downloadedSounds.filterNot { it.id in currentIds }
}

private fun AudioGuideDto.toSoundItem(): SoundItem = SoundItem(
    id = audioId.toString(),
    title = audioTitle,
    category = categoryName,
    isLiked = isLiked,
    isBookmarked = false,
    imageResId = if (audioTitle.contains("비", ignoreCase = true)) {
        R.drawable.ic_rain
    } else {
        R.drawable.ic_empty_image
    },
    audioUrl = fileUrl,
    localFilePath = null
)

private fun mergeIntoAll(
    currentSounds: List<SoundItem>,
    updatedSounds: List<SoundItem>
): List<SoundItem> {
    val updatedById = updatedSounds.associateBy(SoundItem::id)
    val merged = currentSounds.map { sound -> updatedById[sound.id] ?: sound }
    val existingIds = currentSounds.mapTo(mutableSetOf(), SoundItem::id)
    return merged + updatedSounds.filterNot { it.id in existingIds }
}

internal fun Throwable.toPauzeSoundErrorMessage(): String = when (this) {
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

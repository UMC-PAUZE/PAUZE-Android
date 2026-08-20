package com.example.pauze.ui.pauze.sound

import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.PauzeSoundState
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.model.SoundStashTab
import com.example.pauze.data.model.soundImageResource
import com.example.pauze.data.repository.AuthenticationRequiredException
import com.example.pauze.data.repository.PauzeSoundRepository
import com.example.pauze.data.repository.PauzeUsageRepository
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
    private val repository: PauzeSoundRepository,
    private val pauzeUsageRepository: PauzeUsageRepository
) : BaseViewModel<PauzeSoundEffect, PauzeSoundState>(
    uiState = BaseUiState(data = PauzeSoundState())
) {
    private var soundListRequestVersion = 0
    private var likedListRequestVersion = 0
    private var hasLoadedLikedSounds = false

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

        updateData {
            it.copy(
                selectedCategory = category,
                categorySounds = if (category == SoundCategory.ALL) null else emptyList(),
                soundListNextCursor = null,
                hasNextSoundsPage = false
            )
        }
        loadSounds(category)
    }

    fun updateStashSearchQuery(query: String) {
        updateData { it.copy(stashSearchQuery = query) }
    }

    fun selectStashTab(tab: SoundStashTab) {
        updateData { it.copy(selectedStashTab = tab) }
        if (tab == SoundStashTab.LIKED && !hasLoadedLikedSounds) {
            loadLikedSounds()
        }
    }

    fun toggleLike(soundId: String) {
        clearError()
        launch<AudioLikeToggleResultDto>(
            onSuccess = { result ->
                applyLikeResult(soundId, result.isLiked)
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
                },
                block = {
                    try {
                        repository.deleteDownloadedSound(soundId)
                    } finally {
                        finishBookmarkOperation(soundId)
                    }
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
                },
                block = {
                    try {
                        repository.downloadSound(sound)
                    } finally {
                        finishBookmarkOperation(soundId)
                    }
                }
            )
        }
    }

    fun retry() {
        loadSounds(uiState.value.data.selectedCategory)
    }

    fun loadMoreSounds() {
        val currentState = uiState.value.data
        if (
            currentState.isSoundListLoading ||
            currentState.isLoadingMoreSounds ||
            !currentState.hasNextSoundsPage ||
            currentState.soundListNextCursor == null
        ) {
            return
        }
        loadSounds(currentState.selectedCategory, append = true)
    }

    fun retryLikedSounds() {
        loadLikedSounds()
    }

    fun loadMoreLikedSounds() {
        val currentState = uiState.value.data
        if (
            currentState.isLikedListLoading ||
            currentState.isLoadingMoreLikedSounds ||
            !currentState.hasNextLikedSoundsPage ||
            currentState.likedListNextCursor == null
        ) {
            return
        }
        loadLikedSounds(append = true)
    }

    fun openStash() {
        if (!hasLoadedLikedSounds) {
            loadLikedSounds()
        }
        sendEffect(PauzeSoundEffect.NavigateToStash)
    }

    fun openDetail(soundId: String, origin: SoundDestination) {
        sendEffect(PauzeSoundEffect.NavigateToDetail(soundId, origin))
    }

    fun navigateTo(destination: SoundDestination) {
        if (destination == SoundDestination.LIST) {
            clearError()
        }
        sendEffect(PauzeSoundEffect.NavigateTo(destination))
    }

    fun requestBack() {
        sendEffect(PauzeSoundEffect.NavigateBack)
    }

    fun recordCompletedUsage() {
        pauzeUsageRepository.recordCompletedUsage()
    }

    private fun loadSounds(
        category: SoundCategory,
        restoreDownloads: Boolean = false,
        append: Boolean = false
    ) {
        val state = uiState.value.data
        if (append && (state.isLoadingMoreSounds || state.soundListNextCursor == null)) {
            return
        }

        val requestVersion = if (append) {
            soundListRequestVersion
        } else {
            ++soundListRequestVersion
        }
        val cursor = if (append) state.soundListNextCursor else null

        updateData { currentState ->
            if (append) {
                currentState.copy(isLoadingMoreSounds = true)
            } else {
                currentState.copy(
                    isSoundListLoading = true,
                    isLoadingMoreSounds = false,
                    soundListNextCursor = null,
                    hasNextSoundsPage = false
                )
            }
        }
        clearError()

        launch(
            onFailure = {
                if (requestVersion == soundListRequestVersion) {
                    finishSoundListRequest(append)
                }
            },
            block = {
                try {
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

                    val page = repository.getSounds(category, cursor)
                    val remoteSounds = page.content.map(AudioGuideDto::toSoundItem)
                    val currentState = uiState.value.data

                    if (
                        requestVersion != soundListRequestVersion ||
                        currentState.selectedCategory != category
                    ) {
                        currentState
                    } else {
                        val localSounds = currentState.sounds +
                            currentState.categorySounds.orEmpty() +
                            currentState.likedSounds

                        if (category == SoundCategory.ALL) {
                            val combinedSounds = if (append) {
                                mergeIntoAll(currentState.sounds, remoteSounds)
                            } else {
                                remoteSounds
                            }
                            currentState.copy(
                                sounds = mergeRemoteWithLocal(combinedSounds, localSounds),
                                categorySounds = null,
                                isSoundListLoading = false,
                                isLoadingMoreSounds = false,
                                soundListNextCursor = page.nextCursor,
                                hasNextSoundsPage = page.hasNext
                            )
                        } else {
                            val combinedSounds = if (append) {
                                mergeIntoAll(currentState.categorySounds.orEmpty(), remoteSounds)
                            } else {
                                remoteSounds
                            }
                            val matchingLocalSounds = localSounds.filter {
                                it.category == category.displayName
                            }
                            val mergedSounds = mergeRemoteWithLocal(
                                remoteSounds = combinedSounds,
                                localSounds = matchingLocalSounds
                            )
                            currentState.copy(
                                sounds = mergeIntoAll(currentState.sounds, mergedSounds),
                                categorySounds = mergedSounds,
                                isSoundListLoading = false,
                                isLoadingMoreSounds = false,
                                soundListNextCursor = page.nextCursor,
                                hasNextSoundsPage = page.hasNext
                            )
                        }
                    }
                } finally {
                    if (requestVersion == soundListRequestVersion) {
                        finishSoundListRequest(append)
                    }
                }
            }
        )
    }

    private fun finishSoundListRequest(append: Boolean) {
        updateData { currentState ->
            if (append) {
                currentState.copy(isLoadingMoreSounds = false)
            } else {
                currentState.copy(isSoundListLoading = false)
            }
        }
    }

    private fun loadLikedSounds(append: Boolean = false) {
        val state = uiState.value.data
        if (append && (state.isLoadingMoreLikedSounds || state.likedListNextCursor == null)) {
            return
        }

        val requestVersion = if (append) {
            likedListRequestVersion
        } else {
            ++likedListRequestVersion
        }
        val cursor = if (append) state.likedListNextCursor else null

        updateData { currentState ->
            if (append) {
                currentState.copy(isLoadingMoreLikedSounds = true, likedListError = null)
            } else {
                currentState.copy(
                    isLikedListLoading = true,
                    isLoadingMoreLikedSounds = false,
                    likedListNextCursor = null,
                    hasNextLikedSoundsPage = false,
                    likedListError = null
                )
            }
        }
        clearError()

        launch(
            onFailure = { error ->
                if (requestVersion == likedListRequestVersion) {
                    finishLikedListRequest(append, error.toPauzeSoundErrorMessage())
                }
            },
            block = {
                try {
                    val page = repository.getLikedSounds(cursor)
                    val remoteSounds = page.content.map(AudioGuideDto::toSoundItem)
                    val currentState = uiState.value.data

                    if (requestVersion != likedListRequestVersion) {
                        currentState
                    } else {
                        val combinedSounds = if (append) {
                            mergeIntoAll(currentState.likedSounds, remoteSounds)
                        } else {
                            remoteSounds
                        }
                        val mergedSounds = mergeRemoteWithLocal(
                            remoteSounds = combinedSounds,
                            localSounds = currentState.sounds + currentState.likedSounds,
                            includeDownloadedLocalOnly = false
                        )
                        hasLoadedLikedSounds = true

                        currentState.copy(
                            sounds = mergeIntoAll(currentState.sounds, mergedSounds),
                            likedSounds = mergedSounds,
                            isLikedListLoading = false,
                            isLoadingMoreLikedSounds = false,
                            likedListNextCursor = page.nextCursor,
                            hasNextLikedSoundsPage = page.hasNext,
                            likedListError = null
                        )
                    }
                } finally {
                    if (requestVersion == likedListRequestVersion) {
                        finishLikedListRequest(append)
                    }
                }
            }
        )
    }

    private fun finishLikedListRequest(append: Boolean, errorMessage: String? = null) {
        updateData { currentState ->
            if (append) {
                currentState.copy(
                    isLoadingMoreLikedSounds = false,
                    likedListError = errorMessage ?: currentState.likedListError
                )
            } else {
                currentState.copy(
                    isLikedListLoading = false,
                    likedListError = errorMessage ?: currentState.likedListError
                )
            }
        }
    }

    private fun clearError() {
        if (uiState.value.error != null) {
            updateState { currentState ->
                currentState.copy(error = null)
            }
        }
    }

    private fun applyLikeResult(soundId: String, isLiked: Boolean) {
        updateData { currentState ->
            val source = (
                currentState.categorySounds.orEmpty() +
                    currentState.sounds +
                    currentState.likedSounds
                ).firstOrNull { it.id == soundId }
            val likedSound = source?.copy(isLiked = isLiked)

            currentState.copy(
                sounds = currentState.sounds.map { sound ->
                    if (sound.id == soundId) sound.copy(isLiked = isLiked) else sound
                },
                categorySounds = currentState.categorySounds?.map { sound ->
                    if (sound.id == soundId) sound.copy(isLiked = isLiked) else sound
                },
                likedSounds = if (isLiked && likedSound != null) {
                    mergeIntoAll(currentState.likedSounds, listOf(likedSound))
                } else {
                    currentState.likedSounds.filterNot { it.id == soundId }
                }
            )
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
                },
                likedSounds = currentState.likedSounds.map { sound ->
                    if (sound.id == soundId) transform(sound) else sound
                }
            )
        }
    }

    private fun findSound(soundId: String): SoundItem? =
        (
            uiState.value.data.categorySounds.orEmpty() +
                uiState.value.data.sounds +
                uiState.value.data.likedSounds
            ).firstOrNull { it.id == soundId }
}

internal fun mergeRemoteWithLocal(
    remoteSounds: List<SoundItem>,
    localSounds: List<SoundItem>,
    includeDownloadedLocalOnly: Boolean = true
): List<SoundItem> {
    val localById = localSounds.associateBy(SoundItem::id)
    val remoteIds = remoteSounds.mapTo(mutableSetOf(), SoundItem::id)

    val mergedRemoteSounds = remoteSounds.map { remote ->
        val local = localById[remote.id]
        remote.copy(
            isBookmarked = local?.isBookmarked ?: remote.isBookmarked,
            audioUrl = remote.audioUrl.ifBlank { local?.audioUrl.orEmpty() },
            localFilePath = local?.localFilePath
        )
    }

    val downloadedLocalOnlySounds = if (includeDownloadedLocalOnly) {
        localById.values.filter { local ->
            local.id !in remoteIds && local.localFilePath != null
        }
    } else {
        emptyList()
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

private fun AudioGuideDto.toSoundItem(): SoundItem {
    val categoryName = SoundCategory.fromCode(categoryCode)?.displayName
        ?: categoryCode.ifBlank { UNKNOWN_CATEGORY_NAME }

    return SoundItem(
        id = audioId.toString(),
        title = audioTitle,
        category = categoryName,
        isLiked = isLiked,
        isBookmarked = false,
        imageResId = soundImageResource(audioId),
        audioUrl = audioUrl,
        localFilePath = null
    )
}

private const val UNKNOWN_CATEGORY_NAME = "기타"

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

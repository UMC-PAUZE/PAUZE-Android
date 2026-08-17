package com.example.pauze.ui.pauze.sound

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pauze.R
import com.example.pauze.data.dummies.Sounds
import com.example.pauze.data.model.AudioGuideDto
import com.example.pauze.data.model.AudioGuidePageDto
import com.example.pauze.data.model.AudioLikeToggleResultDto
import com.example.pauze.data.model.SoundCategory
import com.example.pauze.data.model.SoundItem
import com.example.pauze.data.repository.PauzeSoundRepository
import com.example.pauze.ui.component.Chips
import com.example.pauze.ui.component.SearchBar
import com.example.pauze.ui.component.SoundItem
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextMdMedium

@Composable
fun PauzeSoundScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PauzeSoundViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState.data
    val errorMessage = uiState.error?.toPauzeSoundErrorMessage()
    var currentDestination by remember { mutableStateOf(SoundDestination.LIST) }
    var detailOrigin by remember { mutableStateOf(SoundDestination.LIST) }
    var selectedSoundId by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PauzeSoundEffect.NavigateBack -> onBackClick()
                PauzeSoundEffect.NavigateToStash -> currentDestination = SoundDestination.STASH
                is PauzeSoundEffect.NavigateToDetail -> {
                    selectedSoundId = effect.soundId
                    detailOrigin = effect.origin
                }
                is PauzeSoundEffect.NavigateTo -> currentDestination = effect.destination
            }
        }
    }

    val selectedSound = (
        state.categorySounds.orEmpty() + state.sounds + state.likedSounds
        ).firstOrNull { it.id == selectedSoundId }
    when {
        selectedSound != null -> {
            PauzeSoundDetailScreen(
                sound = selectedSound,
                onToggleLike = viewModel::toggleLike,
                onToggleBookmark = viewModel::toggleBookmark,
                onUsageQualified = viewModel::recordCompletedUsage,
                isDownloading = selectedSound.id in state.downloadingSoundIds,
                onBackClick = {
                    selectedSoundId = null
                    viewModel.navigateTo(detailOrigin)
                },
                modifier = modifier
            )
        }

        currentDestination == SoundDestination.STASH -> {
            PauzeSoundStashScreen(
                viewModel = viewModel,
                modifier = modifier
            )
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(AppTheme.palette.base.getColor(0)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(
                    title = "청각 안정",
                    showBackButton = true,
                    onBackClick = viewModel::requestBack,
                    rightIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_box),
                            contentDescription = "보관함",
                            tint = AppTheme.palette.gray.getColor(2),
                            modifier = Modifier.clickable(onClick = viewModel::openStash)
                        )
                    }
                )

                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = viewModel::updateSearchQuery,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .width(312.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SoundCategory.entries.forEach { category ->
                        val chipWidth = when (category) {
                            SoundCategory.ALL -> 50.dp
                            SoundCategory.NATURE_SOUND -> 75.dp
                            SoundCategory.ASMR -> 62.dp
                            SoundCategory.NOISE -> 63.dp
                        }

                        Chips(
                            text = category.displayName,
                            isSelected = state.selectedCategory == category,
                            onClick = { viewModel.selectCategory(category) },
                            modifier = Modifier.size(width = chipWidth, height = 34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                when {
                    state.isSoundListLoading && state.filteredSounds.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .width(312.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = AppTheme.palette.primary.getColor(3)
                            )
                        }
                    }

                    errorMessage != null && state.filteredSounds.isEmpty() -> {
                        SoundMessage(
                            message = errorMessage,
                            actionText = "다시 시도",
                            onActionClick = viewModel::retry,
                            modifier = Modifier
                                .weight(1f)
                                .width(312.dp)
                        )
                    }

                    state.filteredSounds.isEmpty() -> {
                        SoundMessage(
                            message = if (state.searchQuery.isBlank()) {
                                "등록된 소리가 없어요."
                            } else {
                                "검색 결과가 없어요."
                            },
                            modifier = Modifier
                                .weight(1f)
                                .width(312.dp)
                        )
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .width(312.dp)
                        ) {
                            errorMessage?.let { message ->
                                Text(
                                    text = message,
                                    style = bodyTextMdMedium,
                                    color = AppTheme.palette.primary.getColor(3),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }

                            SoundList(
                                sounds = state.filteredSounds,
                                downloadingSoundIds = state.downloadingSoundIds,
                                hasNextPage = state.hasNextSoundsPage,
                                isLoadingMore = state.isLoadingMoreSounds,
                                onLoadMore = viewModel::loadMoreSounds,
                                onItemClick = { sound ->
                                    viewModel.openDetail(sound.id, SoundDestination.LIST)
                                },
                                onToggleLike = viewModel::toggleLike,
                                onToggleBookmark = viewModel::toggleBookmark,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SoundMessage(
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = bodyTextMdMedium,
            color = AppTheme.palette.gray.getColor(4)
        )

        actionText?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                style = bodyTextMdMedium,
                color = AppTheme.palette.primary.getColor(3),
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
fun SoundList(
    sounds: List<SoundItem>,
    downloadingSoundIds: Set<String> = emptySet(),
    hasNextPage: Boolean = false,
    isLoadingMore: Boolean = false,
    onLoadMore: () -> Unit = {},
    onItemClick: (SoundItem) -> Unit,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sounds, key = { it.id }) { sound ->
            SoundItem(
                sound = sound,
                onToggleLike = onToggleLike,
                onToggleBookmark = onToggleBookmark,
                isDownloading = sound.id in downloadingSoundIds,
                onClick = { onItemClick(sound) }
            )
        }

        if (hasNextPage || isLoadingMore) {
            item(key = "sound-list-load-more") {
                LaunchedEffect(hasNextPage, isLoadingMore) {
                    if (hasNextPage && !isLoadingMore) {
                        onLoadMore()
                    }
                }
                Box(
                    modifier = Modifier
                        .width(312.dp)
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoadingMore) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = AppTheme.palette.primary.getColor(3),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=441")
@Composable
private fun PauzeSoundScreenPreview() {
    val previewViewModel = remember {
        PauzeSoundViewModel(
            repository = PreviewPauzeSoundRepository,
            pauzeUsageRepository = com.example.pauze.ui.pauze.PreviewPauzeUsageRepository
        )
    }

    MainPaletteTheme {
        PauzeSoundScreen(viewModel = previewViewModel)
    }
}

internal object PreviewPauzeSoundRepository : PauzeSoundRepository {
    override suspend fun getSounds(
        category: SoundCategory,
        cursor: String?
    ): AudioGuidePageDto = AudioGuidePageDto(
        content = Sounds.items
            .filter { category == SoundCategory.ALL || it.category == category.displayName }
            .map(SoundItem::toAudioGuideDto),
        nextCursor = null,
        hasNext = false
    )

    override suspend fun getLikedSounds(cursor: String?): AudioGuidePageDto = AudioGuidePageDto(
        content = Sounds.items.filter(SoundItem::isLiked).map(SoundItem::toAudioGuideDto),
        nextCursor = null,
        hasNext = false
    )

    override suspend fun toggleLike(soundId: String): AudioLikeToggleResultDto =
        AudioLikeToggleResultDto(
            audioId = soundId.toLongOrNull() ?: 0L,
            isLiked = true
        )

    override suspend fun getDownloadedSounds(): List<SoundItem> = emptyList()

    override suspend fun downloadSound(sound: SoundItem): String =
        "/preview/${sound.id}.mp3"

    override suspend fun deleteDownloadedSound(soundId: String) = Unit
}

private fun SoundItem.toAudioGuideDto(): AudioGuideDto = AudioGuideDto(
    audioId = id.toLongOrNull() ?: 0L,
    audioTitle = title,
    categoryCode = SoundCategory.entries.firstOrNull { it.displayName == category }
        ?.name
        ?: category,
    audioUrl = audioUrl,
    isLiked = isLiked
)

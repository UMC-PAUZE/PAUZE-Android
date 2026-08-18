package com.example.pauze.ui.pauze.sound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pauze.R
import com.example.pauze.data.model.SoundStashTab
import com.example.pauze.ui.component.SearchBar
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.pauze.PreviewPauzeUsageRepository
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextMdMedium

@Composable
fun PauzeSoundStashScreen(
    viewModel: PauzeSoundViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState.data

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            title = "보관함",
            showBackButton = true,
            onBackClick = { viewModel.navigateTo(SoundDestination.LIST) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = state.stashSearchQuery,
            onQueryChange = viewModel::updateStashSearchQuery,
            modifier = Modifier.size(312.dp, 48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.width(312.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(SoundStashTab.LIKED, SoundStashTab.SAVED).forEach { tab ->
                val text = if (tab == SoundStashTab.LIKED) "좋아요" else "저장"
                Tab(
                    text = text,
                    selected = state.selectedStashTab == tab,
                    onClick = { viewModel.selectStashTab(tab) },
                    modifier = Modifier.size(width = 148.dp, height = 40.dp),
                    icon = if (tab == SoundStashTab.LIKED) {
                        painterResource(R.drawable.ic_heart_off)
                    } else {
                        painterResource(R.drawable.ic_download)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        val isLikedTab = state.selectedStashTab == SoundStashTab.LIKED
        when {
            isLikedTab && state.isLikedListLoading && state.filteredStashSounds.isEmpty() -> {
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

            isLikedTab && state.likedListError != null && state.filteredStashSounds.isEmpty() -> {
                SoundMessage(
                    message = state.likedListError,
                    actionText = "다시 시도",
                    onActionClick = viewModel::retryLikedSounds,
                    modifier = Modifier
                        .weight(1f)
                        .width(312.dp)
                )
            }

            state.filteredStashSounds.isEmpty() -> {
                SoundMessage(
                    message = if (state.stashSearchQuery.isBlank()) {
                        if (isLikedTab) "좋아요한 소리가 없어요." else "저장한 소리가 없어요."
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
                    if (isLikedTab) {
                        state.likedListError?.let { message ->
                            Text(
                                text = message,
                                style = bodyTextMdMedium,
                                color = AppTheme.palette.primary.getColor(3),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    SoundList(
                        sounds = state.filteredStashSounds,
                        downloadingSoundIds = state.downloadingSoundIds,
                        hasNextPage = isLikedTab && state.hasNextLikedSoundsPage,
                        isLoadingMore = isLikedTab && state.isLoadingMoreLikedSounds,
                        onLoadMore = viewModel::loadMoreLikedSounds,
                        onItemClick = { sound ->
                            viewModel.openDetail(sound.id, SoundDestination.STASH)
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

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=441")
@Composable
private fun PauzeSoundStashScreenPreview() {
    val previewViewModel = remember {
        PauzeSoundViewModel(
            repository = PreviewPauzeSoundRepository,
            pauzeUsageRepository = PreviewPauzeUsageRepository
        )
    }

    MainPaletteTheme {
        PauzeSoundStashScreen(viewModel = previewViewModel)
    }
}

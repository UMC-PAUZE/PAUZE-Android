package com.example.pauze.ui.pauze

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.R
import com.example.pauze.data.model.SoundItem
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
    viewModel: PauzeSoundViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var currentDestination by remember { mutableStateOf(SoundDestination.LIST) }
    var detailOrigin by remember { mutableStateOf(SoundDestination.LIST) }
    var selectedSoundId by remember { mutableStateOf<String?>(null) }
    val categories = listOf("전체", "자연소리", "ASMR", "노이즈")

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

    val selectedSound = state.sounds.firstOrNull { it.id == selectedSoundId }
    when {
        selectedSound != null -> {
            PauzeSoundDetailScreen(
                sound = selectedSound,
                onToggleLike = viewModel::toggleLike,
                onToggleBookmark = viewModel::toggleBookmark,
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
                    categories.forEach { category ->
                        val isSelected = state.selectedCategory == category
                        val buttonWidth = when (category) {
                            "전체" -> 50.dp
                            "자연소리" -> 75.dp
                            "ASMR" -> 62.dp
                            else -> 63.dp
                        }
                        Box(
                            modifier = Modifier
                                .size(buttonWidth, 34.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) AppTheme.palette.primary.getColor(3)
                                    else Color.Transparent
                                )
                                .border(
                                    if (isSelected) BorderStroke(0.dp, Color.Transparent)
                                    else BorderStroke(1.dp, AppTheme.palette.gray.getColor(7)),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.selectCategory(category) }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category,
                                style = bodyTextMdMedium,
                                color = if (isSelected) AppTheme.palette.base.getColor(0)
                                else AppTheme.palette.gray.getColor(4)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                SoundList(
                    sounds = state.filteredSounds,
                    onItemClick = { sound ->
                        viewModel.openDetail(sound.id, SoundDestination.LIST)
                    },
                    onToggleLike = viewModel::toggleLike,
                    onToggleBookmark = viewModel::toggleBookmark,
                    modifier = Modifier
                        .weight(1f)
                        .width(312.dp)
                )
            }
        }
    }
}

@Composable
fun SoundList(
    sounds: List<SoundItem>,
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
                onClick = { onItemClick(sound) }
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=441")
@Composable
private fun PauzeSoundScreenPreview() {
    val previewViewModel = remember { PauzeSoundViewModel() }

    MainPaletteTheme {
        PauzeSoundScreen(viewModel = previewViewModel)
    }
}

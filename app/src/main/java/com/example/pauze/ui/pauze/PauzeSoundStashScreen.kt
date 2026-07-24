package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.SearchBar
import com.example.pauze.ui.component.SoundItem
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme

@Composable
fun PauzeSoundStashScreen(
    viewModel: PauzeSoundViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(SoundStashTab.LIKED, SoundStashTab.SAVED).forEach { tab ->
                val text = if (tab == SoundStashTab.LIKED) "좋아요" else "저장"
                Tab(
                    text = text,
                    selected = state.selectedStashTab == tab,
                    onClick = { viewModel.selectStashTab(tab) },
                    modifier = Modifier.weight(1f),
                    icon = if (tab == SoundStashTab.LIKED) {
                        painterResource(R.drawable.ic_heart_off)
                    } else {
                        painterResource(R.drawable.ic_download)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .width(312.dp),
            contentPadding = PaddingValues(vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.filteredStashSounds, key = { it.id }) { sound ->
                SoundItem(
                    sound = sound,
                    onToggleLike = viewModel::toggleLike,
                    onToggleBookmark = viewModel::toggleBookmark
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=441")
@Composable
private fun PauzeSoundStashScreenPreview() {
    val previewViewModel = remember { PauzeSoundViewModel() }

    MainPaletteTheme {
        PauzeSoundStashScreen(viewModel = previewViewModel)
    }
}

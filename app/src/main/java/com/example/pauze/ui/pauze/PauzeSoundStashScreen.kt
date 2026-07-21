package com.example.pauze.ui.pauze

import com.example.pauze.data.model.SoundItem
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.SearchBar
import com.example.pauze.ui.component.SoundItem
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.*

@Composable
fun PauzeSoundStashScreen(
    soundList: List<SoundItem>,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    // 탭 상태 관리: "좋아요" 또는 "저장"
    var selectedTab by remember { mutableStateOf("좋아요") }

    // 선택된 탭과 검색어에 따른 필터링 로직
    val filteredSounds = soundList.filter { item ->
        val matchesTab = if (selectedTab == "좋아요") item.isLiked else item.isBookmarked
        val matchesSearch = item.title.contains(searchQuery, ignoreCase = true)
        matchesTab && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. 헤더 영역 (공통 TopBar 컴포넌트 사용) ---
        TopBar(
            title = "보관함",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. 검색창 영역 ---
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier.size(312.dp, 48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- 3. 상단 탭 영역 ---
        Row(
            modifier = Modifier.width(312.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("좋아요", "저장").forEach { tab ->
                Tab(
                    text = tab,
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    modifier = Modifier.weight(1f),
                    leadingIconResId = if (tab == "좋아요") {
                        R.drawable.ic_heart_off
                    } else {
                        R.drawable.ic_download
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. 필터링된 보관함 소리 목록 리스트 ---
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .width(312.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSounds, key = { it.id }) { sound ->
                SoundItem(
                    sound = sound,
                    onToggleLike = onToggleLike,
                    onToggleBookmark = onToggleBookmark
                )
            }
        }
    }
}

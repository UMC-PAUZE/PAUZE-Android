package com.example.pauze.ui.sound

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
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
    // 탭 상태 관리: "좋아요" 또는 "스크랩"
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
            .background(AppTheme.palette.base.getColor(0))
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. 헤더 영역 ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 17.dp, top = 40.dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "뒤로가기",
                    tint = AppTheme.palette.gray.getColor(0),
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 149.dp, top = 40.dp)
                    .size(62.dp, 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "보관함",
                    style = headingSmBold,
                    color = AppTheme.palette.gray.getColor(0)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 2. 검색창 영역 ---
        Box(
            modifier = Modifier
                .size(312.dp, 48.dp)
                .clip(CircleShape)
                .background(AppTheme.palette.gray.getColor(9))
                .border(BorderStroke(1.dp, AppTheme.palette.gray.getColor(8)), CircleShape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = bodyTextMdRegular.copy(color = AppTheme.palette.gray.getColor(0)),
                    cursorBrush = SolidColor(AppTheme.palette.gray.getColor(0)),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "원하는 소리를 검색해보세요",
                                style = bodyTextMdRegular,
                                color = AppTheme.palette.gray.getColor(5)
                            )
                        }
                        innerTextField()
                    }
                )
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_search),
                    contentDescription = "검색",
                    tint = AppTheme.palette.gray.getColor(5),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. 상단 탭 영역 (좋아요 / 스크랩 고정 분할 비율 반영) ---
        Row(
            modifier = Modifier.width(312.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("좋아요", "스크랩").forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .size(150.dp, 40.dp) // 균등 분할 배치 크기
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) AppTheme.palette.gray.getColor(8) // 스크런샷의 선택된 회색 배경 적용
                            else Color.Transparent
                        )
                        .clickable { selectedTab = tab },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        style = bodyTextMdMedium,
                        color = if (isSelected) AppTheme.palette.gray.getColor(0)
                        else AppTheme.palette.gray.getColor(4)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 4. 필터링된 보관함 소리 목록 리스트 ---
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .width(312.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSounds, key = { it.id }) { sound ->
                Row(
                    modifier = Modifier
                        .size(312.dp, 80.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(AppTheme.palette.gray.getColor(8))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = sound.imageResId),
                            contentDescription = sound.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = sound.title,
                            style = bodyTextLgBold,
                            color = AppTheme.palette.gray.getColor(0)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = sound.category,
                            style = bodyTextSmRegular,
                            color = AppTheme.palette.gray.getColor(5)
                        )
                    }

                    IconButton(
                        onClick = { onToggleLike(sound.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (sound.isLiked) R.drawable.ic_heart_on else R.drawable.ic_heart_off
                            ),
                            contentDescription = "좋아요",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { onToggleBookmark(sound.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (sound.isBookmarked) R.drawable.ic_bookmark_on else R.drawable.ic_bookmark_off
                            ),
                            contentDescription = "북마크",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
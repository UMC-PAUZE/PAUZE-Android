package com.example.pauze.ui.pauze

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.*
// [해결포인트] 다른 패키지(com.example.pauze.ui.sound)에 정의된 상세화면 컴포저블을 올바르게 import합니다.
import com.example.pauze.ui.pauze.PauzeSoundDetailScreen

// 소리 아이템 데이터 구조 (이미지 리소스 ID 필수 적용)
data class SoundItem(
    val id: String,
    val title: String,
    val category: String,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val imageResId: Int
)

@Composable
fun PauzeSoundScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    // 1. 현재 화면 상태 관리 ("list" = 메인 리스트, "stash" = 보관함, "detail" = 상세 재생 화면)
    var currentScreen by remember { mutableStateOf("list") }

    // 상세 화면으로 진입하기 직전의 화면을 기억 (상세 화면에서 뒤로 가기 시 복원 목적)
    var previousScreen by remember { mutableStateOf("list") }

    // 2. 선택된 음원 아이템 상태 관리 (상세 화면용)
    var selectedSound by remember { mutableStateOf<SoundItem?>(null) }

    // 3. 검색어 상태 관리
    var searchQuery by remember { mutableStateOf("") }

    // 4. 카테고리 상태 관리 (메인 리스트용)
    val categories = listOf("전체", "자연소리", "ASMR", "노이즈")
    var selectedCategory by remember { mutableStateOf("전체") }

    // 5. 음원 데이터 리스트 상태 관리 (공통 원천 데이터)
    var soundList by remember {
        mutableStateOf(
            listOf(
                SoundItem(id = "1", title = "빗소리", category = "자연소리", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_rain),
                SoundItem(id = "2", title = "파도소리", category = "자연소리", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_empty_image),
                SoundItem(id = "3", title = "시냇물소리", category = "자연소리", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_empty_image),
                SoundItem(id = "4", title = "천둥소리", category = "자연소리", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_empty_image),
                SoundItem(id = "5", title = "바람소리", category = "자연소리", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_empty_image),
                SoundItem(id = "6", title = "ASMR 속삭임", category = "ASMR", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_empty_image),
                SoundItem(id = "7", title = "백색소음", category = "노이즈", isLiked = false, isBookmarked = false, imageResId = R.drawable.ic_empty_image)
            )
        )
    }

    // =================--- 화면 전환 조건 분기 ---=================
    when (currentScreen) {
        "detail" -> {
            if (selectedSound != null) {
                // 상세 화면 상태일 때, 실시간 동기화된 원본 리스트의 최신 데이터 획득
                val currentDetailSound = soundList.firstOrNull { it.id == selectedSound!!.id } ?: selectedSound!!

                PauzeSoundDetailScreen(
                    sound = currentDetailSound,
                    onToggleLike = { id ->
                        soundList = soundList.map {
                            if (it.id == id) it.copy(isLiked = !it.isLiked) else it
                        }
                    },
                    onToggleBookmark = { id ->
                        soundList = soundList.map {
                            if (it.id == id) it.copy(isBookmarked = !it.isBookmarked) else it
                        }
                    },
                    onBackClick = {
                        currentScreen = previousScreen // 상세 화면에서 빠져나갈 때 직전 화면 상태로 복원
                    },
                    modifier = modifier
                )
            }
        }
        "stash" -> {
            PauzeSoundStashScreen(
                soundList = soundList,
                onToggleLike = { id ->
                    soundList = soundList.map {
                        if (it.id == id) it.copy(isLiked = !it.isLiked) else it
                    }
                },
                onToggleBookmark = { id ->
                    soundList = soundList.map {
                        if (it.id == id) it.copy(isBookmarked = !it.isBookmarked) else it
                    }
                },
                onBackClick = {
                    currentScreen = "list" // 보관함에서 뒤로가기 누르면 메인 리스트로 복귀
                },
                modifier = modifier
            )
        }
        else -> {
            // "list" -> 기본 청각안정 메인 리스트 화면
            val filteredSounds = soundList.filter { item ->
                val matchesCategory = (selectedCategory == "전체" || item.category == selectedCategory)
                val matchesSearch = item.title.contains(searchQuery, ignoreCase = true)
                matchesCategory && matchesSearch
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(AppTheme.palette.base.getColor(0))
            ) {
                // =================--- 1. 헤더 영역 (공통 TopBar 컴포넌트 사용) ---=================
                TopBar(
                    title = "청각 안정",
                    showBackButton = true,
                    onBackClick = { onBackClick() }, // 메인 리스트 상태면 외부 뒤로가기 동작 실행
                    rightIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_box),
                            contentDescription = "보관함",
                            tint = AppTheme.palette.gray.getColor(2),
                            modifier = Modifier.clickable { currentScreen = "stash" }
                        )
                    }
                )

                // =================--- 2. 검색창 영역 ---=================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .height(48.dp)
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

                // =================--- 3. 조건별 서브 필터/칩 영역 ---=================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
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
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
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

                // =================--- 4. 소리 리스트 출력 영역 ---=================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredSounds, key = { it.id }) { sound ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(AppTheme.palette.gray.getColor(8))
                                .clickable {
                                    previousScreen = "list"
                                    selectedSound = sound
                                    currentScreen = "detail"
                                }
                                .padding(16.dp),
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

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = sound.title,
                                    style = bodyTextLgBold,
                                    color = AppTheme.palette.gray.getColor(0)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sound.category,
                                    style = bodyTextSmRegular,
                                    color = AppTheme.palette.gray.getColor(5)
                                )
                            }

                            // 하트 (좋아요) 토글 버튼
                            IconButton(
                                onClick = {
                                    soundList = soundList.map {
                                        if (it.id == sound.id) it.copy(isLiked = !it.isLiked) else it
                                    }
                                }
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

                            // 북마크 토글 버튼
                            IconButton(
                                onClick = {
                                    soundList = soundList.map {
                                        if (it.id == sound.id) it.copy(isBookmarked = !it.isBookmarked) else it
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_download),
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
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1C18)
@Composable
fun PauzeSoundScreenPreview() {
    MainPaletteTheme {
        PauzeSoundScreen()
    }
}
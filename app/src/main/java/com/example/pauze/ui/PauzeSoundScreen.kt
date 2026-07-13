package com.example.pauze.ui

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
import androidx.compose.ui.res.painterResource
import com.example.pauze.R
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.*

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
    onBackClick: () -> Unit = {},
    onStorageClick: () -> Unit = {}
) {
    // 1. 검색어 상태 관리
    var searchQuery by remember { mutableStateOf("") }

    // 2. 카테고리 상태 관리
    val categories = listOf("전체", "자연소리", "ASMR", "노이즈")
    var selectedCategory by remember { mutableStateOf("전체") }

    // 3. 음원 데이터 리스트 상태 관리
    var soundList by remember {
        mutableStateOf(
            listOf(
                SoundItem(
                    id = "1",
                    title = "빗소리",
                    category = "자연소리",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_rain
                ),
                SoundItem(
                    id = "2",
                    title = "파도소리",
                    category = "자연소리",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_empty_image
                ),
                SoundItem(
                    id = "3",
                    title = "시냇물소리",
                    category = "자연소리",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_empty_image
                ),
                SoundItem(
                    id = "4",
                    title = "천둥소리",
                    category = "자연소리",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_empty_image
                ),
                SoundItem(
                    id = "5",
                    title = "바람소리",
                    category = "자연소리",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_empty_image
                ),
                SoundItem(
                    id = "6",
                    title = "ASMR 속삭임",
                    category = "ASMR",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_empty_image
                ),
                SoundItem(
                    id = "7",
                    title = "백색소음",
                    category = "노이즈",
                    isLiked = false,
                    isBookmarked = false,
                    imageResId = R.drawable.ic_empty_image
                )
            )
        )
    }

    // 4. 필터링 로직
    val filteredSounds = soundList.filter { item ->
        val matchesCategory = (selectedCategory == "전체" || item.category == selectedCategory)
        val matchesSearch = item.title.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)) // theme 배경색 (어두운 회색)
            .statusBarsPadding()
    ) {
        // --- 1. 헤더 영역 ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "뒤로가기",
                    tint = AppTheme.palette.gray.getColor(0) // 밝은 색상
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "청각 안정",
                style = headingSmBold,
                color = AppTheme.palette.gray.getColor(0)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onStorageClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_box),
                    contentDescription = "보관함",
                    tint = AppTheme.palette.gray.getColor(0)
                )
            }
        }

        // --- 2. 검색창 영역 ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(48.dp)
                .clip(CircleShape)
                .background(AppTheme.palette.gray.getColor(9)) // 매우 어두운 회색
                .border(
                    BorderStroke(1.dp, AppTheme.palette.gray.getColor(8)),
                    CircleShape
                )
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
                                color = AppTheme.palette.gray.getColor(5) // 가이드 색상 (중간 회색)
                            )
                        }
                        innerTextField()
                    }
                )
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_search), // 내장 리소스 직접 참조
                    contentDescription = "검색",
                    tint = AppTheme.palette.gray.getColor(5),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // --- 3. 카테고리 필터 칩 영역 ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) AppTheme.palette.primary.getColor(3) // 선택된 칩: 민트/세이지 연두색
                            else Color.Transparent
                        )
                        .border(
                            if (isSelected) BorderStroke(0.dp, Color.Transparent)
                            else BorderStroke(1.dp, AppTheme.palette.gray.getColor(7)), // 선택 안 됨: 어두운 회색 테두리
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        style = bodyTextMdMedium,
                        color = if (isSelected) AppTheme.palette.base.getColor(0) // 선택되면 글씨 어둡게
                        else AppTheme.palette.gray.getColor(4) // 선택 안 되면 글씨 밝은 회색
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- 4. 소리 목록 리스트 ---
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
                        .background(AppTheme.palette.gray.getColor(8)) // 카드 배경색 (진한 어두운 회색)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 음원 썸네일 컨테이너 영역 (60dp 고정)
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (sound.id == "1") {
                            // 1번 빗소리: 고유 텍스처를 잃지 않도록 Image 컴포넌트로 꽉 채우기
                            Image(
                                painter = painterResource(id = sound.imageResId),
                                contentDescription = sound.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // 나머지 기본 아이템: ic_empty_image 자체가 프레임 형태이므로,
                            // 내부에 작게 갇히지 않도록 Image 컴포넌트로 변경 후 fillMaxSize() 적용
                            Image(
                                painter = painterResource(id = sound.imageResId),
                                contentDescription = sound.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 텍스트 정보
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = sound.title,
                            style = bodyTextLgBold,
                            color = AppTheme.palette.gray.getColor(0) // 완전 흰색에 가까운 색
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sound.category,
                            style = bodyTextSmRegular,
                            color = AppTheme.palette.gray.getColor(5) // 보조적인 연한 회색
                        )
                    }

                    // 하트 (좋아요) 버튼
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

                    // 북마크 버튼
                    IconButton(
                        onClick = {
                            soundList = soundList.map {
                                if (it.id == sound.id) it.copy(isBookmarked = !it.isBookmarked) else it
                            }
                        }
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

@Preview(showBackground = true, backgroundColor = 0xFF1B1C18)
@Composable
fun PauzeSoundScreenPreview() {
    MainPaletteTheme {
        PauzeSoundScreen()
    }
}
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

    // 5. 보관함 탭 상태 관리 ("좋아요" 또는 "스크랩")
    var selectedStashTab by remember { mutableStateOf("좋아요") }

    // 6. 음원 데이터 리스트 상태 관리 (공통 원천 데이터)
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

    // 7. 현재 화면 구분에 따른 필터링 로직 분기
    val filteredSounds = soundList.filter { item ->
        val matchesSearch = item.title.contains(searchQuery, ignoreCase = true)
        if (currentScreen == "list" || (currentScreen == "detail" && previousScreen == "list")) {
            // 메인 리스트 기준: 카테고리 매칭 + 검색어
            val matchesCategory = (selectedCategory == "전체" || item.category == selectedCategory)
            matchesCategory && matchesSearch
        } else {
            // 보관함 기준: 탭 분류(좋아요/스크랩) + 검색어
            val matchesTab = if (selectedStashTab == "좋아요") item.isLiked else item.isBookmarked
            matchesTab && matchesSearch
        }
    }

    // =================--- 화면 전환 조건 분기 ---=================
    if (currentScreen == "detail" && selectedSound != null) {
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
    } else {
        // 기존 메인 리스트 및 보관함 화면 레이아웃
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppTheme.palette.base.getColor(0)) // theme 배경색 (어두운 회색)
        ) {
            // =================--- 1. 헤더 영역 (공통 TopBar 컴포넌트 사용) ---=================
            TopBar(
                title = if (currentScreen == "list") "청각 안정" else "보관함",
                showBackButton = true,
                onBackClick = {
                    if (currentScreen == "stash") {
                        currentScreen = "list" // 보관함 상태에서 뒤로가기 누르면 메인으로
                    } else {
                        onBackClick() // 메인 리스트 상태면 외부 뒤로가기 동작 실행
                    }
                },
                rightIcon = if (currentScreen == "list") {
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_box),
                            contentDescription = "보관함",
                            tint = AppTheme.palette.gray.getColor(2),
                            modifier = Modifier.clickable { currentScreen = "stash" }
                        )
                    }
                } else null
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

            // =================--- 3. 조건별 서브 필터/탭 영역 ---=================
            if (currentScreen == "list") {
                // [메인 리스트 모드] 카테고리 칩 목록
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
            } else {
                // [보관함 모드] 가로분할 좋아요 / 스크랩 탭 목록
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("좋아요", "스크랩").forEach { tab ->
                        val isSelected = selectedStashTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) AppTheme.palette.gray.getColor(8)
                                    else Color.Transparent
                                )
                                .clickable { selectedStashTab = tab },
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
            }

            Spacer(modifier = Modifier.height(36.dp))

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
                            // 리스트 카드 영역 클릭 시 상세 화면으로 이동하는 로직 추가
                            .clickable {
                                previousScreen = currentScreen // 현재 화면 상태 백업 ("list" 또는 "stash")
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
}

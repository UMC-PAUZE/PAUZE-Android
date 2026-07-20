package com.example.pauze.ui.pauze

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.*

@Composable
fun PauzeSoundDetailScreen(
    sound: SoundItem,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black) // 이미지 로드 전 기본 배경색
    ) {
        // 1. 상단 여백/헤더 영역 (공통 TopBar 컴포넌트 사용)
        // TopBar 자체가 80dp 고정 높이 + 상태바 여백을 포함하고 있어 별도 statusBarsPadding()이 필요 없다.
        TopBar(
            title = "",
            showBackButton = true,
            onBackClick = onBackClick
        )

        // 2. 나머지 영역 (배경 이미지 + 하단 카드)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // 배경 이미지
            Image(
                painter = painterResource(id = sound.imageResId),
                contentDescription = "${sound.title} 배경 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 어두운 오버레이 필터 (하단 카드의 가독성을 높이기 위함)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            // 3. 하단 카드 영역 (곡 정보 및 컨트롤러)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(AppTheme.palette.base.getColor(0)) // 레퍼런스 이미지의 다크 테마 카드 배경색 적용 (불투명)
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 상단 곡 정보 + 좋아요/북마크 버튼 영역
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 타이틀 및 카테고리
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = sound.title,
                                style = headingSmBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sound.category,
                                style = bodyTextSmRegular,
                                color = AppTheme.palette.gray.getColor(4) // 연한 회색 톤
                            )
                        }

                        // 하트 버튼 (상태 토글)
                        IconButton(
                            onClick = { onToggleLike(sound.id) },
                            modifier = Modifier.size(32.dp)
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

                        Spacer(modifier = Modifier.width(12.dp))

                        // 북마크 버튼 (상태 토글)
                        IconButton(
                            onClick = { onToggleBookmark(sound.id) },
                            modifier = Modifier.size(32.dp)
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

                    Spacer(modifier = Modifier.height(32.dp))

                    // 하단 재생 컨트롤러 (비활성 UI 단독)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 이전 곡 버튼
                        IconButton(
                            onClick = { /* 무반응 */ },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sound_back),
                                contentDescription = "이전곡",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        // 재생/일시정지 원형 버튼
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { /* 무반응 */ },
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sound_play),
                                    contentDescription = "재생",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        // 다음 곡 버튼
                        IconButton(
                            onClick = { /* 무반응 */ },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sound_next),
                                contentDescription = "다음곡",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=441")
@Composable
fun PauzeSoundDetailScreenPreview() {
    var mockSound by remember {
        mutableStateOf(
            SoundItem(
                id = "1",
                title = "빗소리",
                category = "자연소리",
                isLiked = true,
                isBookmarked = false,
                imageResId = R.drawable.ic_rain
            )
        )
    }

    MainPaletteTheme {
        PauzeSoundDetailScreen(
            sound = mockSound,
            onToggleLike = { mockSound = mockSound.copy(isLiked = !mockSound.isLiked) },
            onToggleBookmark = { mockSound = mockSound.copy(isBookmarked = !mockSound.isBookmarked) },
            onBackClick = {}
        )
    }
}
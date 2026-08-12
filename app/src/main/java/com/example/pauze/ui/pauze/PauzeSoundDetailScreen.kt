package com.example.pauze.ui.pauze

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.model.SoundItem
import com.example.pauze.ui.component.SoundPlay
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.*

@Composable
fun PauzeSoundDetailScreen(
    sound: SoundItem,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDownloading: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black) // 이미지 로드 전 기본 배경색
    ) {
        // 1. 배경 이미지
        Image(
            painter = painterResource(id = sound.imageResId),
            contentDescription = "${sound.title} 배경 이미지",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. 어두운 오버레이 필터 (하단 카드의 가독성을 높이기 위함)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // 3. 투명한 상단 바를 배경 이미지 위에 겹쳐 표시
        TopBar(
            title = "",
            showBackButton = true,
            onBackClick = onBackClick,
            backgroundColor = Color.Transparent,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(start = 36.dp, top = 112.dp, end = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sound.title,
                    style = headingSmBold,
                    color = AppTheme.palette.gray.getColor(1)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = sound.category,
                    style = bodyTextSmRegular,
                    color = AppTheme.palette.gray.getColor(4)
                )
            }

            IconButton(
                onClick = { onToggleLike(sound.id) },
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (sound.isLiked) R.drawable.ic_heart_on else R.drawable.ic_heart_off
                    ),
                    contentDescription = "좋아요",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { onToggleBookmark(sound.id) },
                enabled = !isDownloading,
                modifier = Modifier.size(44.dp)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AppTheme.palette.primary.getColor(3),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        painter = painterResource(
                            if (sound.isBookmarked) R.drawable.ic_downloaded else R.drawable.ic_download
                        ),
                        contentDescription = if (sound.isBookmarked) "다운로드 완료" else "다운로드",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // 4. 하단 타이머 및 재생 컨트롤러
        SoundPlay(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
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

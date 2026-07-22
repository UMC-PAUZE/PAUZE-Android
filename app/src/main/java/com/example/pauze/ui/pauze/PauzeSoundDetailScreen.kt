package com.example.pauze.ui.pauze

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
    modifier: Modifier = Modifier
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

        // 4. 하단 카드 영역 (곡 정보 및 컨트롤러)
        SoundPlay(
            sound = sound,
            onToggleLike = onToggleLike,
            onToggleBookmark = onToggleBookmark,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
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

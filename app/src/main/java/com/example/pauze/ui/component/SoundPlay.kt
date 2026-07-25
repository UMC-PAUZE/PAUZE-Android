package com.example.pauze.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.model.SoundItem
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.headingSmBold

/** 소리 상세 화면 하단의 곡 정보와 재생 컨트롤 카드입니다. */
@Composable
fun SoundPlay(
    sound: SoundItem,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(AppTheme.palette.base.getColor(0))
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sound.title,
                        style = headingSmBold,
                        color = Color.White
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
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (sound.isLiked) R.drawable.ic_heart_on else R.drawable.ic_heart_off
                        ),
                        contentDescription = "좋아요",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = { onToggleBookmark(sound.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        contentDescription = "저장",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.sound_back),
                        contentDescription = "이전곡",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onPlayClick, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            painter = painterResource(R.drawable.sound_play),
                            contentDescription = "재생",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = onNextClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.sound_next),
                        contentDescription = "다음곡",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

package com.example.pauze.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmBold
import com.example.pauze.ui.theme.bodyTextSmRegular

private val timerOptions = listOf(
    "없음" to 50.dp,
    "10분" to 53.dp,
    "30분" to 53.dp,
    "1시간" to 58.dp
)

/** 소리 상세 화면 하단의 타이머와 재생 컨트롤입니다. */
@Composable
fun SoundPlay(
    modifier: Modifier = Modifier,
    progress: Float = 0.35f,
    currentTime: String = "03:32",
    totalTime: String = "10:00",
    onPreviousClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    var selectedTimerIndex by rememberSaveable { mutableIntStateOf(0) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        AppTheme.palette.base.getColor(0).copy(alpha = 0.94f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    start = 28.dp,
                    top = 72.dp,
                    end = 28.dp,
                    bottom = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timerOptions.forEachIndexed { index, (label, width) ->
                    val isSelected = selectedTimerIndex == index

                    Box(
                        modifier = Modifier
                            .size(width = width, height = 34.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) {
                                        AppTheme.palette.gray.getColor(1)
                                    } else {
                                        AppTheme.palette.gray.getColor(4)
                                    }
                                ),
                                shape = CircleShape
                            )
                            .clickable { selectedTimerIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = if (isSelected) bodyTextSmBold else bodyTextSmRegular,
                            color = if (isSelected) {
                                AppTheme.palette.gray.getColor(1)
                            } else {
                                AppTheme.palette.gray.getColor(4)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(AppTheme.palette.gray.getColor(8))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(AppTheme.palette.gray.getColor(1))
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentTime,
                    style = bodyTextSmRegular,
                    color = AppTheme.palette.gray.getColor(4)
                )
                Text(
                    text = totalTime,
                    style = bodyTextSmRegular,
                    color = AppTheme.palette.gray.getColor(4)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.sound_back),
                        contentDescription = "이전 소리",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        onPlayClick()
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                        ),
                        contentDescription = if (isPlaying) "일시정지" else "재생",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                IconButton(onClick = onNextClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.sound_next),
                        contentDescription = "다음 소리",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

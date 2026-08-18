package com.example.pauze.ui.component

import android.os.SystemClock
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmBold
import com.example.pauze.ui.theme.bodyTextSmRegular
import kotlinx.coroutines.delay

private data class TimerOption(
    val label: String,
    val width: Dp,
    val maximumTime: String?,
    val durationMillis: Long?
)

private val timerOptions = listOf(
    TimerOption(label = "없음", width = 50.dp, maximumTime = null, durationMillis = null),
    TimerOption(label = "10분", width = 53.dp, maximumTime = "10:00", durationMillis = 10 * 60_000L),
    TimerOption(label = "30분", width = 53.dp, maximumTime = "30:00", durationMillis = 30 * 60_000L),
    TimerOption(label = "1시간", width = 58.dp, maximumTime = "1:00:00", durationMillis = 60 * 60_000L)
)

/** 소리 상세 화면 하단의 타이머와 재생 컨트롤입니다. */
@Composable
fun SoundPlay(
    modifier: Modifier = Modifier,
    currentTime: String = "00:00",
    isPlaying: Boolean = false,
    isPlaybackAvailable: Boolean = true,
    usageSessionId: String = "",
    onUsageQualified: () -> Unit = {},
    onTimerFinished: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    var selectedTimerIndex by rememberSaveable(usageSessionId) { mutableIntStateOf(0) }
    var timerElapsedMs by rememberSaveable(usageSessionId) { mutableLongStateOf(0L) }
    var playedSeconds by rememberSaveable(usageSessionId) { mutableIntStateOf(0) }
    var isUsageRecorded by rememberSaveable(usageSessionId) { mutableStateOf(false) }
    val currentOnUsageQualified by rememberUpdatedState(onUsageQualified)
    val currentOnTimerFinished by rememberUpdatedState(onTimerFinished)
    val selectedTimer = timerOptions[selectedTimerIndex]
    val selectedDurationMs = selectedTimer.durationMillis

    LaunchedEffect(isPlaying, selectedTimerIndex) {
        val durationMs = selectedDurationMs ?: return@LaunchedEffect
        if (!isPlaying || timerElapsedMs >= durationMs) return@LaunchedEffect

        var previousTickMs = SystemClock.elapsedRealtime()
        while (isPlaying && timerElapsedMs < durationMs) {
            delay(TIMER_UPDATE_INTERVAL_MS)

            val currentTickMs = SystemClock.elapsedRealtime()
            val playedSinceLastTickMs = currentTickMs - previousTickMs
            previousTickMs = currentTickMs
            timerElapsedMs = (timerElapsedMs + playedSinceLastTickMs).coerceAtMost(durationMs)

            if (timerElapsedMs >= durationMs) {
                currentOnTimerFinished()
                break
            }
        }
    }

    LaunchedEffect(isPlaying, isUsageRecorded) {
        while (isPlaying && !isUsageRecorded) {
            delay(1000L)
            playedSeconds++
            if (playedSeconds >= MINIMUM_USAGE_SECONDS) {
                isUsageRecorded = true
                currentOnUsageQualified()
            }
        }
    }

    val displayedProgress = selectedDurationMs?.let { durationMs ->
        (timerElapsedMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    } ?: 1f
    val displayedCurrentTime = selectedDurationMs?.let {
        formatElapsedTime(timerElapsedMs)
    } ?: currentTime

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
                timerOptions.forEachIndexed { index, option ->
                    val isSelected = selectedTimerIndex == index

                    Box(
                        modifier = Modifier
                            .size(width = option.width, height = 34.dp)
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
                            .clickable {
                                if (selectedTimerIndex != index) {
                                    selectedTimerIndex = index
                                    timerElapsedMs = 0L
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option.label,
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
                        .fillMaxWidth(displayedProgress)
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
                    text = displayedCurrentTime,
                    style = bodyTextSmRegular,
                    color = AppTheme.palette.gray.getColor(4)
                )
                timerOptions[selectedTimerIndex].maximumTime?.let { maximumTime ->
                    Text(
                        text = maximumTime,
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(4)
                    )
                }
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
                        if (!isPlaying && selectedDurationMs != null && timerElapsedMs >= selectedDurationMs) {
                            timerElapsedMs = 0L
                        }
                        onPlayClick()
                    },
                    enabled = isPlaybackAvailable,
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

private const val MINIMUM_USAGE_SECONDS = 60
private const val TIMER_UPDATE_INTERVAL_MS = 250L

private fun formatElapsedTime(elapsedMs: Long): String {
    val totalSeconds = (elapsedMs / 1_000L).coerceAtLeast(0L)
    val hours = totalSeconds / 3_600L
    val minutes = (totalSeconds % 3_600L) / 60L
    val seconds = totalSeconds % 60L

    return if (hours > 0L) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

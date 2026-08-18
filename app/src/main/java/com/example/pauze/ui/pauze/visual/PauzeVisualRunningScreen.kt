package com.example.pauze.ui.pauze.visual

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.headingLgBold
import kotlinx.coroutines.delay
import kotlin.math.ceil

@Composable
fun PauzeVisualMeditationRunningScreen(
    totalSeconds: Int,
    visualUrl: String?,
    showStopDialog: Boolean,
    onShowStopDialog: () -> Unit,
    onStopClick: () -> Unit,
    onContinueClick: () -> Unit,
    onUsageThresholdReached: () -> Unit,
    onFinish: () -> Unit,
) {
    var remainingSeconds by remember(totalSeconds) {
        mutableStateOf(totalSeconds)
    }
    var isUsageRecorded by remember(totalSeconds) {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    // 가이드 음원이 없거나 로딩에 실패한 경우에도 명상 타이머는 무음으로 계속 동작한다.
    val player = remember(visualUrl) {
        visualUrl
            ?.takeIf { it.isNotBlank() }
            ?.let { url ->
                ExoPlayer.Builder(context).build().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(C.USAGE_MEDIA)
                            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                            .build(),
                        true
                    )
                    setMediaItem(MediaItem.fromUri(url))
                    repeatMode = Player.REPEAT_MODE_ONE
                    prepare()
                }
            }
    }

    // 종료 여부를 확인하는 동안에는 음원을 멈춰 다이얼로그 뒤에서 재생되지 않게 한다.
    LaunchedEffect(player, showStopDialog) {
        if (showStopDialog) {
            player?.pause()
        } else {
            player?.play()
        }
    }

    DisposableEffect(player) {
        onDispose {
            player?.release()
        }
    }

    // 종료 확인에 걸린 시간은 실제 명상 시간에 포함하지 않는다.
    LaunchedEffect(totalSeconds, showStopDialog) {
        while (remainingSeconds > 0 && !showStopDialog) {
            delay(1000L)
            remainingSeconds = (remainingSeconds - 1).coerceAtLeast(0)
        }
    }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds == 0) {
            onFinish()
        }
    }

    val elapsedSeconds = totalSeconds - remainingSeconds
    val usageThresholdSeconds = ceil(totalSeconds * VISUAL_USAGE_RATIO).toInt()
        .coerceAtLeast(1)

    // 전체 시간의 40%에 도달한 시점만 사용 완료로 한 번 기록한다.
    LaunchedEffect(elapsedSeconds, usageThresholdSeconds) {
        if (!isUsageRecorded && elapsedSeconds >= usageThresholdSeconds) {
            isUsageRecorded = true
            onUsageThresholdReached()
        }
    }

    val minute = remainingSeconds / 60
    val second = remainingSeconds % 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(enabled = !showStopDialog) {
                onShowStopDialog()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%02d : %02d".format(minute, second),
                style = headingLgBold.copy(
                    fontSize = 64.sp,
                    lineHeight = 64.sp
                ),
                color = AppTheme.palette.gray.getColor(8)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "화면을 탭하면 종료됩니다.",
                style = bodyTextLgRegular,
                color = AppTheme.palette.gray.getColor(8)
            )
        }

        if (showStopDialog) {
            Dialog(
                title = "명상을 중단하시겠어요?",
                content = "남은 시간은 저장되지 않습니다.",
                btnCancel = "중단하기",
                btnContinue = "계속하기",
                onDismissRequest = onStopClick,
                onContinue = onContinueClick
            )
        }
    }
}

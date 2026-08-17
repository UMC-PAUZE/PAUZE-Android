package com.example.pauze.ui.pauze.visual

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.pauze.R
import com.example.pauze.data.model.BreathPhase
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.pauze.component.PauzeBreathingCircle
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingLgBold
import kotlinx.coroutines.delay
import kotlin.math.ceil

private const val INHALE_SECONDS = 4
private const val HOLD_SECONDS = 7
private const val EXHALE_SECONDS = 8
private const val BREATH_CYCLE_SECONDS = INHALE_SECONDS + HOLD_SECONDS + EXHALE_SECONDS
internal const val VISUAL_USAGE_RATIO = 0.4

@Composable
fun PauzeVisualBreathingRunningScreen(
    totalSeconds: Int,
    visualUrl: String?,
    showStopDialog: Boolean,
    onShowStopDialog: () -> Unit,
    onStopClick: () -> Unit,
    onContinueClick: () -> Unit,
    onUsageThresholdReached: () -> Unit,
    onFinish: () -> Unit
) {
    var remainingSeconds by remember(totalSeconds) {
        mutableStateOf(totalSeconds)
    }
    var isPlaying by remember {
        mutableStateOf(true)
    }
    var isUsageRecorded by remember(totalSeconds) {
        mutableStateOf(false)
    }

    val context = LocalContext.current
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

    LaunchedEffect(player, isPlaying, showStopDialog) {
        if (isPlaying && !showStopDialog) {
            player?.play()
        } else {
            player?.pause()
        }
    }

    DisposableEffect(player) {
        onDispose {
            player?.release()
        }
    }

    LaunchedEffect(totalSeconds, showStopDialog, isPlaying) {
        while (remainingSeconds > 0 && !showStopDialog && isPlaying) {
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

    LaunchedEffect(elapsedSeconds, usageThresholdSeconds) {
        if (!isUsageRecorded && elapsedSeconds >= usageThresholdSeconds) {
            isUsageRecorded = true
            onUsageThresholdReached()
        }
    }

    val breathAnimationState = calculateBreathAnimationState(elapsedSeconds)
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PauzeBreathingCircle(
                progress = breathAnimationState.progress,
                secondsText = breathAnimationState.remainingPhaseSeconds.toString(),
                showCircle = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(8),
                        shape = CircleShape
                    )
                    .clickable {
                        isPlaying = !isPlaying
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = if (isPlaying) "일시정지" else "재생",
                    tint = AppTheme.palette.gray.getColor(4),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = breathAnimationState.guideText,
                style = headingLgBold,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PauzeVisualBreathStepText(
                    label = "들숨",
                    seconds = INHALE_SECONDS,
                    isActive = breathAnimationState.phase == BreathPhase.INHALE
                )
                PauzeVisualBreathStepText(
                    label = "참기",
                    seconds = HOLD_SECONDS,
                    isActive = breathAnimationState.phase == BreathPhase.HOLD
                )
                PauzeVisualBreathStepText(
                    label = "날숨",
                    seconds = EXHALE_SECONDS,
                    isActive = breathAnimationState.phase == BreathPhase.EXHALE
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "%02d : %02d".format(minute, second),
                style = headingLgBold.copy(
                    fontSize = 48.sp,
                    lineHeight = 48.sp
                ),
                color = AppTheme.palette.gray.getColor(7)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "화면을 탭하면 종료됩니다.",
                style = bodyTextLgRegular,
                color = AppTheme.palette.gray.getColor(7)
            )
        }

        if (showStopDialog) {
            Dialog(
                title = "호흡 가이드를 중단하시겠어요?",
                content = "남은 시간은 저장되지 않습니다.",
                btnCancel = "중단하기",
                btnContinue = "계속하기",
                onDismissRequest = onStopClick,
                onContinue = onContinueClick
            )
        }
    }
}

@Composable
private fun PauzeVisualBreathStepText(
    label: String,
    seconds: Int,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    ) {
        Text(
            text = label,
            style = bodyTextXlBold,
            color = AppTheme.palette.gray.getColor(if (isActive) 2 else 6)
        )
        Text(
            text = "${seconds}초",
            style = bodyTextMdMedium,
            color = AppTheme.palette.gray.getColor(if (isActive) 4 else 7)
        )
    }
}

private data class BreathAnimationState(
    val phase: BreathPhase,
    val progress: Float,
    val remainingPhaseSeconds: Int
) {
    val guideText: String
        get() = when (phase) {
            BreathPhase.READY -> "준비하세요"
            BreathPhase.INHALE -> "들이쉬세요"
            BreathPhase.HOLD -> "참으세요"
            BreathPhase.EXHALE -> "내쉬세요"
        }
}

private fun calculateBreathAnimationState(elapsedSeconds: Int): BreathAnimationState {
    val cycleSecond = elapsedSeconds.mod(BREATH_CYCLE_SECONDS)

    return when {
        cycleSecond < INHALE_SECONDS -> {
            val phaseSecond = cycleSecond + 1
            BreathAnimationState(
                phase = BreathPhase.INHALE,
                progress = phaseSecond.toFloat() / INHALE_SECONDS,
                remainingPhaseSeconds = INHALE_SECONDS - cycleSecond
            )
        }

        cycleSecond < INHALE_SECONDS + HOLD_SECONDS -> {
            BreathAnimationState(
                phase = BreathPhase.HOLD,
                progress = 1f,
                remainingPhaseSeconds = INHALE_SECONDS + HOLD_SECONDS - cycleSecond
            )
        }

        else -> {
            val phaseSecond = cycleSecond - INHALE_SECONDS - HOLD_SECONDS + 1
            BreathAnimationState(
                phase = BreathPhase.EXHALE,
                progress = 1f - phaseSecond.toFloat() / EXHALE_SECONDS,
                remainingPhaseSeconds = BREATH_CYCLE_SECONDS - cycleSecond
            )
        }
    }
}

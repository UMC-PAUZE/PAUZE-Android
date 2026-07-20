package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingLgBold
import kotlinx.coroutines.delay

@Composable
fun PauzeVisualRunningScreen(
    totalSeconds: Int,
    onStopClick: () -> Unit,
    onFinish: () -> Unit,
) {
    var remainingSeconds by remember(totalSeconds) {
        mutableStateOf(totalSeconds)
    }
    var showStopDialog by remember {
        mutableStateOf(false)
    }

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

    val minute = remainingSeconds / 60
    val second = remainingSeconds % 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(enabled = !showStopDialog) {
                showStopDialog = true
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
            PauzeVisualStopDialog(
                onStopClick = onStopClick,
                onContinueClick = { showStopDialog = false }
            )
        }
    }
}

@Composable
private fun PauzeVisualStopDialog(
    onStopClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(292.dp)
                .background(
                    color = AppTheme.palette.gray.getColor(8),
                    shape = RoundedCornerShape(20.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "명상을 중단하시겠어요?",
                style = bodyTextXlBold,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "남은 시간은 저장되지 않습니다.",
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppTheme.palette.gray.getColor(7))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable(onClick = onStopClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "중단하기",
                        style = bodyTextLgBold,
                        color = AppTheme.palette.gray.getColor(2)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxSize()
                        .background(AppTheme.palette.gray.getColor(7))
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable(onClick = onContinueClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "계속하기",
                        style = bodyTextLgBold,
                        color = AppTheme.palette.gray.getColor(2)
                    )
                }
            }
        }
    }
}

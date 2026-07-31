package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.headingLgBold
import kotlinx.coroutines.delay

@Composable
fun PauzeVisualMeditationRunningScreen(
    totalSeconds: Int,
    showStopDialog: Boolean,
    onShowStopDialog: () -> Unit,
    onStopClick: () -> Unit,
    onContinueClick: () -> Unit,
    onFinish: () -> Unit,
) {
    var remainingSeconds by remember(totalSeconds) {
        mutableStateOf(totalSeconds)
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

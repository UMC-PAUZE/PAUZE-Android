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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.headingLgBold
import kotlinx.coroutines.delay

@Composable
fun PauzeVisualCountdownScreen(
    countdownNumber: Int,
    onCountdownChange: (Int) -> Unit,
    onCountdownFinish: () -> Unit
) {
    LaunchedEffect(Unit) {
        for (number in 3 downTo 1) {
            onCountdownChange(number)
            delay(1000L)
        }

        onCountdownFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = countdownNumber.toString(),
                style = headingLgBold.copy(
                    fontSize = 64.sp,
                    lineHeight = 64.sp
                ),
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "잠시 후 시작됩니다\n편안한 자세를 잡아주세요",
                style = bodyTextLgRegular,
                color = AppTheme.palette.gray.getColor(4),
                textAlign = TextAlign.Center
            )
        }
    }
}

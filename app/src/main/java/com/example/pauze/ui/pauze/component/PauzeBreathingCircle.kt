package com.example.pauze.ui.pauze.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.fontFamily

@Composable
fun PauzeBreathingCircle(
    progress: Float,
    secondsText: String,
    showCircle: Boolean,
    modifier: Modifier = Modifier
){
    val outerSize by animateDpAsState(
        targetValue = lerp(220.dp, 312.dp, progress),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )
    val middleSize by animateDpAsState(
        targetValue = lerp(180.dp, 218.dp, progress),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(312.dp),
        contentAlignment = Alignment.Center
    ){
        if(showCircle) {
            Box(
                modifier = Modifier
                    .size(outerSize)
                    .background(
                        color = AppTheme.palette.gray.getColor(8),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(middleSize)
                    .background(
                        color = AppTheme.palette.gray.getColor(7),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(6),
                        shape = CircleShape
                    )
            )
        }
        Text(
            text = secondsText,
            color = AppTheme.palette.gray.getColor(2),
            fontSize = 64.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily
        )
    }
}

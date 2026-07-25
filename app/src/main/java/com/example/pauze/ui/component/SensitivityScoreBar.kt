package com.example.pauze.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme

@Composable
fun SensitivityScoreBar(score: Int) {
    val trackColor = AppTheme.palette.base.getColor(0)
    val gradientColors = arrayOf(
        0f to AppTheme.palette.primary.getColor(3),
        0.5f to AppTheme.palette.tertiary.getColor(3),
        1f to AppTheme.palette.secondary.getColor(4)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        val cornerRadius = CornerRadius(1000f, 1000f)
        val fillWidth = size.width * (score / 100f)

        drawRoundRect(
            color = trackColor,
            cornerRadius = cornerRadius
        )
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colorStops = gradientColors,
                endX = size.width
            ),
            size = Size(fillWidth, size.height),
            cornerRadius = cornerRadius
        )
    }
}


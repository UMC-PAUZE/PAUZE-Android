package com.example.pauze.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme

@Composable
fun ConditionBar(score: Int, containerWidth: Dp){    // containerWidth는 패딩 제외한 값
    Box(
        modifier = Modifier
            .width(containerWidth)
            .height(29.dp)
            // todo: 색상 그라데이션 나중에 수정
            .background(
                color = AppTheme.palette.gray.getColor(9),
                shape = RoundedCornerShape(1000.dp)
            )
    ){
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf<Color>(
                            Color(0xFFC3D7B6),
                            AppTheme.palette.tertiary.getColor(3),
                            AppTheme.palette.secondary.getColor(4)
                        ),
                    ),
                    shape = RoundedCornerShape(1000.dp)
                )
                .size(containerWidth * score / 100)
        )
    }
}


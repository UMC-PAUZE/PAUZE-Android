package com.example.pauze.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextMdMedium

/** 선택 여부에 따라 스타일이 바뀌는 단일 칩 버튼입니다. */
@Composable
fun Chips(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) AppTheme.palette.primary.getColor(3)
                else Color.Transparent
            )
            .border(
                border = if (isSelected) {
                    BorderStroke(0.dp, Color.Transparent)
                } else {
                    BorderStroke(1.dp, AppTheme.palette.gray.getColor(7))
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = bodyTextMdMedium,
            color = if (isSelected) {
                AppTheme.palette.base.getColor(0)
            } else {
                AppTheme.palette.gray.getColor(4)
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ChipsPreview() {
    var isSelected by remember { mutableStateOf(true) }

    MainPaletteTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.palette.base.getColor(0))
                .padding(24.dp)
        ) {
            Chips(
                text = "전체",
                isSelected = isSelected,
                onClick = { isSelected = !isSelected },
                modifier = Modifier.size(width = 50.dp, height = 34.dp)
            )
        }
    }
}

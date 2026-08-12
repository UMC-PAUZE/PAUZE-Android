package com.example.pauze.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextMdMedium

@Composable
fun Chips(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    contentColor: Color? = null,
) {
    val isDisplayStyle = contentColor != null
    val resolvedColor = contentColor
        ?: if (isSelected) AppTheme.palette.base.getColor(0) else AppTheme.palette.gray.getColor(4)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                when {
                    isDisplayStyle -> AppTheme.palette.gray.getColor(8)
                    isSelected -> AppTheme.palette.primary.getColor(3)
                    else -> Color.Transparent
                }
            )
            .border(
                border = when {
                    isDisplayStyle -> BorderStroke(1.5.dp, AppTheme.palette.gray.getColor(7))
                    isSelected -> BorderStroke(0.dp, Color.Transparent)
                    else -> BorderStroke(1.dp, AppTheme.palette.gray.getColor(7))
                },
                shape = RoundedCornerShape(20.dp)
            )
            .let { if (isDisplayStyle) it else it.clickable(onClick = onClick) }
            .padding(
                horizontal = if (isDisplayStyle) 12.dp else 8.dp,
                vertical = if (isDisplayStyle) 8.dp else 0.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                Icon(
                    painter = it,
                    contentDescription = text,
                    tint = resolvedColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = text, style = bodyTextMdMedium, color = resolvedColor)
        }
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

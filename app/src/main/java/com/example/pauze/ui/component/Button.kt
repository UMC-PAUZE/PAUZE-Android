package com.example.pauze.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextXlBold

enum class ButtonSize { Lg, Md, Sm }
enum class ButtonVariant { Solid, Stroke }

@Composable
fun Button(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Lg,
    variant: ButtonVariant = ButtonVariant.Solid,
    color: Color = AppTheme.palette.gray.getColor(2),
    pressedColor: Color = AppTheme.palette.gray.getColor(4),
    contentColor: Color = AppTheme.palette.gray.getColor(9),
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (horizontalPadding, verticalPadding, textStyle) = when (size) {
        ButtonSize.Lg -> Triple(28.dp, 18.dp, bodyTextXlBold)
        ButtonSize.Md -> Triple(24.dp, 16.dp, bodyTextLgBold)
        ButtonSize.Sm -> Triple(20.dp, 12.dp, bodyTextMdBold)
    }

    val shape = RoundedCornerShape(percent = 50)
    val currentColor = if (isPressed) pressedColor else color

    Box(
        modifier = modifier
            .let {
                when (variant) {
                    ButtonVariant.Solid -> it.background(
                        color = if (!enabled) AppTheme.palette.gray.getColor(8) else currentColor,
                        shape = shape
                    )
                    ButtonVariant.Stroke -> it.border(
                        width = 2.dp,
                        color = if (!enabled) AppTheme.palette.gray.getColor(7) else currentColor,
                        shape = shape
                    )
                }
            }
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = textStyle,
            color = when {
                !enabled && variant == ButtonVariant.Solid -> AppTheme.palette.gray.getColor(9)
                !enabled && variant == ButtonVariant.Stroke -> AppTheme.palette.gray.getColor(7)
                variant == ButtonVariant.Stroke -> currentColor
                else -> contentColor
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 사이즈별
            Button(label = "Lg", size = ButtonSize.Lg, modifier = Modifier.fillMaxWidth())
            Button(label = "Md", size = ButtonSize.Md, modifier = Modifier.width(152.dp))
            Button(label = "Sm", size = ButtonSize.Sm)

            // 비활성
            Button(label = "비활성", enabled = false)

            // Stroke
            Button(
                label = "Stroke",
                variant = ButtonVariant.Stroke,
                color = AppTheme.palette.gray.getColor(2)
            )

            // 커스텀
            Button(
                label = "커스텀",
                color = AppTheme.palette.secondary.getColor(4),
                pressedColor = AppTheme.palette.secondary.getColor(5),
                contentColor = AppTheme.palette.gray.getColor(0)
            )
        }
    }
}

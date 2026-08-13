package com.example.pauze.ui.pauze.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingMdBold
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun PauzeVisualStepLayout(
    title: String,
    description: String,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(
            title = "시각 안정",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(5)
            )

            Spacer(modifier = Modifier.height(72.dp))

            content()

            Spacer(modifier = Modifier.weight(1f))

            PauzeVisualBottomButton(
                text = buttonText,
                enabled = buttonEnabled,
                onClick = onButtonClick
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun PauzeVisualBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = if (enabled) {
                    AppTheme.palette.gray.getColor(2)
                } else {
                    AppTheme.palette.gray.getColor(7)
                },
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            style = headingSmBold,
            color = if (enabled) {
                AppTheme.palette.gray.getColor(9)
            } else {
                AppTheme.palette.gray.getColor(4)
            },
            textAlign = TextAlign.Center
        )
    }
}

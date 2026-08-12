package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.pauze.component.PauzeVisualStepLayout
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold

@Composable
fun PauzeVisualMethodSelectScreen(
    selectedMethod: PauzeVisualMethod?,
    onMethodSelect: (PauzeVisualMethod) -> Unit,
    isLoading: Boolean,
    hasError: Boolean,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    PauzeVisualStepLayout(
        title = "안정하고 싶은 방식을\n선택해주세요",
        description = "시각적 자극이 심할 때는 눈을 쉬게 해주세요.\n화면이 꺼지고, 소리와 호흡만으로 집중할 수 있는 환경이\n만들어집니다.",
        buttonText = "다음",
        buttonEnabled = selectedMethod != null && !isLoading,
        onButtonClick = onNextClick,
        onBackClick = onBackClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PauzeVisualMethodCard(
                title = "호흡 가이드",
                description = "4-7-8 호흡법으로\n신경계를 안정시켜요",
                selected = selectedMethod == PauzeVisualMethod.BreathingGuide,
                enabled = !isLoading,
                onClick = { onMethodSelect(PauzeVisualMethod.BreathingGuide) },
                modifier = Modifier.weight(1f)
            )

            PauzeVisualMethodCard(
                title = "명상",
                description = "아무 생각 없이\n소리에만 집중해요",
                selected = selectedMethod == PauzeVisualMethod.Meditation,
                enabled = !isLoading,
                onClick = { onMethodSelect(PauzeVisualMethod.Meditation) },
                modifier = Modifier.weight(1f)
            )
        }

        when {
            isLoading -> {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.palette.primary.getColor(3),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            hasError -> {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "호흡 가이드를 불러오지 못했어요.\n잠시 후 다시 시도해주세요.",
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(4),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "다시 시도",
                        style = bodyTextXlBold,
                        color = AppTheme.palette.primary.getColor(3),
                        modifier = Modifier.clickable(onClick = onRetryClick)
                    )
                }
            }
        }
    }
}

@Composable
private fun PauzeVisualMethodCard(
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(176.dp)
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) AppTheme.palette.gray.getColor(0) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        if (title == "호흡 가이드") {
            Icon(
                painter = painterResource(id = R.drawable.ic_meditation_visual),
                contentDescription = null,
                tint = AppTheme.palette.primary.getColor(4),
                modifier = Modifier.size(48.dp)
            )
        }
        else {
            Icon(
                painter = painterResource(id = R.drawable.ic_calm_visual),
                contentDescription = null,
                tint = AppTheme.palette.tertiary.getColor(3),
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = bodyTextXlBold,
            color = AppTheme.palette.gray.getColor(0)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
            textAlign = TextAlign.Center
        )
    }
}

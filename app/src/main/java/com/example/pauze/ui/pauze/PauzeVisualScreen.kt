package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingMdBold
import com.example.pauze.ui.theme.headingSmBold
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.example.pauze.R
import androidx.compose.foundation.layout.size
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.ui.tooling.preview.Preview
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.MainPaletteTheme

// 시각 진정 화면 상태
enum class PauzeVisualStep {
    SelectMethod, // 호흡법, 명상 선택 화면
    SelectTime, // 시간 선택
    Countdown, // 시작 카운트다운
    Running // 호흡법 및 명상
}

// 시각 진정 상태
enum class PauzeVisualMethod {
    BreathingGuide, // 호흡법
    Meditation, // 명상
}

// 화면 상태에 따른 분기
@Composable
fun PauzeVisualScreen() {
    var step by remember { mutableStateOf(PauzeVisualStep.SelectMethod) }
    var selectedMethod by remember { mutableStateOf<PauzeVisualMethod?>(null) }

    when (step) {
        PauzeVisualStep.SelectMethod -> {
            PauzeVisualMethodSelectContent(
                selectedMethod = selectedMethod,
                onMethodSelect = { selectedMethod = it },
                onNextClick = { step = PauzeVisualStep.SelectTime }
            )
        }

        PauzeVisualStep.SelectTime -> {
            Text(text = "시간 선택 화면")
        }

        PauzeVisualStep.Countdown -> {
            Text(text = "카운트다운 화면")
        }

        PauzeVisualStep.Running -> {
            Text(text = "진행 중 화면")
        }
    }
}

@Composable
fun PauzeVisualMethodSelectContent(
    selectedMethod: PauzeVisualMethod?,
    onMethodSelect: (PauzeVisualMethod) -> Unit,
    onNextClick: () -> Unit
) {
    val isNextEnabled = selectedMethod != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        PauzeVisualTopBar()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "안정하고 싶은 방식을\n선택해주세요",
            style = headingMdBold,
            color = AppTheme.palette.gray.getColor(2)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "시각적 자극이 심할 때는 눈을 쉬게 해주세요.\n화면이 꺼지고, 소리와 호흡만으로 집중할 수 있는 환경이\n만들어집니다.",
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(5)
        )

        Spacer(modifier = Modifier.height(72.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PauzeVisualMethodCard(
                title = "호흡 가이드",
                description = "4-7-8 호흡법으로\n신경계를 안정시켜요",
                selected = selectedMethod == PauzeVisualMethod.BreathingGuide,
                onClick = { onMethodSelect(PauzeVisualMethod.BreathingGuide) },
                modifier = Modifier.weight(1f)
            )

            PauzeVisualMethodCard(
                title = "명상",
                description = "아무 생각 없이\n소리에만 집중해요",
                selected = selectedMethod == PauzeVisualMethod.Meditation,
                onClick = { onMethodSelect(PauzeVisualMethod.Meditation) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = if (isNextEnabled) {
                        AppTheme.palette.gray.getColor(2)
                    } else {
                        AppTheme.palette.gray.getColor(7)
                    },
                    shape = RoundedCornerShape(28.dp)
                )
                .clickable(
                    enabled = isNextEnabled,
                    onClick = onNextClick
                )
        ) {
            Text(
                text = "다음",
                style = headingSmBold,
                color = if (isNextEnabled) {
                    AppTheme.palette.gray.getColor(9)
                } else {
                    AppTheme.palette.gray.getColor(4)
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PauzeVisualTopBar() {
    Text(
        text = "<    시각 안정",
        color = AppTheme.palette.gray.getColor(0)
    )
}

@Composable
fun PauzeVisualMethodCard(
    title: String,
    description: String,
    selected: Boolean,
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
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_round_headset),
            contentDescription = null,
            tint = AppTheme.palette.primary.getColor(0),
            modifier = Modifier.size(48.dp)
        )

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

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PauzeVisualScreenPreview() {
    PAUZEAndroidTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        MainPaletteTheme {
            PauzeVisualScreen()
        }
    }
}

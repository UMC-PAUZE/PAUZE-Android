package com.example.pauze.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun ReportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(
            title = "리포트",
            showBackButton = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Tab(
                text = "일별",
                selected = false,
                modifier = Modifier.weight(1f)
            )
            Tab(
                text = "주별",
                selected = false,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(83.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_headset),
                contentDescription = "게스트모드 헤드셋",
                modifier = Modifier.size(107.dp),
                tint = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "아직 기록이 없어요",
                    style = headingSmBold,
                    color = AppTheme.palette.gray.getColor(2),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "오늘의 컨디션을 입력하면 예민함 패턴을 분석해드려요",
                    style = bodyTextMdRegular,
                    color = AppTheme.palette.gray.getColor(4),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                label = "로그인하고 시작하기",
                modifier = Modifier.fillMaxWidth()
            )

            // 내비게이션 바
        }
    }
}

// 공용 컴포넌트로 따로 빼기
enum class ButtonSize { Lg, Md, Sm }

@Composable
private fun Button(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Lg,
    backgroundColor: Color = AppTheme.palette.gray.getColor(2),
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

    Box(
        modifier = modifier
            .background(
                color = when {
                    !enabled -> AppTheme.palette.gray.getColor(8)
                    isPressed -> pressedColor
                    else -> backgroundColor
                },
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = textStyle, color = contentColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportScreenPreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
        ){
            ReportScreen()
        }

    }
}

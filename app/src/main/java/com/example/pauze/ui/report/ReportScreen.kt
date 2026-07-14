package com.example.pauze.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ButtonSize
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlMedium
import com.example.pauze.ui.theme.fontFamily
import com.example.pauze.ui.theme.headingMdRegular
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun ReportScreen(isGuest: Boolean = true) {
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
        ) {
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

        if (isGuest) {
            Spacer(modifier = Modifier.height(83.dp))
            GuestContent()
        } else {
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TodayConditionCard()
                AverageScoreCard()
            }
        }
    }
}

@Composable
private fun AverageScoreCard(){
    ReportCard() {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Text(
                text = "이번 달 평균 민감 지수",
                color = AppTheme.palette.gray.getColor(2),
                style = bodyTextLgRegular
            )
            Text(
                text = "56점",
                color = AppTheme.palette.tertiary.getColor(3),
                style = bodyTextLgBold
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
            Column(
                modifier = Modifier.height(165.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("100", "75", "50", "25", "0").forEach { label ->
                    Text(
                        text = label,
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(2)
                    )
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val bars = listOf("1주" to 73, "2주" to 123, "3주" to 153, "4주" to 113, "5주" to 123)
                bars.forEach { (label, barHeight) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(barHeight.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(AppTheme.palette.tertiary.getColor(3), Color(0xFFC3D7B6))
                                    ),
                                    shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50)
                                )
                        )
                        Text(text = label, style = bodyTextSmRegular, color = AppTheme.palette.gray.getColor(2))
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Text(text = "최고 민감 요일", color = AppTheme.palette.gray.getColor(2), style = bodyTextMdRegular)
                Text(text = "금요일", color= AppTheme.palette.secondary.getColor(4), style = bodyTextMdBold)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Text(text = "출석", color = AppTheme.palette.gray.getColor(2), style = bodyTextMdRegular)
                Text(text = "7회", color= AppTheme.palette.primary.getColor(4), style = bodyTextMdBold)
            }
        }
    }
}

@Composable
private fun GuestContent() {
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

@Composable
private fun TodayConditionCard() {
    ReportCard {
        Text(
            text = "오늘의 컨디션은 어떤가요?",
            style = bodyTextXlMedium,
            color = AppTheme.palette.gray.getColor(2)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "?",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                    color = AppTheme.palette.gray.getColor(2)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "/ 100",
                    style = headingMdRegular,
                    color = AppTheme.palette.gray.getColor(4)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(29.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(9),
                        shape = RoundedCornerShape(percent = 50)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(237.dp)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colorStops = arrayOf(
                                    0.14f to Color(0xFFC3D7B6),
                                    0.22f to AppTheme.palette.tertiary.getColor(3),
                                    1f to AppTheme.palette.secondary.getColor(4)
                                )
                            ),
                            shape = RoundedCornerShape(percent = 50)
                        )
                )
            }
        }

        Button(
            label = "오늘의 컨디션 입력하기",
            size = ButtonSize.Md,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun ReportCard(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = AppTheme.palette.gray.getColor(7),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        content = content
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ReportScreenGuestPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
        ) {
            ReportScreen(isGuest = false)
        }
    }
}

package com.example.pauze.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlin.math.asin

@Composable
fun ReportScreen(isGuest: Boolean = true, viewModel: ReportViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(
            title = "리포트",
            showBackButton = false
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Tab(
                    text = "일별",
                    selected = viewModel.selectedPeriod == ReportPeriod.DAILY,
                    onClick = { viewModel.selectPeriod(ReportPeriod.DAILY) },
                    modifier = Modifier.weight(1f)
                )
                Tab(
                    text = "주별",
                    selected = viewModel.selectedPeriod == ReportPeriod.WEEKLY,
                    onClick = { viewModel.selectPeriod(ReportPeriod.WEEKLY) },
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
                    AverageScoreCard(viewModel.averageScore)
                    TriggerCard()
                    InsightCard(viewModel.insightTitle)
                    Spacer(modifier = Modifier.height(136.dp)) // 네비게이션 바 자리
                }
            }
        }
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
private fun AverageScoreCard(state: AverageScoreUiState){
    ReportCard() {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Text(
                text = state.title,
                color = AppTheme.palette.gray.getColor(2),
                style = bodyTextLgRegular
            )
            Text(
                text = state.scoreText,
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
                state.bars.forEach { (label, barHeight) ->
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
                Text(text = state.bestDay, color= AppTheme.palette.secondary.getColor(4), style = bodyTextMdBold)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Text(text = "출석", color = AppTheme.palette.gray.getColor(2), style = bodyTextMdRegular)
                Text(text = state.attendanceCount, color= AppTheme.palette.primary.getColor(4), style = bodyTextMdBold)
            }
        }
    }
}

@Composable
private fun TriggerCard(){
    data class TriggerData(val label: String, val percent: Float, val color: Color)

    val triggers = listOf(
        TriggerData("소음 노출", 0.40f, AppTheme.palette.secondary.getColor(4)),
        TriggerData("수면 부족", 0.20f, AppTheme.palette.tertiary.getColor(4)),
        TriggerData("사회 피로", 0.20f, AppTheme.palette.tertiary.getColor(2)),
        TriggerData("업무 스트레스", 0.15f, AppTheme.palette.primary.getColor(5)),
        TriggerData("혼잡한 공간", 0.05f, AppTheme.palette.gray.getColor(4)),
    )

    ReportCard(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "주요 트리거",
            style = bodyTextXlMedium,
            color = AppTheme.palette.gray.getColor(2)
        )

        Canvas(modifier = Modifier.size(180.dp)) {
            val ringThickness = 36.dp.toPx()
            val cornerRadius = 3.dp.toPx()

            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = cornerRadius * 2
                strokeJoin = Paint.Join.ROUND
            }

            val cx = size.width / 2
            val cy = size.height / 2
            val outerRadius = size.minDimension / 2 - cornerRadius
            val innerRadius = size.minDimension / 2 - ringThickness + cornerRadius
            val outerRect = RectF(cx - outerRadius, cy - outerRadius, cx + outerRadius, cy + outerRadius)
            val innerRect = RectF(cx - innerRadius, cy - innerRadius, cx + innerRadius, cy + innerRadius)

            val halfGap = 0.65.dp.toPx() + cornerRadius
            val outerInset = Math.toDegrees(asin((halfGap / outerRadius).toDouble())).toFloat()
            val innerInset = Math.toDegrees(asin((halfGap / innerRadius).toDouble())).toFloat()

            var startAngle = -90f
            triggers.forEach { trigger ->
                val sweep = trigger.percent * 360f
                val path = Path().apply {
                    arcTo(outerRect, startAngle - outerInset, -(sweep - 2 * outerInset))
                    arcTo(innerRect, startAngle - sweep + innerInset, sweep - 2 * innerInset)
                    close()
                }
                paint.color = trigger.color.toArgb()
                drawIntoCanvas { it.nativeCanvas.drawPath(path, paint) }

                startAngle -= sweep
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                triggers.take(3).forEach { trigger ->
                    LegendItem(trigger.label, trigger.color)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                triggers.drop(3).forEach { trigger ->
                    LegendItem(trigger.label, trigger.color)
                }
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .background(color, CircleShape)
        )
        Text(text = label, style = bodyTextMdRegular, color = AppTheme.palette.gray.getColor(2))
    }
}


@Composable
private fun InsightCard(title: String) {
    ReportCard() {
        Row(){
            Text(
                text = title,
                color= AppTheme.palette.gray.getColor(2),
                style=bodyTextXlMedium
            )
        }

        // ui 확인용
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
            InsightParagraph(
                "이번 달 소음 노출이 " to false,
                "18회" to true,
                "로 가장 잦은 트리거였어요. 이어폰 착용을 습관화해보세요." to false
            )
            InsightParagraph(
                "3주차" to true,
                "에 민감지수가 " to false,
                "월 최고치(71점)" to true,
                "를 기록했어요. " to false,
                "업무 마감" to true,
                "과 겹친 시기예요." to false
            )
            InsightParagraph(
                "수면이 7시간 이상" to true,
                "인 날은 예민함 점수가 평균 " to false,
                "28% 낮게" to true,
                " 나타났어요." to false
            )
            InsightParagraph(
                "4주차에 안정세" to true,
                "를 되찾은 건 PAUZE 사용 횟수가 늘어난 덕분이에요. " to false
            )
        }
    }
}

@Composable
private fun InsightParagraph(vararg segments: Pair<String, Boolean>) {
    Text(
        text = buildAnnotatedString {
            segments.forEach { (text, bold) ->
                if (bold) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(text) }
                } else {
                    append(text)
                }
            }
        },
        color = AppTheme.palette.gray.getColor(2),
        style = bodyTextMdRegular
    )
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

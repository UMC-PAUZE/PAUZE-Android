package com.example.pauze.ui.report.component

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.pauze.data.model.TriggerColorToken
import com.example.pauze.data.model.TriggerUiState
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlMedium
import kotlin.math.asin

// 예민함 트리거 화면
@Composable
fun TriggerCard(triggers: List<TriggerUiState>){

    val coloredTriggers = triggers.map {it to it.colorToken.toColor()}
    val donutSegments = coloredTriggers.filter { (trigger, _) -> trigger.percent > 0f }

    ReportCard {
        Text(
            text = "주요 트리거",
            style = bodyTextXlMedium,
            color = AppTheme.palette.gray.getColor(2)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 도넛차트 그리는 코드
            Canvas(modifier = Modifier.size(180.dp)) {
                val ringThickness = 36.dp.toPx() // 도넛 두께
                val cornerRadius = 3.dp.toPx() // 각 도넛 조각 둥글게 표현

                // 그리기 스타일
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.FILL_AND_STROKE
                    strokeWidth = cornerRadius * 2
                    strokeJoin = Paint.Join.ROUND
                }

                // 캔버스 중심 좌표
                val cx = size.width / 2
                val cy = size.height / 2

                // 도넛 바깥쪽, 안쪽 반지름 계산
                val outerRadius = size.minDimension / 2 - cornerRadius
                val innerRadius = size.minDimension / 2 - ringThickness + cornerRadius

                // 원호를 그리기 위한 원을 감싸는 사각형 영역 정의
                val outerRect = RectF(cx - outerRadius, cy - outerRadius, cx + outerRadius, cy + outerRadius)
                val innerRect = RectF(cx - innerRadius, cy - innerRadius, cx + innerRadius, cy + innerRadius)

                // 조각 사이 간격
                val halfGap = 0.65.dp.toPx() + cornerRadius
                val outerInset = Math.toDegrees(asin((halfGap / outerRadius).toDouble())).toFloat()
                val innerInset = Math.toDegrees(asin((halfGap / innerRadius).toDouble())).toFloat()

                var startAngle = -90f // 시작위치: 12시 방향
                donutSegments.forEach { (trigger, color) ->
                    // 데이터 비율 원의 360도 각도로 변환
                    val sweep = trigger.percent * 360f

                    // 양 끝 간격 (Inset) 제외하고 그릴 각도 계산
                    val outerSweep = (sweep - 2 * outerInset).coerceAtLeast(0f)
                    val innerSweep = (sweep - 2 * innerInset).coerceAtLeast(0f)

                    // 조각 형태로 생성
                    val path = Path().apply {
                        arcTo(outerRect, startAngle - outerInset, -outerSweep)
                        arcTo(innerRect, startAngle - sweep + innerInset, innerSweep)
                        close()
                    }

                    // 색상 적용 & 그리기
                    paint.color = color.toArgb()
                    drawIntoCanvas { it.nativeCanvas.drawPath(path, paint) }

                    // 시계 반대방향 이동
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
                    coloredTriggers.take(3).forEach { (trigger, color) ->
                        LegendItem(trigger.label, color)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    coloredTriggers.drop(3).forEach { (trigger, color) ->
                        LegendItem(trigger.label, color)
                    }
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
private fun TriggerColorToken.toColor(): Color = when (this) {
    TriggerColorToken.NOISE -> AppTheme.palette.tertiary.getColor(3)
    TriggerColorToken.SLEEP -> AppTheme.palette.purple.getColor(2)
    TriggerColorToken.SOCIAL -> AppTheme.palette.primary.getColor(4)
    TriggerColorToken.ENERGY -> AppTheme.palette.secondary.getColor(3)
    TriggerColorToken.VISUAL_OVERLOAD -> AppTheme.palette.blue.getColor(2)
}
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

@Composable
fun TriggerCard(triggers: List<TriggerUiState>){

    val coloredTriggers = triggers.map {it to it.colorToken.toColor()}

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
            coloredTriggers.forEach { (trigger, color) ->
                val sweep = trigger.percent * 360f
                val path = Path().apply {
                    arcTo(outerRect, startAngle - outerInset, -(sweep - 2 * outerInset))
                    arcTo(innerRect, startAngle - sweep + innerInset, sweep - 2 * innerInset)
                    close()
                }
                paint.color = color.toArgb()
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
    TriggerColorToken.WORK -> AppTheme.palette.secondary.getColor(3)
    TriggerColorToken.CROWDED -> AppTheme.palette.blue.getColor(2)
}
package com.example.pauze.ui.report.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pauze.data.model.AverageScoreUiState
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun AverageScoreCard(state: AverageScoreUiState){
    ReportCard() {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Text(
                text = state.title,
                color = AppTheme.palette.gray.getColor(2),
                style = bodyTextLgRegular
            )
            Text(
                text = "${state.score}점",
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

            Column(modifier = Modifier.weight(1f)) {
                val chartHeight = 165.dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    state.bars.forEach { bar ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(chartHeight * (bar.score / 100f))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            AppTheme.palette.tertiary.getColor(3),
                                            Color(0xFFC3D7B6)
                                        )
                                    ),
                                    shape = RoundedCornerShape(
                                        topStartPercent = 50,
                                        topEndPercent = 50
                                    )
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.bars.forEach { bar ->
                        Text(
                            text = bar.label,
                            style = bodyTextSmRegular,
                            color = AppTheme.palette.gray.getColor(2),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
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
                Text(text = "Pauze 실행", color = AppTheme.palette.gray.getColor(2), style = bodyTextMdRegular)
                Text(text = "${state.attendanceCount}회", color= AppTheme.palette.primary.getColor(4), style = bodyTextMdBold)
            }
        }
    }
}
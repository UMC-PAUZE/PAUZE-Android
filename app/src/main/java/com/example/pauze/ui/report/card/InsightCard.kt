package com.example.pauze.ui.report.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.pauze.data.model.InsightSegment
import com.example.pauze.data.model.InsightUiState
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlMedium

@Composable
fun InsightCard(state: InsightUiState) {
    ReportCard() {
        Row(){
            Text(
                text = state.title,
                color= AppTheme.palette.gray.getColor(2),
                style=bodyTextXlMedium
            )
        }

        // ui 확인용
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
            state.paragraphs.forEach { segments ->
                InsightParagraph(segments)
            }
        }
    }
}

@Composable
private fun InsightParagraph(segments: List<InsightSegment>) {
    Text(
        text = buildAnnotatedString {
            segments.forEach { segment ->
                if (segment.bold) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(segment.text) }
                } else {
                    append(segment.text)
                }
            }
        },
        color = AppTheme.palette.gray.getColor(2),
        style = bodyTextMdRegular
    )
}
package com.example.pauze.ui.report.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
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

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
            state.paragraphs.forEach { text ->
                Text(
                    text = text,
                    color = AppTheme.palette.gray.getColor(2),
                    style = bodyTextMdRegular
                )
            }
        }
    }
}
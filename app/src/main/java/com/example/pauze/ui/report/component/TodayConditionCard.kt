package com.example.pauze.ui.report.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.data.model.Condition
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ButtonSize
import com.example.pauze.ui.component.SensitivityScoreBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextXlMedium
import com.example.pauze.ui.theme.fontFamily
import com.example.pauze.ui.theme.headingMdRegular

@Composable
fun TodayConditionCard(
    condition: Condition?,
    onInputClick: () -> Unit
) {
    ReportCard {
        if(condition != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Text(
                    text="오늘의 민감 지수",
                    style= bodyTextLgRegular,
                    color = AppTheme.palette.gray.getColor(2)
                )
                Text(
                    text = condition.index.label,
                    style = bodyTextLgBold,
                    color = AppTheme.palette.tertiary.getColor(3)
                )
            }
        } else {
            Text(
                text = "오늘의 컨디션은 어떤가요?",
                style = bodyTextXlMedium,
                color = AppTheme.palette.gray.getColor(2)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = condition?.score?.toString() ?: "?",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                    color = AppTheme.palette.gray.getColor(2)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "/ 100", style = headingMdRegular, color = AppTheme.palette.gray.getColor(4))
            }

            SensitivityScoreBar(score = condition?.score ?: 0)

        }

        if (condition == null){
            Button(
                label = "오늘의 컨디션 입력하기",
                size = ButtonSize.Md,
                modifier = Modifier.fillMaxWidth(),
                onClick = onInputClick
            )
        }
    }
}
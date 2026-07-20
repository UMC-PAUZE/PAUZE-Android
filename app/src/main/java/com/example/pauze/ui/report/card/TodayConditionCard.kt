package com.example.pauze.ui.report.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ButtonSize
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextXlMedium
import com.example.pauze.ui.theme.fontFamily
import com.example.pauze.ui.theme.headingMdRegular

@Composable
fun TodayConditionCard(onInputClick: () -> Unit) {
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
            modifier = Modifier.fillMaxWidth(),
            onClick = onInputClick
        )
    }
}
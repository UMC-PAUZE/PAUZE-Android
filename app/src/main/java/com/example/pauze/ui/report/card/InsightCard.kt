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
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlMedium

@Composable
fun InsightCard(title: String) {
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
package com.example.pauze.data.dummies

import com.example.pauze.data.model.AverageScoreUiState
import com.example.pauze.data.model.ChartBar
import com.example.pauze.data.model.InsightSegment
import com.example.pauze.data.model.InsightUiState

object ReportDummyData { //ui 확인하기 위한 데이터
    val dailyAverageScore = AverageScoreUiState(
        title = "이번 주 평균 민감 지수",
        score = 55,
        bars = listOf(
            ChartBar("일", 44), ChartBar("월", 75),
            ChartBar("화", 93), ChartBar("수", 68),
            ChartBar("목", 75), ChartBar("금", 51),
            ChartBar("토", 64)
        ),
        bestDay = "금요일",
        attendanceCount = 7
    )

    val weeklyAverageScore = AverageScoreUiState(
        title = "이번 달 평균 민감 지수",
        score = 56,
        bars = listOf(
            ChartBar("1주", 44), ChartBar("2주", 75),
            ChartBar("3주", 93), ChartBar("4주", 68),
            ChartBar("5주", 75)
        ),
        bestDay = "금요일",
        attendanceCount = 7
    )

    val dailyInsight = InsightUiState(
        title = "이번 주 인사이트",
        paragraphs = listOf(
            listOf(
                InsightSegment("금요일 오후", true),
                InsightSegment("에 예민함이 가장 높아요. 오전에 미리 PAUZE를 해보세요.", false)
            ),
            listOf(
                InsightSegment("충분한 수면(7-8시간)을 한 날 예민함이 ", false),
                InsightSegment("평균 23% ", true),
                InsightSegment("낮았어요.", false)
            ),
            listOf(
                InsightSegment("주말 휴식", true),
                InsightSegment("이 잘 이루어지고 있어요! 이 패턴을 유지해보세요. ", false)
            )
        )
    )

    val weeklyInsight = InsightUiState(
        title = "이번 달 인사이트",
        paragraphs = listOf(
            listOf(
                InsightSegment("이번 달 소음 노출이 ", false),
                InsightSegment("18회", true),
                InsightSegment("로 가장 잦은 트리거였어요. 이어폰 착용을 습관화해보세요.", false)
            ),
            listOf(
                InsightSegment("3주차", true),
                InsightSegment("에 민감지수가 ", false),
                InsightSegment("월 최고치(71점)", true),
                InsightSegment("를 기록했어요. ", false),
                InsightSegment("업무 마감", true),
                InsightSegment("과 겹친 시기예요.", false)
            ),
            listOf(
                InsightSegment("수면이 7시간 이상", true),
                InsightSegment("인 날은 예민함 점수가 평균 ", false),
                InsightSegment("28% 낮게", true),
                InsightSegment(" 나타났어요.", false)
            ),
            listOf(
                InsightSegment("4주차에 안정세", true),
                InsightSegment("를 되찾은 건 PAUZE 사용 횟수가 늘어난 덕분이에요. ", false)
            )
        )
    )
}
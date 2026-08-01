package com.example.pauze.data.dummies

import com.example.pauze.data.model.Activity
import com.example.pauze.data.model.AverageScoreUiState
import com.example.pauze.data.model.ChartBar
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.InsightUiState
import com.example.pauze.data.model.Noise
import com.example.pauze.data.model.OverallIndex
import com.example.pauze.data.model.Sleeping
import com.example.pauze.data.model.TriggerColorToken
import com.example.pauze.data.model.TriggerUiState

object ReportDummyData { //ui 확인하기 위한 데이터

    val todayCondition = Condition(
        score = 26,
        index = OverallIndex.Low,
        sleeping = Sleeping.Moderate,
        noise = Noise.Low,
        activity = Activity.Moderate
    )

    val dailyAverageScore = AverageScoreUiState(
        title = "이번 주 평균 민감 지수",
        score = 55,
        bars = listOf(
            ChartBar("월", 44), ChartBar("화", 75),
            ChartBar("수", 93), ChartBar("목", 68),
            ChartBar("금", 75), ChartBar("토", 51),
            ChartBar("일", 64)
        ),
        bestLabel = "최고 민감 요일",
        bestValue = "금요일",
        executionCount = 7
    )

    val weeklyAverageScore = AverageScoreUiState(
        title = "이번 달 평균 민감 지수",
        score = 56,
        bars = listOf(
            ChartBar("1주", 44), ChartBar("2주", 75),
            ChartBar("3주", 93), ChartBar("4주", 68),
            ChartBar("5주", 75)
        ),
        bestLabel = "최고 민감 주차",
        bestValue = "2주",
        executionCount = 7
    )

    val dailyTriggers = listOf(
        TriggerUiState("소음 노출", 0.40f, TriggerColorToken.NOISE),
        TriggerUiState("수면 부족", 0.20f, TriggerColorToken.SLEEP),
        TriggerUiState("사회 피로", 0.20f, TriggerColorToken.SOCIAL),
        TriggerUiState("에너지 소진", 0.15f, TriggerColorToken.ENERGY),
        TriggerUiState("과한 시각 정보", 0.05f, TriggerColorToken.VISUAL_OVERLOAD)
    )

    val weeklyTriggers = listOf(
        TriggerUiState("소음 노출", 0.25f, TriggerColorToken.NOISE),
        TriggerUiState("수면 부족", 0.30f, TriggerColorToken.SLEEP),
        TriggerUiState("사회 피로", 0.05f, TriggerColorToken.SOCIAL),
        TriggerUiState("업무 스트레스", 0.35f, TriggerColorToken.ENERGY),
        TriggerUiState("과한 시각 정보", 0.05f, TriggerColorToken.VISUAL_OVERLOAD)
    )

    val dailyInsight = InsightUiState(
        title = "이번 주 인사이트",
        paragraphs = listOf(
            "금요일 오후에 예민함이 가장 높아요. 오전에 미리 PAUZE를 해보세요.",
            "충분한 수면(7-8시간)을 한 날 예민함이 평균 23% 낮았어요.",
            "주말 휴식이 잘 이루어지고 있어요! 이 패턴을 유지해보세요."
        )
    )

    val weeklyInsight = InsightUiState(
        title = "이번 달 인사이트",
        paragraphs = listOf(
            "이번 달 소음 노출이 18회로 가장 잦은 트리거였어요. 이어폰 착용을 습관화해보세요.",
            "3주차에 민감지수가 월 최고치(71점)를 기록했어요. 업무 마감과 겹친 시기예요.",
            "수면이 7시간 이상인 날은 예민함 점수가 평균 28% 낮게 나타났어요.",
            "4주차에 안정세를 되찾은 건 PAUZE 사용 횟수가 늘어난 덕분이에요."
        )
    )
}
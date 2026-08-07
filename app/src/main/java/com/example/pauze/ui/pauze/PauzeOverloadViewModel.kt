package com.example.pauze.ui.pauze

import com.example.pauze.R
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.InstantAction
import com.example.pauze.data.model.RestGuide
import com.example.pauze.ui.BaseViewModel

sealed interface PauzeOverloadEffect {
    object BackStack: PauzeOverloadEffect
    object NavigateToFind: PauzeOverloadEffect
}

class PauzeOverloadViewModel: BaseViewModel<PauzeOverloadEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    val instantActions = listOf<InstantAction>(
        InstantAction(
            duration = 10,
            title = "눈 감고 10초 호흡",
            image = R.drawable.ic_closed_eyes
        ),
        InstantAction(
            duration = 60,
            title = "어깨 스트레칭",
            image = R.drawable.ic_muscle
        ),
        InstantAction(
            duration = 30,
            title = "손목 털기",
            image = R.drawable.ic_hands
        ),
        InstantAction(
            duration = 0,
            title = "물 한 모금",
            image = R.drawable.ic_water
        ),
    )

    val restGuideList = listOf<RestGuide>(
        RestGuide(
            image = 0,
            duration = 10,
            title = "혼자만의 공간 찾기",
            content = "자극이 차단된 조용한 곳에서 아무것도 하지 않고 머무는 시간. 혼잡한 환경에서 소모된 신경계를 안정시키는 것이 중요해요."
        ),
        RestGuide(
            image = 0,
            duration = 5,
            title = "햇빛과 바랍 쐬기",
            content = "창문을 열거나 야외로 나가 자연 감각을 짧게 접촉합니다. 과도한 인공 자극에서 벗어나 감각을 환기하는 데 도움이 됩니다."
        ),
        RestGuide(
            image = 0,
            duration = 20,
            title = "디지털 디톡스",
            content = "화면과 알림을 모두 끄고 온전히 나에 집중해보세요. 시각·청각 자극이 집중되는 스마트폰 사용은 HSP의 피로를 빠르게 가중시킵니다."
        ),
        RestGuide(
            image = 0,
            duration = 10,
            title = "따뜻한 음료 한 잔",
            content = "온도와 향에 집중하며 천천히 마십니다. 감각에 부드럽게 주의를 기울이는 행위가 과각성 상태를 낮추는 데 효과적입니다."
        ),
        RestGuide(
            image = 0,
            duration = 5,
            title = "감정 일기 3줄 쓰기",
            content = "오늘 가장 강하게 느낀 감정 하나를 3줄 이내로 적습니다. 감정을 언어화하면 처리되지 않은 자극이 쌓이는 것을 방지하는 효과가 있어요."
        ),
    )
    fun backStack(){
        sendEffect(PauzeOverloadEffect.BackStack)
    }
    fun navigateToFind(){
        sendEffect(PauzeOverloadEffect.NavigateToFind)
    }
}
package com.example.pauze.ui.pauze

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.ConditionQuestion
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TodayConditionState(
    val currentQuestionIndex: Int = 0,
    val answers: List<Int?> = List(TODAY_CONDITION_QUESTION_COUNT) { null },
    val sensitivityScore: Int = 53,
    val showResult: Boolean = false
) {
    val isPreviousEnabled: Boolean
        get() = currentQuestionIndex > 0

    val isNextEnabled: Boolean
        get() = answers[currentQuestionIndex] != null
}

sealed interface TodayConditionEffect {
    data object ShowExitDialog : TodayConditionEffect
    data object NavigateBack : TodayConditionEffect
    data object NavigateToMainActivity : TodayConditionEffect
    data object NavigateToPauzeStartActivity : TodayConditionEffect
}

class PauzeTodayConditionViewModel : BaseViewModel<TodayConditionEffect, Unit>(
    uiState = BaseUiState(data = Unit)
) {
    private val _conditionQuestions = MutableStateFlow(
        listOf(
            ConditionQuestion(
                title = "지난 밤, 몇 시간동안\n수면을 취했나요?",
                description = "수면의 양과 질은 예민함에 큰 영향을 줘요.",
                choices = listOf("4시간 미만", "4~6시간", "6~8시간", "8시간 이상")
            ),
            ConditionQuestion(
                title = "오늘 소음 노출은\n어느 정도였나요?",
                description = "대중교통, 사무실 소음 등 모든 소음을 포함해요",
                choices = listOf("조용했어요", "감당 가능한 정도였어요", "불편했어요", "힘들 정도였어요")
            ),
            ConditionQuestion(
                title = "오늘의 시각 정보량은\n어땠나요?",
                description = "화면 시청, 광고, 밝은 조명 등 시각 자극 전체를 포함해요",
                choices = listOf("거의 없음", "약간 있음", "꽤 많았어요", "매우 많았어요")
            ),
            ConditionQuestion(
                title = "오늘은 사회적 활동을\n얼마나 했나요?",
                description = "대화, 회의, 모임 등 타인과의 상호작용 시간을\n알려주세요",
                choices = listOf("혼자였어요", "조금 있었어요", "꽤 있었어요", "많았어요")
            ),
            ConditionQuestion(
                title = "지금 가지고 있는 에너지는\n얼마나 되나요?",
                description = "현재 느끼는 신체적, 정서적 에너지를 알려주세요.",
                choices = listOf("완전 방전", "낮아요", "보통이에요", "충분해요")
            )
        )
    )
    val conditionQuestions = _conditionQuestions.asStateFlow()

    private val _state = MutableStateFlow(TodayConditionState())
    val state = _state.asStateFlow()

    fun selectAnswer(choiceIndex: Int) {
        _state.update { currentState ->
            currentState.copy(
                answers = currentState.answers.mapIndexed { index, answer ->
                    if (index == currentState.currentQuestionIndex) choiceIndex else answer
                }
            )
        }
    }

    fun moveToPreviousQuestion() {
        _state.update { currentState ->
            currentState.copy(
                currentQuestionIndex = (currentState.currentQuestionIndex - 1).coerceAtLeast(0)
            )
        }
    }

    fun moveToNextQuestion() {
        _state.update { currentState ->
            when {
                !currentState.isNextEnabled -> currentState
                currentState.currentQuestionIndex < TODAY_CONDITION_QUESTION_COUNT - 1 -> {
                    currentState.copy(currentQuestionIndex = currentState.currentQuestionIndex + 1)
                }
                else -> currentState.copy(showResult = true)
            }
        }
    }

    fun onBackClick() {
        sendEffect(TodayConditionEffect.ShowExitDialog)
    }

    fun confirmExit() {
        sendEffect(TodayConditionEffect.NavigateBack)
    }

    fun navigateToMainActivity() {
        sendEffect(TodayConditionEffect.NavigateToMainActivity)
    }

    fun navigateToPauzeStartActivity() {
        sendEffect(TodayConditionEffect.NavigateToPauzeStartActivity)
    }
}

private const val TODAY_CONDITION_QUESTION_COUNT = 5

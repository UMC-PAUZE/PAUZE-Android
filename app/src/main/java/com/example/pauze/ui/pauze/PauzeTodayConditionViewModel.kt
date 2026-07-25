package com.example.pauze.ui.pauze

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
}

class PauzeTodayConditionViewModel : BaseViewModel<TodayConditionEffect>() {
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
}

private const val TODAY_CONDITION_QUESTION_COUNT = 5

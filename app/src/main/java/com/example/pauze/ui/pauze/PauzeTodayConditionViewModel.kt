package com.example.pauze.ui.pauze

import androidx.lifecycle.viewModelScope
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.ConditionQuestion
import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.EnergyLevel
import com.example.pauze.data.model.NoiseLevel
import com.example.pauze.data.model.SleepLevel
import com.example.pauze.data.model.SocialLevel
import com.example.pauze.data.model.TODAY_CONDITION_QUESTION_COUNT
import com.example.pauze.data.model.TodayConditionState
import com.example.pauze.data.model.TriggerCode
import com.example.pauze.data.model.VisualLevel
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface TodayConditionEffect {
    data object ShowExitDialog : TodayConditionEffect
    data object NavigateBack : TodayConditionEffect
    data object NavigateToMainActivity : TodayConditionEffect
    data object NavigateToPauzeStartActivity : TodayConditionEffect
}

@HiltViewModel
class PauzeTodayConditionViewModel @Inject constructor(
    private val repository: TodayConditionRepository
) : BaseViewModel<TodayConditionEffect, Unit>(
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
        if (_state.value.isSubmitting) return

        _state.update { currentState ->
            currentState.copy(
                answers = currentState.answers.mapIndexed { index, answer ->
                    if (index == currentState.currentQuestionIndex) choiceIndex else answer
                },
                submissionError = null
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
        val currentState = _state.value
        if (!currentState.isNextEnabled) return

        if (currentState.currentQuestionIndex < TODAY_CONDITION_QUESTION_COUNT - 1) {
            _state.update {
                it.copy(currentQuestionIndex = it.currentQuestionIndex + 1)
            }
        } else {
            submitTodayCondition()
        }
    }

    private fun submitTodayCondition() {
        val request = try {
            _state.value.answers.toTodayConditionRequest()
        } catch (error: IllegalArgumentException) {
            _state.update { it.copy(submissionError = error.message) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, submissionError = null) }

            try {
                val result = repository.createTodayCondition(request)
                _state.update {
                    it.copy(
                        conditionId = result.conditionId,
                        sensitivityScore = result.sensitivityScore,
                        sensitivityLevel = result.sensitivityLevel,
                        triggerCodes = result.triggerCodes,
                        showResult = true,
                        isSubmitting = false
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        submissionError = error.toTodayConditionMessage()
                    )
                }
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

private val sleepLevelsByChoice = listOf(
    SleepLevel.LESS_4,
    SleepLevel.FOUR_TO_SIX,
    SleepLevel.SIX_TO_EIGHT,
    SleepLevel.OVER_8
)

private val noiseLevelsByChoice = listOf(
    NoiseLevel.QUIET,
    NoiseLevel.NORMAL,
    NoiseLevel.UNCOMFORTABLE,
    NoiseLevel.HARD
)

private val visualLevelsByChoice = listOf(
    VisualLevel.LOW,
    VisualLevel.NORMAL,
    VisualLevel.HIGH,
    VisualLevel.VERY_HIGH
)

private val socialLevelsByChoice = listOf(
    SocialLevel.ALONE,
    SocialLevel.LITTLE,
    SocialLevel.SOME,
    SocialLevel.MANY
)

private val energyLevelsByChoice = listOf(
    EnergyLevel.NONE,
    EnergyLevel.LOW,
    EnergyLevel.NORMAL,
    EnergyLevel.ENOUGH
)

internal fun List<Int?>.toTodayConditionRequest(): CreateTodayConditionRequest {
    require(size == TODAY_CONDITION_QUESTION_COUNT && all { it != null }) {
        "모든 질문에 답변해주세요."
    }

    return CreateTodayConditionRequest(
        sleepLevel = sleepLevelsByChoice.getValue(0, this),
        noiseLevel = noiseLevelsByChoice.getValue(1, this),
        visualLevel = visualLevelsByChoice.getValue(2, this),
        socialLevel = socialLevelsByChoice.getValue(3, this),
        energyLevel = energyLevelsByChoice.getValue(4, this)
    )
}

private fun <T> List<T>.getValue(questionIndex: Int, answers: List<Int?>): T {
    val choiceIndex = requireNotNull(answers[questionIndex])
    return getOrNull(choiceIndex)
        ?: throw IllegalArgumentException("올바르지 않은 답변입니다.")
}

private fun Throwable.toTodayConditionMessage(): String = when (this) {
    is HttpException -> when (code()) {
        400 -> "입력값을 확인해주세요."
        401 -> "로그인 후 오늘의 컨디션을 저장할 수 있어요."
        409 -> "오늘의 컨디션을 이미 입력했어요."
        503 -> "서버가 잠시 응답하지 않아요. 잠시 후 다시 시도해주세요."
        504 -> "요청 시간이 초과됐어요. 다시 시도해주세요."
        else -> "오늘의 컨디션 저장에 실패했어요. (${code()})"
    }
    is IOException -> "네트워크 연결을 확인해주세요."
    else -> message ?: "오늘의 컨디션 저장에 실패했어요."
}

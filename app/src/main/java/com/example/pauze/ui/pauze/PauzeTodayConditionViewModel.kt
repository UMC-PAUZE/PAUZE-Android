package com.example.pauze.ui.pauze

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.ConditionQuestion
import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult
import com.example.pauze.data.model.EnergyLevel
import com.example.pauze.data.model.NoiseLevel
import com.example.pauze.data.model.SensitivityLevel
import com.example.pauze.data.model.SleepLevel
import com.example.pauze.data.model.SocialLevel
import com.example.pauze.data.model.VisualLevel
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

data class TodayConditionState(
    val conditionQuestions: List<ConditionQuestion> = todayConditionQuestions,
    val currentQuestionIndex: Int = 0,
    val answers: List<Int?> = List(conditionQuestions.size) { null },
    val conditionId: Long? = null,
    val sensitivityScore: Int = 0,
    val sensitivityLevel: SensitivityLevel? = null,
    val triggerCodes: List<String> = emptyList(),
    val showResult: Boolean = false,
    val submissionError: String? = null
) {
    val isPreviousEnabled: Boolean
        get() = currentQuestionIndex > 0

    val isNextEnabled: Boolean
        get() = answers[currentQuestionIndex] != null
}

private val todayConditionQuestions = listOf(
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

sealed interface TodayConditionEffect {
    data object ShowExitDialog : TodayConditionEffect
    data object NavigateBack : TodayConditionEffect
    data object NavigateToMainActivity : TodayConditionEffect
    data object NavigateToPauzeStartActivity : TodayConditionEffect
}

@HiltViewModel
class PauzeTodayConditionViewModel @Inject constructor(
    private val repository: TodayConditionRepository
) : BaseViewModel<TodayConditionEffect, TodayConditionState>(
    uiState = BaseUiState(data = TodayConditionState())
) {
    fun selectAnswer(choiceIndex: Int) {
        if (uiState.value.isLoading) return

        clearError()
        updateData { currentState ->
            currentState.copy(
                answers = currentState.answers.mapIndexed { index, answer ->
                    if (index == currentState.currentQuestionIndex) choiceIndex else answer
                },
                submissionError = null
            )
        }
    }

    fun moveToPreviousQuestion() {
        if (uiState.value.isLoading) return

        updateData { currentState ->
            currentState.copy(
                currentQuestionIndex = (currentState.currentQuestionIndex - 1).coerceAtLeast(0)
            )
        }
    }

    fun moveToNextQuestion() {
        val currentState = uiState.value.data
        if (uiState.value.isLoading || !currentState.isNextEnabled) return

        if (currentState.currentQuestionIndex < currentState.conditionQuestions.lastIndex) {
            updateData {
                it.copy(currentQuestionIndex = it.currentQuestionIndex + 1)
            }
        } else {
            submitTodayCondition()
        }
    }

    private fun submitTodayCondition() {
        val request = try {
            uiState.value.data.answers.toTodayConditionRequest()
        } catch (error: IllegalArgumentException) {
            updateData { it.copy(submissionError = error.message) }
            return
        }

        clearError()
        updateData { it.copy(submissionError = null) }
        launch<CreateTodayConditionResult>(
            onSuccess = { result ->
                updateData {
                    it.copy(
                        conditionId = result.conditionId,
                        sensitivityScore = result.sensitivityScore,
                        sensitivityLevel = result.sensitivityLevel,
                        triggerCodes = result.triggerCodes,
                        showResult = true
                    )
                }
            },
            block = {
                repository.createTodayCondition(request)
            }
        )
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

    private fun clearError() {
        if (uiState.value.error != null) {
            updateState { currentState ->
                currentState.copy(error = null)
            }
        }
    }
}

private const val TODAY_CONDITION_QUESTION_COUNT = 5

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

internal fun Throwable.toTodayConditionMessage(): String = when (this) {
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

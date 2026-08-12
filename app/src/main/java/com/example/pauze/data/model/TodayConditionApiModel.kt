package com.example.pauze.data.model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class CreateTodayConditionRequest(
    val sleepLevel: SleepLevel,
    val noiseLevel: NoiseLevel,
    val visualLevel: VisualLevel,
    val socialLevel: SocialLevel,
    val energyLevel: EnergyLevel
)

data class CreateTodayConditionResult(
    val conditionId: Long,
    val sensitivityScore: Int,
    val sensitivityLevel: SensitivityLevel
)

data class GetTodayConditionResponseDto(
    val conditionId: Long,
    val conditionDate: String,
    val sleepLevel: SleepLevel,
    val noiseLevel: NoiseLevel,
    val visualLevel: VisualLevel,
    val socialLevel: SocialLevel,
    val energyLevel: EnergyLevel,
    val sensitivityScore: Int,
    val sensitivityLevel: SensitivityLevel,
)

fun GetTodayConditionResponseDto.toCondition() = Condition(
    score = sensitivityScore,
    sleep = sleepLevel,
    noise = noiseLevel,
    visual = visualLevel,
    social = socialLevel,
    energy = energyLevel,
    sensitivity = sensitivityLevel
)

fun GetTodayConditionResponseDto.isToday(): Boolean =
    conditionDate == Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

const val TODAY_CONDITION_QUESTION_COUNT = 5

data class TodayConditionState(
    val currentQuestionIndex: Int = 0,
    val answers: List<Int?> = List(TODAY_CONDITION_QUESTION_COUNT) { null },
    val conditionId: Long? = null,
    val sensitivityScore: Int = 0,
    val sensitivityLevel: SensitivityLevel? = null,
    val showResult: Boolean = false,
    val isSubmitting: Boolean = false,
    val submissionError: String? = null
) {
    val isPreviousEnabled: Boolean
        get() = currentQuestionIndex > 0 && !isSubmitting

    val isNextEnabled: Boolean
        get() = answers[currentQuestionIndex] != null && !isSubmitting
}
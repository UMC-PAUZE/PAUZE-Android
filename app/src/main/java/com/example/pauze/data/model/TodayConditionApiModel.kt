package com.example.pauze.data.model

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
    val sensitivityLevel: SensitivityLevel,
    val triggerCodes: List<TriggerCode> = emptyList()
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
    val triggerCodes: List<TriggerCode> = emptyList(),
)

const val TODAY_CONDITION_QUESTION_COUNT = 5

data class TodayConditionState(
    val currentQuestionIndex: Int = 0,
    val answers: List<Int?> = List(TODAY_CONDITION_QUESTION_COUNT) { null },
    val conditionId: Long? = null,
    val sensitivityScore: Int = 0,
    val sensitivityLevel: SensitivityLevel? = null,
    val triggerCodes: List<TriggerCode> = emptyList(),
    val showResult: Boolean = false,
    val isSubmitting: Boolean = false,
    val submissionError: String? = null
) {
    val isPreviousEnabled: Boolean
        get() = currentQuestionIndex > 0 && !isSubmitting

    val isNextEnabled: Boolean
        get() = answers[currentQuestionIndex] != null && !isSubmitting
}
package com.example.pauze.data.model

enum class SleepLevel {
    OVER_8,
    SIX_TO_EIGHT,
    FOUR_TO_SIX,
    LESS_4
}

enum class NoiseLevel {
    QUIET,
    NORMAL,
    UNCOMFORTABLE,
    HARD
}

enum class VisualLevel {
    LOW,
    NORMAL,
    HIGH,
    VERY_HIGH
}

enum class SocialLevel {
    MANY,
    SOME,
    LITTLE,
    ALONE
}

enum class EnergyLevel {
    ENOUGH,
    NORMAL,
    LOW,
    NONE
}

enum class SensitivityLevel {
    LOW,
    NORMAL,
    HIGH
}

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
    val triggerCodes: List<String> = emptyList()
)

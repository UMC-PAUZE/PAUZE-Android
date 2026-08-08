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
    val triggerCodes: List<String> = emptyList()
)

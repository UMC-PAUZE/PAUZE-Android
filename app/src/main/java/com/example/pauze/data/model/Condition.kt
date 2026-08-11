package com.example.pauze.data.model

enum class SleepLevel(val label:String) {
    OVER_8("8시간 이상"),
    SIX_TO_EIGHT("6~8시간"),
    FOUR_TO_SIX("4~6시간"),
    LESS_4("4시간 미만")
}
enum class NoiseLevel(val label: String) {
    QUIET("적음"),
    NORMAL("보통"),
    UNCOMFORTABLE("불편"),
    HARD("과다")
}

enum class VisualLevel(val label: String){
    LOW("적음"),
    NORMAL("보통"),
    HIGH("불편"),
    VERY_HIGH("과다"),
}

enum class SocialLevel(val label: String){
    MANY("피곤"),
    SOME("보통"),
    LITTLE("적음"),
    ALONE("없음"),
}
enum class EnergyLevel(val label: String){
    ENOUGH("편안"),
    NORMAL("보통"),
    LOW("부족"),
    NONE("소진"),
}

enum class SensitivityLevel(val label: String){
    LOW("낮음"),
    NORMAL("보통"),
    HIGH("높음")
}

enum class TriggerCode {
    SLEEP_DEPRIVATION,
    NOISE_EXPOSURE,
    VISUAL_OVERLOAD,
    SOCIAL_FATIGUE,
    ENERGY_DEPLETION
}
data class Condition(
    val score: Int,
    val sleep: SleepLevel,
    val noise: NoiseLevel,
    val visual: VisualLevel,
    val social: SocialLevel,
    val energy: EnergyLevel,
    val sensitivity: SensitivityLevel
)

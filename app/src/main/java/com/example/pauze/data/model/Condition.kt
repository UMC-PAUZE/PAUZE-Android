package com.example.pauze.data.model

enum class SleepLevel(val label:String) {
    OVER_8("8시간 이상"),
    SIX_TO_EIGHT("6~8시간"),
    FOUR_TO_SIX("4~6시간"),
    LESS_4("4시간 미만")
}
enum class NoiseLevel(val label: String) {
    QUIET(""),
    NORMAL(""),
    UNCOMFORTABLE(""),
    HARD("과다")
}

enum class VisualLevel(val label: String){
    LOW(""),
    NORMAL(""),
    HIGH(""),
    VERY_HIGH(""),
}

enum class SocialLevel(val label: String){
    MANY(""),
    SOME(""),
    LITTLE(""),
    ALONE(""),
}
enum class EnergyLevel(val label: String){
    ENOUGH(""),
    NORMAL(""),
    LOW(""),
    NONE(""),
}

enum class SensitivityLevel(val label: String){
    LOW("낮음"),
    NORMAL("보통"),
    HIGH("높음")
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

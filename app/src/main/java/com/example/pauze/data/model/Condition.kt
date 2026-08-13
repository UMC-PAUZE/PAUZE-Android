package com.example.pauze.data.model

enum class SleepLevel(val label: String, val severityRank: Int) {
    OVER_8("8시간 이상", 1),
    SIX_TO_EIGHT("6~8시간", 2),
    FOUR_TO_SIX("4~6시간", 3),
    LESS_4("4시간 미만", 4)
}
enum class NoiseLevel(val label: String, val severityRank: Int) {
    QUIET("적음", 1),
    NORMAL("보통", 2),
    UNCOMFORTABLE("불편", 3),
    HARD("과다", 4)
}

enum class VisualLevel(val label: String, val severityRank: Int){
    LOW("적음", 1),
    NORMAL("보통", 2),
    HIGH("불편", 3),
    VERY_HIGH("과다", 4),
}

enum class SocialLevel(val label: String, val severityRank: Int){
    MANY("피곤", 4),
    SOME("보통", 3),
    LITTLE("적음", 2),
    ALONE("없음", 1),
}
enum class EnergyLevel(val label: String, val severityRank: Int){
    ENOUGH("편안", 1),
    NORMAL("보통", 2),
    LOW("부족", 3),
    NONE("소진", 4),
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

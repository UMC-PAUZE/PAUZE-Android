package com.example.pauze.data.model

// todo: 확정되면 수정
// 민감 지수
enum class OverallIndex(val label: String) {
    Low("낮음"), Moderate("보통"), High("높음")
}
// 수면
enum class Sleeping(val label: String){
    Low("부족"), Moderate("보통"), High("충분")
}
// 소음
enum class Noise(val label: String) {
    Low("적음"), Moderate("보통"), High("심함")
}
// 사회 활동
enum class Activity(val label: String) {
    Low("적음"), Moderate("보통"), High("많음")
}

data class Condition(
    val score: Int,
    val index: OverallIndex,
    val sleeping: Sleeping,
    val noise: Noise,
    val activity: Activity
)
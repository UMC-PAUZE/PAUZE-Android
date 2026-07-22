package com.example.pauze.data.model

// 확정되면 수정
enum class Sleeping{ Lack, Moderate, Enough }
enum class Noise { Piercing, Moderate, Faint }
enum class Activity { Less, Moderate, Much }

data class Condition(
    val score: Int,
    val sleeping: Sleeping,
    val noise: Noise,
    val activity: Activity
)
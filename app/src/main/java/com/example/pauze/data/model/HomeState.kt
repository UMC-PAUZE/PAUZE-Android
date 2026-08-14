package com.example.pauze.data.model

import javax.inject.Inject

data class HomeState(
    val nickname: String = "사용자님",
    val condition: Condition? = null,
    val isTodayConditionExists: Boolean = false,
)
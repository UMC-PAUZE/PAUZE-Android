package com.example.pauze.data.model

import javax.inject.Inject

data class HomeState(
    val nickname: String = "게스트",
    val condition: Condition? = null,
    val isTodayConditionExists: Boolean = false,
)
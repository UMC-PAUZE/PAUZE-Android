package com.example.pauze.data.model

import java.util.Collections.emptyList

data class PauzeOverloadState(
    val instantActions: List<InstantAction> = emptyList(),
    val restGuideList: List<RestGuide> = emptyList()
)
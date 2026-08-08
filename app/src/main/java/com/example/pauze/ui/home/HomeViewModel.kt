package com.example.pauze.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.dummies.conditions
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.HomeState
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface HomeEffect {
    object MoveToTodayCondition: HomeEffect
    object MoveToBreathingBtn: HomeEffect
    object MoveToReportScreen: HomeEffect
}
class HomeViewModel: BaseViewModel<HomeEffect, HomeState>(
    uiState = BaseUiState(data = HomeState())
) {
    init {
        getCondition()
    }

    fun getCondition(){
        launch {
            val example = conditions.first()
            updateData {
                it.copy(condition = example, isTodayConditionExists = true)
            }
        }
    }
    fun moveToTodayCondition(){
        sendEffect(HomeEffect.MoveToTodayCondition)
    }
    fun moveToBreathing(){
        sendEffect(HomeEffect.MoveToBreathingBtn)
    }

    fun moveToReportScreen(){
        sendEffect(HomeEffect.MoveToReportScreen)
    }
}
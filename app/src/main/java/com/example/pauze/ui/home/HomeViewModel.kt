package com.example.pauze.ui.home

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.GetTodayConditionResponseDto
import com.example.pauze.data.model.HomeState
import com.example.pauze.data.model.isToday
import com.example.pauze.data.model.toCondition
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface HomeEffect {
    object MoveToTodayCondition: HomeEffect
    object MoveToBreathingBtn: HomeEffect
    object MoveToReportScreen: HomeEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TodayConditionRepository
) : BaseViewModel<HomeEffect, HomeState>(
    uiState = BaseUiState(data = HomeState())
) {
    init {
        getCondition()
    }

    fun getCondition() {
        launch {
            repository.getTodayCondition()?.toHomeState() ?: HomeState()
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

    private fun GetTodayConditionResponseDto.toHomeState() = HomeState(
        condition = toCondition(),
        isTodayConditionExists = isToday()
    )
}
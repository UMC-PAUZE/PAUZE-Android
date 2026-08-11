package com.example.pauze.ui.home

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.GetTodayConditionResponseDto
import com.example.pauze.data.model.HomeState
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

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

    private fun GetTodayConditionResponseDto.toHomeState(): HomeState {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
        return HomeState(
            condition = Condition(
                score = sensitivityScore,
                sleep = sleepLevel,
                noise = noiseLevel,
                visual = visualLevel,
                social = socialLevel,
                energy = energyLevel,
                sensitivity = sensitivityLevel
            ),
            isTodayConditionExists = conditionDate == today
        )
    }
}
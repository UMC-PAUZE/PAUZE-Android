package com.example.pauze.ui.home

import androidx.datastore.dataStore
import androidx.lifecycle.viewModelScope
import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.GetTodayConditionResponseDto
import com.example.pauze.data.model.HomeState
import com.example.pauze.data.model.User
import com.example.pauze.data.model.isToday
import com.example.pauze.data.model.toCondition
import com.example.pauze.data.repository.AuthRepository
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeEffect {
    object MoveToTodayCondition: HomeEffect
    object MoveToBreathingBtn: HomeEffect
    object MoveToReportScreen: HomeEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val conditionRepository: TodayConditionRepository
) : BaseViewModel<HomeEffect, HomeState>(
    uiState = BaseUiState(data = HomeState())
) {
    init {
        getUserAndCondition()
    }

    fun getUserAndCondition(){
        launch {
            TokenRepository.isInitialized.first { it }
            val user = async { authRepository.getUser() }
            val condition = async { conditionRepository.getTodayCondition() }
            toHomeState(
                user.await(),
                condition.await()
            )
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

    private fun toHomeState(user: User?, conditionDto: GetTodayConditionResponseDto?): HomeState {
        return HomeState(
            nickname = user?.nickname ?: "사용자님",
            condition = conditionDto?.toCondition(),
            isTodayConditionExists = conditionDto?.isToday() ?: false
        )
    }
}
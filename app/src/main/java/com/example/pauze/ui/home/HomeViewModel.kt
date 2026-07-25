package com.example.pauze.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pauze.data.dummies.conditions
import com.example.pauze.data.model.Condition
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface HomeEffect {
    object MoveToTodayCondition: HomeEffect
    object MoveToBreathingBtn: HomeEffect
}
class HomeViewModel: BaseViewModel<HomeEffect>() {
    private val _dummy = MutableStateFlow<Condition?>(null)
    val dummy = _dummy.asStateFlow()
    var isTodayConditionExists by mutableStateOf(false)

    init {
        getCondition()
    }

    fun getCondition(){
        launch {
            _dummy.value = conditions.first()
        }
    }
    fun moveToTodayCondition(){
        sendEffect(HomeEffect.MoveToTodayCondition)
    }
    fun moveToBreathing(){
        sendEffect(HomeEffect.MoveToBreathingBtn)
    }
}
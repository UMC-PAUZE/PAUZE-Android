package com.example.pauze.ui.pauze

import androidx.collection.ObjectList
import androidx.collection.objectListOf
import androidx.compose.runtime.mutableStateListOf
import com.example.pauze.data.dummies.actions
import com.example.pauze.data.dummies.guideList
import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.InstantAction
import com.example.pauze.data.model.PauzeOverloadState
import com.example.pauze.data.model.RestGuide
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface PauzeOverloadEffect {
    object BackStack: PauzeOverloadEffect
    object NavigateToFind: PauzeOverloadEffect
}

class PauzeOverloadViewModel: BaseViewModel<PauzeOverloadEffect, PauzeOverloadState>(
    uiState = BaseUiState(data = PauzeOverloadState())
) {

    init{
        getData()
    }

    fun getData(){
        launch {
            // todo: 레포지토리로 불러오기
            val actionExample = actions
            val guideExample = guideList
            updateData {
                it.copy(
                    instantActions = actionExample,
                    restGuideList = guideExample
                )
            }
        }
    }
    fun backStack(){
        sendEffect(PauzeOverloadEffect.BackStack)
    }
    fun navigateToFind(){
        sendEffect(PauzeOverloadEffect.NavigateToFind)
    }
}
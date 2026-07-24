package com.example.pauze.ui.pauze

import androidx.collection.ObjectList
import androidx.collection.objectListOf
import androidx.compose.runtime.mutableStateListOf
import com.example.pauze.data.dummies.actions
import com.example.pauze.data.dummies.guideList
import com.example.pauze.data.model.InstantAction
import com.example.pauze.data.model.RestGuide
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface PauzeOverloadEffect {
    object BackStack: PauzeOverloadEffect
}

class PauzeOverloadViewModel: BaseViewModel<PauzeOverloadEffect>() {
    private val _instantActions = MutableStateFlow<ObjectList<InstantAction>>(objectListOf())
    val instantActions = _instantActions.asStateFlow()

    private val _restGuideList = MutableStateFlow<ObjectList<RestGuide>>(objectListOf())
    val restGuideList = _restGuideList.asStateFlow()

    init{
        getInstantActions()
        getRestGuideList()
    }

    fun getInstantActions(){
        _instantActions.value = actions
    }
    fun getRestGuideList(){
        _restGuideList.value = guideList
    }
    fun backStack(){
        sendEffect(PauzeOverloadEffect.BackStack)
    }
}
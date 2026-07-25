package com.example.pauze.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.pauze.ui.BaseViewModel
import kotlinx.datetime.LocalDate
import androidx.compose.runtime.mutableIntStateOf

sealed interface SignUpEffect {
    object BackStack: SignUpEffect
    object NavigateToPolicy: SignUpEffect
    object NavigateToCompleted: SignUpEffect
}

class SignUpViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel<SignUpEffect>(){
    val initialAgreed = savedStateHandle.toRoute<LoginNavDestination.SignUp>().isAgreed
    var isAgreed by mutableStateOf(initialAgreed)
        private set

    var phase by mutableIntStateOf(0)
    var name by mutableStateOf("")
    var birthday by mutableStateOf<LocalDate?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")


    fun updateIsAgreed(isAgreed: Boolean){
        this.isAgreed = isAgreed
    }

    fun backStack(){
        sendEffect(SignUpEffect.BackStack)
    }

    fun signUp(){
        sendEffect(SignUpEffect.NavigateToCompleted)
    }

    fun checkPolicy(){
        sendEffect(SignUpEffect.NavigateToPolicy)
    }
}
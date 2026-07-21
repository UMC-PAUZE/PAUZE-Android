package com.example.pauze.ui.login

import kotlinx.serialization.Serializable

sealed interface LoginNavDestination {
    @Serializable
    object Login: LoginNavDestination
    @Serializable
    object Home: LoginNavDestination
    @Serializable
    data class SignUp(val isAgreed: Boolean) : LoginNavDestination
    @Serializable
    object Policy: LoginNavDestination
    @Serializable
    data class Completed(val name: String): LoginNavDestination
    @Serializable
    object Kakao: LoginNavDestination
}
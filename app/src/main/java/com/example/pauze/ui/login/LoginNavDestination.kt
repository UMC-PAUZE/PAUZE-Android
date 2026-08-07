package com.example.pauze.ui.login

import kotlinx.serialization.Serializable

sealed interface LoginNavDestination {
    @Serializable
    object Login: LoginNavDestination
    @Serializable
    data class SignUp(val isAgreedToTerm: Boolean, val isAgreedToPolicy: Boolean) : LoginNavDestination
    @Serializable
    data class Policy(val isTermOfUse: Boolean): LoginNavDestination
    @Serializable
    data class Completed(val name: String): LoginNavDestination
    @Serializable
    data class Kakao(val isAgreedToTerm: Boolean, val isAgreedToPolicy: Boolean): LoginNavDestination
}
package com.example.pauze.data.model

data class SignUpState (
    val localSignUpResult: LocalSignUpResult? = null,
    val verifyEmailRequest: VerifyEmailResult? = null
)

data class LoginState(
    val localLoginState: LocalLoginResult? = null
)
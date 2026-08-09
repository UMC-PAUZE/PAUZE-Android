package com.example.pauze.data.repository

import android.content.Context
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LocalSignUpRequest
import com.example.pauze.data.model.LocalSignUpResult
import com.example.pauze.data.model.Token
import com.example.pauze.data.model.User
import com.example.pauze.data.model.VerifyEmailResult

interface AuthRepository {
    suspend fun localSignUp(request: LocalSignUpRequest): LocalSignUpResult?
    suspend fun verifyEmail(email: String, code: String): VerifyEmailResult?
    suspend fun login(email: String, password: String): LocalLoginResult?
    suspend fun refresh(refreshToken: String): Token?
    suspend fun kakaoLogin(context: Context): KakaoLoginResult?
}
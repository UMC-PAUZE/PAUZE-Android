package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.KakaoLoginRequest
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.LocalLoginRequest
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LocalSignUpRequest
import com.example.pauze.data.model.LocalSignUpResult
import com.example.pauze.data.model.RefreshRequest
import com.example.pauze.data.model.Token
import com.example.pauze.data.model.User
import com.example.pauze.data.model.VerifyEmailRequest
import com.example.pauze.data.model.VerifyEmailResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/signup")
    suspend fun localSignUp(@Body request: LocalSignUpRequest): BaseResponse<LocalSignUpResult>

    @POST("auth/email/verify")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): BaseResponse<VerifyEmailResult>

    @POST("auth/login")
    suspend fun localLogin(@Body request: LocalLoginRequest): BaseResponse<LocalLoginResult>

    @POST("auth/kakao")
    suspend fun kakaoLogin(@Body request: KakaoLoginRequest): BaseResponse<KakaoLoginResult>

    @POST("auth/logout")
    suspend fun logout(@Body refreshToken: String?): BaseResponse<Unit>

    @GET("auth/me")
    suspend fun getUser(): BaseResponse<User>
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): BaseResponse<Token>
}
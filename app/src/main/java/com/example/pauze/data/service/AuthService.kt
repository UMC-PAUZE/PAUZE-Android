package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.ConfirmKakaoRequest
import com.example.pauze.data.model.ConfirmKakaoResult
import com.example.pauze.data.model.EmailAvailableResult
import com.example.pauze.data.model.KakaoLoginRequest
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.KakaoSignUpRequest
import com.example.pauze.data.model.KakaoSignUpResult
import com.example.pauze.data.model.LinkAccountRequest
import com.example.pauze.data.model.LinkAccountResult
import com.example.pauze.data.model.LocalLoginRequest
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LocalSignUpRequest
import com.example.pauze.data.model.LocalSignUpResult
import com.example.pauze.data.model.NicknameAvailableResult
import com.example.pauze.data.model.RefreshOrLogoutRequest
import com.example.pauze.data.model.SendCodeForLinkingRequest
import com.example.pauze.data.model.SendCodeForLinkingResult
import com.example.pauze.data.model.SendCodeForSignUpRequest
import com.example.pauze.data.model.SendCodeForSignUpResult
import com.example.pauze.data.model.Token
import com.example.pauze.data.model.User
import com.example.pauze.data.model.VerifyEmailRequest
import com.example.pauze.data.model.VerifyEmailResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @GET("auth/email/availability")
    suspend fun isEmailAvailable(@Query("email") email: String): BaseResponse<EmailAvailableResult>
    @POST("auth/email/code")
    suspend fun sendCodeForSignUp(@Body result: SendCodeForSignUpRequest): BaseResponse<SendCodeForSignUpResult>
    @POST("auth/link/email-code")
    suspend fun sendCodeForLinking(@Body request: SendCodeForLinkingRequest): BaseResponse<SendCodeForLinkingResult>
    @POST("auth/email/verify")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): BaseResponse<VerifyEmailResult>
    @GET("auth/nickname/availability")
    suspend fun isNicknameAvailable(@Query("nickname") nickname: String): BaseResponse<NicknameAvailableResult>
    @POST("auth/signup")
    suspend fun localSignUp(@Body request: LocalSignUpRequest): BaseResponse<LocalSignUpResult>
    @POST("auth/login")
    suspend fun localLogin(@Body request: LocalLoginRequest): BaseResponse<LocalLoginResult>
    @POST("auth/kakao/signup")
    suspend fun kakaoSignUp(@Body request: KakaoSignUpRequest): BaseResponse<KakaoSignUpResult>
    @POST("auth/kakao")
    suspend fun kakaoLogin(@Body request: KakaoLoginRequest): BaseResponse<KakaoLoginResult>
    @GET("auth/me")
    suspend fun getUser(): BaseResponse<User>
    @POST("auth/signup/kakao-confirm")
    suspend fun confirmKakaoAccount(@Body request: ConfirmKakaoRequest): BaseResponse<ConfirmKakaoResult>
    @POST("auth/link")
    suspend fun linkAccount(@Body request: LinkAccountRequest): BaseResponse<LinkAccountResult>
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshOrLogoutRequest): BaseResponse<Token>
    @POST("auth/logout")
    suspend fun logout(@Body request: RefreshOrLogoutRequest?): BaseResponse<Unit>
}
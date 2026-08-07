package com.example.pauze.data.repository

import com.example.pauze.data.model.KakaoLoginRequest
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.LocalLoginRequest
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LocalSignUpRequest
import com.example.pauze.data.model.LocalSignUpResult
import com.example.pauze.data.model.RefreshRequest
import com.example.pauze.data.model.Token
import com.example.pauze.data.model.VerifyEmailRequest
import com.example.pauze.data.model.VerifyEmailResult
import com.example.pauze.data.service.AuthService
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.coroutines.CancellationException
import java.sql.DriverManager.println
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService
): AuthRepository{
    override suspend fun localSignUp(request: LocalSignUpRequest): LocalSignUpResult? {
        try {
            val response = service.localSignUp(request)
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun verifyEmail(email: String, code: String): VerifyEmailResult? {
        try {
            val response = service.verifyEmail(VerifyEmailRequest(email, code))
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): LocalLoginResult? {
        try {
            val response = service.localLogin(LocalLoginRequest(email, password))
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun refresh(refreshToken: String): Token? {
        try {
            val response = service.refreshToken(RefreshRequest(refreshToken))
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun kakaoLogin(): KakaoLoginResult? {
        try {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if(error != null) {
                    println("카카오 로그인 실패: ${error.message}")
                } else if (token != null) {
                    val accessToken = token.accessToken

                }
            }
            val response = service.kakaoLogin(KakaoLoginRequest(""))
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }
}
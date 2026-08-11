package com.example.pauze.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.pauze.data.datastore.AuthDataStore
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
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Dispatcher
import java.sql.DriverManager.println
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: AuthDataStore,
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun kakaoLogin(context: Context): KakaoLoginResult? {
        val accessToken = dataStore.getKakaoAccessToken(context).firstOrNull() ?: return null

        return try {
            val response = service.kakaoLogin(KakaoLoginRequest(accessToken))
            response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }
}
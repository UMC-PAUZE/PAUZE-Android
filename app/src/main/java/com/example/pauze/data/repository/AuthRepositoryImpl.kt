package com.example.pauze.data.repository

import android.content.Context
import com.example.pauze.data.datastore.AuthDataStore
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
import com.example.pauze.data.model.SendCodeForLinkingRequest
import com.example.pauze.data.model.SendCodeForLinkingResult
import com.example.pauze.data.model.SendCodeForSignUpRequest
import com.example.pauze.data.model.SendCodeForSignUpResult
import com.example.pauze.data.model.TermAgreement
import com.example.pauze.data.model.VerifyEmailRequest
import com.example.pauze.data.model.VerifyEmailResult
import com.example.pauze.data.service.AuthService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.firstOrNull
import java.sql.DriverManager.println
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: AuthDataStore,
    private val service: AuthService
): AuthRepository{
    override suspend fun isEmailAvailable(email: String): EmailAvailableResult? {
        try {
            val response = service.isEmailAvailable(email)
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun sendCodeForSignUp(email: String): SendCodeForSignUpResult? {
        try {
            val response = service.sendCodeForSignUp(SendCodeForSignUpRequest(email))
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun sendCodeForLinking(email: String, kakaoAccessToken: String): SendCodeForLinkingResult? {
        try {
            val response = service.sendCodeForLinking(SendCodeForLinkingRequest(email, kakaoAccessToken))
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

    override suspend fun isNicknameAvailable(nickname: String): NicknameAvailableResult? {
        try {
            val response = service.isNicknameAvailable(nickname)
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun localSignUp(
        name: String,
        nickname: String,
        birth: String,
        email: String,
        password: String,
        termAgreements: List<TermAgreement>
    ): LocalSignUpResult? {
        try {
            val response = service.localSignUp(
                LocalSignUpRequest(
                    name, nickname, birth, email, password, termAgreements
                )
            )
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

    override suspend fun kakaoSignUp(
        name: String,
        nickname: String,
        birth: String,
        kakaoAccessToken: String,
        termAgreements: List<TermAgreement>
    ): KakaoSignUpResult? {
        try {
            val response = service.kakaoSignUp(
                KakaoSignUpRequest(
                    name = name,
                    nickname= nickname,
                    birth = birth,
                    kakaoAccessToken = kakaoAccessToken,
                    termAgreements = termAgreements
                )
            )
            return response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }

    override suspend fun kakaoLogin(context: Context): KakaoLoginResult? {
        val kakaoAccessToken = dataStore.kakaoLoginAndGetToken(context).firstOrNull() ?: return null
        dataStore.saveKakaoAccessToken(kakaoAccessToken)

        return try {
            val response = service.kakaoLogin(KakaoLoginRequest(kakaoAccessToken))
            response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }

    }

    override suspend fun confirmKakaoAccount(
        context: Context,
        email: String
    ): ConfirmKakaoResult? {
        val kakaoAccessToken = dataStore.kakaoLoginAndGetToken(context).firstOrNull() ?: return null

        return try {
            val response = service.confirmKakaoAccount(ConfirmKakaoRequest(email, kakaoAccessToken))
            response.result
        } catch (e: CancellationException){
            println("작업이 사용자에 의해 취소되었습니다, ${e.message}")
            throw e
        } catch(e: Exception){
            println("예외 발생: ${e.message}")
            throw e
        }
    }


    override suspend fun linkAccount(
        context: Context,
        direction: String,
        email: String?
    ): LinkAccountResult? {
        val kakaoAccessToken = dataStore.kakaoLoginAndGetToken(context).firstOrNull() ?: return null

        return try {
            val response = service.linkAccount(LinkAccountRequest(direction, kakaoAccessToken, email))
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
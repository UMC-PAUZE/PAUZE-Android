package com.example.pauze.data.repository

import android.content.Context
import com.example.pauze.data.model.ConfirmKakaoResult
import com.example.pauze.data.model.EmailAvailableResult
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.KakaoSignUpResult
import com.example.pauze.data.model.LinkAccountResult
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.LocalSignUpResult
import com.example.pauze.data.model.NicknameAvailableResult
import com.example.pauze.data.model.SendCodeForLinkingResult
import com.example.pauze.data.model.SendCodeForSignUpResult
import com.example.pauze.data.model.TermAgreement
import com.example.pauze.data.model.Token
import com.example.pauze.data.model.User
import com.example.pauze.data.model.VerifyEmailResult

interface AuthRepository {
    suspend fun isEmailAvailable(email: String): EmailAvailableResult?
    suspend fun sendCodeForSignUp(email: String): SendCodeForSignUpResult?
    suspend fun sendCodeForLinking(email: String, kakaoAccessToken: String): SendCodeForLinkingResult?
    suspend fun verifyEmail(email: String, code: String): VerifyEmailResult?
    suspend fun isNicknameAvailable(nickname: String): NicknameAvailableResult?
    suspend fun localSignUp(name: String, nickname: String, birth: String, email: String, password: String, termAgreements: List<TermAgreement>): LocalSignUpResult?
    suspend fun login(email: String, password: String): LocalLoginResult?
    suspend fun kakaoSignUp(name: String, nickname: String, birth: String, kakaoAccessToken: String, termAgreements: List<TermAgreement>): KakaoSignUpResult?
    suspend fun kakaoLogin(context: Context): KakaoLoginResult?
    suspend fun getUser(): User?
    suspend fun confirmKakaoAccount(email: String): ConfirmKakaoResult?
    suspend fun linkAccount(direction: String, kakaoAccessToken: String, email: String?, password: String?): LinkAccountResult?
    suspend fun refreshToken(): Token?
    suspend fun logout(): Boolean
}
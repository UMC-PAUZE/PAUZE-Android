package com.example.pauze.data.model

import com.example.pauze.data.KakaoLoginResultSerializer
import com.example.pauze.data.SendCodeForSignUpResultSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 이메일 사용 가능 여부
@Serializable
data class EmailAvailableResult(
    val status: String,
    val email: String
)
@Serializable
data class SendCodeForSignUpRequest(
    val email: String,
)

// 회원가입 이메일 인증코드 발송
@Serializable(with = SendCodeForSignUpResultSerializer::class)
sealed interface SendCodeForSignUpResult {
    @Serializable
    data class Success (
        val email: String,
        val expiresIn: Long,
        val nextStep: String
    ): SendCodeForSignUpResult

    @Serializable
    data class KakaoExists (
        val existingSocialType: String,
        val email: String,
        val nextStep: String
    ): SendCodeForSignUpResult

    @Serializable
    data class Failure (
        val result: String?
    ): SendCodeForSignUpResult
}

// 연동 이메일 인증코드 발송
@Serializable
data class SendCodeForLinkingRequest (
    val email: String,
    val kakaoAccessToken: String
)
@Serializable
data class SendCodeForLinkingResult (
    val email: String,
    val expiresIn: Long,
    val nextStep: String
)

// 이메일 인증코드 검증
@Serializable
data class VerifyEmailRequest(
    val email: String,
    val code: String
)
@Serializable
data class VerifyEmailResult(
    val email: String,
    val nextStep: String
)

// 닉네임 사용 가능 여부
@Serializable
data class NicknameAvailableResult(
    val available: Boolean,
    val nickname: String
)

// 로컬 회원가입
@Serializable
data class LocalSignUpRequest (
    val name: String,
    val nickname: String,
    val birth: String,
    val email: String,
    val password: String,
    val termAgreements: List<TermAgreement>
)
@Serializable
data class LocalSignUpResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

// 로컬 로그인
@Serializable
data class LocalLoginRequest(
    val email: String,
    val password: String
)
@Serializable
data class LocalLoginResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
)

// 카카오 회원가입
@Serializable
data class KakaoSignUpRequest(
    val name: String,
    val nickname: String,
    val birth: String,
    val kakaoAccessToken: String,
    val termAgreements: List<TermAgreement>
)
@Serializable
data class KakaoSignUpResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

// 앱 소셜 로그인(카카오)
@Serializable
data class KakaoLoginRequest(
    val kakaoAccessToken: String
)
@Serializable(with = KakaoLoginResultSerializer::class)
sealed interface KakaoLoginResult{
    @Serializable
    data class LoginSuccess(
        val accessToken: String,
        val refreshToken: String,
        val user: User,
    ): KakaoLoginResult

    @Serializable
    data class SignUp(
        val email: String,
        val nickname: String,
        val nextStep: String,
    ): KakaoLoginResult

    @Serializable
    data class HasLocalAccount(
        val existingSocialType: String,
        val email: String,
        val nextStep: String,
    ): KakaoLoginResult

    @Serializable
    data class Failure (
        val result: String?
    ): KakaoLoginResult
}

// 카카오 계정 확인(로컬가입 연동)
@Serializable
data class ConfirmKakaoRequest(
    val email: String,
    val kakaoAccessToken: String
)
@Serializable
data class ConfirmKakaoResult(
    val email: String,
    val expiresIn: Long,
    val nextStep: String
)

// 계정 연동
@Serializable
data class LinkAccountRequest(
    val direction: String,
    val kakaoAccessToken: String,
    val email: String? = null,
    val password: String? = null,
)
@Serializable
data class LinkAccountResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

// 토큰 갱신
@Serializable
data class RefreshOrLogoutRequest(
    val refreshToken: String
)

@Serializable
data class TermAgreement(
    val termId: Int,
    val agreed: Boolean
)
@Serializable
data class Token (
    val accessToken: String,
    val refreshToken: String,
)
@Serializable
data class User (
    @SerialName("uid") val uid: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("nickname") val nickname: String = "",
    @SerialName("birth") val birth: String = "",
    @SerialName("role") val role: String? = null,
    @SerialName("socialTypes") val socialTypes: List<String>? = null
)
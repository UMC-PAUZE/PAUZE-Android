package com.example.pauze.data.model

import com.example.pauze.data.KakaoLoginResultSerializer
import com.example.pauze.data.SendCodeForSignUpResultSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailAvailableResult(
    val status: String,
    val email: String
)

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

@Serializable
data class NicknameAvailableResult(
    val available: Boolean,
    val nickname: String
)

@Serializable
data class LocalSignUpRequest (
    val name: String,
    val nickname: String,
    val birth: String,
    val email: String,
    val password: String,
    val termAgreement: List<TermsAgreement>
)
@Serializable
data class TermsAgreement(
    val termId: Int,
    val agreed: Boolean
)
@Serializable
data class LocalSignUpResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

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

@Serializable
data class KakaoSignUpRequest(
    val name: String,
    val nickname: String,
    val birth: String,
    val kakaoAccessToken: String
)
@Serializable
data class KakaoSignUpResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

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

@Serializable
data class LinkAccountRequest(
    val direction: String,
    val kakaoAccessToken: String,
    val email: String? = null,
)
@Serializable
data class LinkAccountResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

@Serializable
data class RefreshRequest(
    val refreshToken: String
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
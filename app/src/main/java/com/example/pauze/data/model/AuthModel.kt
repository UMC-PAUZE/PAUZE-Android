package com.example.pauze.data.model

import kotlinx.serialization.Serializable
import com.example.pauze.data.LoginResultSerializer
import kotlinx.serialization.SerialName
import java.io.Serial

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
sealed interface LocalSignUpResult {
    @Serializable
    data class Success(
        val email: String,
        val expiresIn: Long,
        val nextStep: String
    ): LocalSignUpResult

    @Serializable
    data class KakaoExists(
        val existingSocialType: String,
        val email: String,
        val nextStep: String,
    ): LocalSignUpResult
}

@Serializable
data class TermsAgreement(
    val termId: Int,
    val agreed: Boolean
)

@Serializable
data class VerifyEmailRequest(
    val email: String,
    val code: String
)

@Serializable
sealed interface VerifyEmailResult {
    @Serializable
    data class LocalSuccess(
        val accessToken: String,
        val refreshToken: String,
        val user: User,
    ): VerifyEmailResult

    @Serializable
    data class KakaoSuccess(
        val email: String,
        val nextStep: String
    ): VerifyEmailResult
}

@Serializable
data class LocalLoginRequest(
    val email: String,
    val password: String
)
@Serializable(with = LoginResultSerializer::class)
sealed interface LocalLoginResult{
    @Serializable
    data class Success(
        val accessToken: String,
        val refreshToken: String,
        val user: User,
    ): LocalLoginResult
    @Serializable
    data class KakaoExists(
        val existingSocialType: String,
        val email: String,
    ): LocalLoginResult

    @Serializable
    data class Failure (
        val result: String?
    ): LocalLoginResult
}

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
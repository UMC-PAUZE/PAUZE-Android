package com.example.pauze.data.remote

/**
 * 로그인 API가 연결되면 발급받은 access token을 이곳에 전달합니다.
 * 토큰을 코드에 하드코딩하지 않기 위한 임시 연결 지점입니다.
 */
object PauzeAuthSession {
    @Volatile
    private var accessToken: String? = null

    fun updateAccessToken(token: String?) {
        accessToken = token?.trim()?.takeIf { it.isNotEmpty() }
    }

    fun optionalBearerToken(): String? = accessToken?.let { "Bearer $it" }

    fun requireBearerToken(): String = optionalBearerToken()
        ?: throw AuthenticationRequiredException()
}

class AuthenticationRequiredException : IllegalStateException("로그인이 필요한 기능입니다.")

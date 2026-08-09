package com.example.pauze.data.datastore

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.callbackFlow
import java.sql.DriverManager.println


class AuthDataStore {
    fun getKakaoAccessToken(context: Context) = callbackFlow{
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                println("카카오 로그인 실패: ${error.message}")
                trySend(null)
            } else if (token != null) {
                trySend(token.accessToken)
            }
            close()
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}
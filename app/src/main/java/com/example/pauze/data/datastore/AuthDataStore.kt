package com.example.pauze.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import java.sql.DriverManager.println
import javax.inject.Inject


class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KAKAO_ACCESS_TOKEN = stringPreferencesKey("kakao_access_token")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    // 토큰 가져오기
    suspend fun getKakaoAccessToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[KAKAO_ACCESS_TOKEN]
    }
    suspend fun getAccessToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[ACCESS_TOKEN]
    }
    suspend fun getRefreshToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[REFRESH_TOKEN]
    }
    // 토큰 저장
    suspend fun saveKakaoAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KAKAO_ACCESS_TOKEN] = token
        }
    }
    suspend fun saveAccessToken(token: String){
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }
    suspend fun saveRefreshToken(token: String){
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }


    // 카카오 로그인
    fun kakaoLoginAndGetToken(context: Context) = callbackFlow{
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                println("카카오 로그인 실패: ${error.message}")
                trySend(null)
            } else if (token != null) {
                trySend(token.accessToken)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }

        awaitClose {}
    }
}
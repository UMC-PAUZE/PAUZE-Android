package com.example.pauze.data.datastore

import android.content.Context
import android.os.Build
import android.preference.PreferenceDataStore
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.callbackFlow
import java.sql.DriverManager.println
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
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
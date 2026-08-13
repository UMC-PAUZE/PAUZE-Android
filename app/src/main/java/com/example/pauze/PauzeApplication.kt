package com.example.pauze

import android.app.Application
import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.repository.TokenRepository
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PauzeApplication : Application(){
    @Inject
    lateinit var authDataStore: AuthDataStore

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_KEY)
        TokenRepository.init(authDataStore)
    }
}
package com.example.pauze.ui.login

import com.example.pauze.data.datastore.AuthDataStore
import com.example.pauze.data.repository.TokenRepository

suspend fun saveTokens(dataStore: AuthDataStore, accessToken: String, refreshToken: String){
    dataStore.saveAccessToken(accessToken)
    dataStore.saveRefreshToken(refreshToken)
    TokenRepository.updateAccessToken(accessToken)
}

suspend fun clearToken(dataStore: AuthDataStore) {
    dataStore.clearToken()
    TokenRepository.updateAccessToken(null)
}
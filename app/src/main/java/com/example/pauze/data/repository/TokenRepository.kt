package com.example.pauze.data.repository

import com.example.pauze.data.datastore.AuthDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
object TokenRepository {
    @Volatile
    var accessToken: String? = null

    fun init(dataStore: AuthDataStore) {
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
        }
    }
}
package com.example.pauze.data.repository

import com.example.pauze.data.datastore.AuthDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
object TokenRepository {
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    @Volatile
    var accessToken: String? = null

    fun init(dataStore: AuthDataStore) {
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
            _isInitialized.value = true
        }
    }

    fun updateAccessToken(newToken: String?) {
        accessToken = newToken
    }
}
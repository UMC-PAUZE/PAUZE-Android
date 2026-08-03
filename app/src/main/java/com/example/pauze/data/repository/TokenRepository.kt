package com.example.pauze.data.repository

object TokenRepository {
    @Volatile
    var accessToken: String? = null
}
package com.example.pauze.data.module

import com.example.pauze.data.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = TokenRepository.accessToken

        val newRequest = if(token != null){
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }

        return chain.proceed(newRequest)
    }
}
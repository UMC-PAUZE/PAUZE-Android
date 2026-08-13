package com.example.pauze.data.service

import com.example.pauze.data.model.AuthTokenResultDto
import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.LocalLoginRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LocalLoginRequestDto
    ): BaseResponse<AuthTokenResultDto>
}

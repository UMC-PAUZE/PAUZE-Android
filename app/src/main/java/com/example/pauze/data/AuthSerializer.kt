package com.example.pauze.data

import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.SendCodeForSignUpResult
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

// 회원가입 이메일 인증코드 발송 결과 파서
object SendCodeForSignUpResultSerializer: JsonContentPolymorphicSerializer<SendCodeForSignUpResult>(SendCodeForSignUpResult::class){
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SendCodeForSignUpResult>{
        val jsonObject = element.jsonObject

        val hasExistingSocialType = jsonObject.containsKey("existingSocialType")
        val hasEmail = jsonObject.containsKey("email")

        return if(hasExistingSocialType){
            SendCodeForSignUpResult.KakaoExists.serializer()
        } else if(hasEmail) {
            SendCodeForSignUpResult.Success.serializer()
        } else {
            SendCodeForSignUpResult.Failure.serializer()
        }
    }
}

// 앱 소셜 로그인(카카오) 결과 파서
object KakaoLoginResultSerializer: JsonContentPolymorphicSerializer<KakaoLoginResult>(KakaoLoginResult::class){
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<KakaoLoginResult>{
        val jsonObject = element.jsonObject

        val hasAccessToken = jsonObject.containsKey("accessToken")
        val hasNextStep = jsonObject.containsKey("nextStep")
        val hasExistingSocialType = jsonObject.containsKey("existingSocialType")

        return if(hasAccessToken){
            KakaoLoginResult.LoginSuccess.serializer()
        } else if(hasExistingSocialType) {
            KakaoLoginResult.HasLocalAccount.serializer()
        } else if(hasNextStep){
            KakaoLoginResult.SignUp.serializer()
        } else{
            KakaoLoginResult.Failure.serializer()
        }
    }
}
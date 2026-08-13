package com.example.pauze.data

import android.util.Log
import com.example.pauze.data.model.KakaoLoginResult
import com.example.pauze.data.model.LocalLoginResult
import com.example.pauze.data.model.SendCodeForSignUpResult
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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

object KakaoLoginResultSerializer: JsonContentPolymorphicSerializer<KakaoLoginResult>(KakaoLoginResult::class){
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<KakaoLoginResult>{
        val jsonObject = element.jsonObject

        val hasAccessToken = jsonObject.containsKey("accessToken")
        val hasNextStep = jsonObject.containsKey("nextStep")
        val hasExistingSocialType = jsonObject.containsKey("existingSocialType")

        return if(hasAccessToken){
            KakaoLoginResult.LoginSuccess.serializer()
        } else if(hasNextStep && hasExistingSocialType) {
            KakaoLoginResult.HasLocalAccount.serializer()
        } else if(hasNextStep){
            KakaoLoginResult.SignUp.serializer()
        } else{
            KakaoLoginResult.Failure.serializer()
        }
    }
}
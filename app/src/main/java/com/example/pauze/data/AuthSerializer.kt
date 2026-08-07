package com.example.pauze.data

import android.util.Log
import com.example.pauze.data.model.LocalLoginResult
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object LoginResultSerializer: JsonContentPolymorphicSerializer<LocalLoginResult>(LocalLoginResult::class){
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<LocalLoginResult>{
        val jsonObject = element.jsonObject

        val hasAccessToken = jsonObject.containsKey("accessToken")
        val hasExistingSocialType = jsonObject.containsKey("existingSocialType")

        return if(hasAccessToken) {
            LocalLoginResult.Success.serializer()
        } else if(hasExistingSocialType){
            LocalLoginResult.KakaoExists.serializer()
        } else {
            LocalLoginResult.Failure.serializer()
        }
    }
}
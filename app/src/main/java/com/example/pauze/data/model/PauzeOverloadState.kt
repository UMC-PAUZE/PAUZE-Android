package com.example.pauze.data.model

data class InstantAction(
    val duration: Int,
    val title: String,
    val image: Int,
)

data class RestGuide(
    val image: Int,
    val duration: Int,
    val title: String,
    val content: String
)
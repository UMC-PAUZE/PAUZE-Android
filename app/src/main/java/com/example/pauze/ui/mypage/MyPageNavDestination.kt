package com.example.pauze.ui.mypage

import kotlinx.serialization.Serializable

sealed interface MyPageNavDestination {
    val route: String
    @Serializable
    object Main : MyPageNavDestination { override val route = "MyPageMain" }
    @Serializable
    object ProfileEdit : MyPageNavDestination { override val route = "MyPageProfileEdit" }
    @Serializable
    object AccountInfo : MyPageNavDestination { override val route = "MyPageAccountInfo" }
}
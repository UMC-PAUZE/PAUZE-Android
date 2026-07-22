package com.example.pauze

import kotlinx.serialization.Serializable

sealed interface BottomNavDestination {
    @Serializable
    object Home: BottomNavDestination
    @Serializable
    object Report: BottomNavDestination
    @Serializable
    object Find: BottomNavDestination
    @Serializable
    object MyPage: BottomNavDestination
}
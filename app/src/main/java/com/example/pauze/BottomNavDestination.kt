package com.example.pauze

import kotlinx.serialization.Serializable

sealed interface BottomNavDestination {
    val route: String
    @Serializable
    object Home: BottomNavDestination {
        override val route = "Home"
    }
    @Serializable
    object Report: BottomNavDestination {
        override val route = "Report"
    }
    @Serializable
    object Find: BottomNavDestination {
        override val route = "Find"
    }
    @Serializable
    object MyPage: BottomNavDestination {
        override val route = "MyPage"
    }
}
package com.example.pauze.ui.pauze

import kotlinx.serialization.Serializable

sealed interface PauzeNavDestination {
    val route: String

    @Serializable
    object Start : PauzeNavDestination {
        override val route = "PauzeStart"
    }

    @Serializable
    object Breathing : PauzeNavDestination {
        override val route = "PauzeBreathing"
    }

    @Serializable
    object Sound : PauzeNavDestination {
        override val route = "PauzeSound"
    }

    @Serializable
    object Visual : PauzeNavDestination {
        override val route = "PauzeVisual"
    }

    @Serializable
    object Overload : PauzeNavDestination {
        override val route = "PauzeOverload"
    }
}
package com.example.pauze.ui.splash

import kotlinx.serialization.Serializable

sealed interface SplashNavDestination {
    @Serializable
    object Splash : SplashNavDestination
    @Serializable
    object Onboarding : SplashNavDestination
}
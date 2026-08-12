package com.example.pauze.ui.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = SplashNavDestination.Splash) {
                    composable<SplashNavDestination.Splash> {
                        SplashScreen(navController)
                    }
                    composable<SplashNavDestination.Onboarding> {
                        OnboardingScreen(this@SplashActivity)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // todo: Lottie JSON 받으면 애니메이션으로 교체 재생 완료 콜백에서 navigate
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(SplashNavDestination.Onboarding) {
            popUpTo(SplashNavDestination.Splash) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0))
    )
}
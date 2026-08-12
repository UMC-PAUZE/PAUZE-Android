package com.example.pauze.ui.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pauze.R
import com.example.pauze.ui.login.LoginActivity
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
                        SplashScreen(navController, this@SplashActivity)
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
fun SplashScreen(
    navController: NavController,
    context: Context,
    viewModel: SplashViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // todo: 추후 애니메이션 교체
    LaunchedEffect(Unit) {
        delay(2000)
        if(uiState.data == true){
            context.startActivity(
                Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
            )
            (context as? Activity)?.finish()
        } else{
            navController.navigate(SplashNavDestination.Onboarding){
                popUpTo(SplashNavDestination.Splash) { inclusive=true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(AppTheme.palette.gray.getColor(9))
    ){
        // 동작 확인을 위해 넣어둔 것
        Image(
            painterResource(R.drawable.pauze_login),
            contentDescription = "임시 로고"
        )
    }
}
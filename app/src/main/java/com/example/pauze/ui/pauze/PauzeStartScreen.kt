package com.example.pauze.ui.pauze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pauze.MainActivity
import com.example.pauze.R
import com.example.pauze.ui.component.Destination
import com.example.pauze.ui.component.NavigationButton
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingMdBold
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PauzeStartActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            MainPaletteTheme {
                val navController = rememberNavController()
                // 홈에서 호흡 화면으로 바로 이동
                val destination = intent.getStringExtra("Pauze Destination")
                LaunchedEffect(destination) {
                    if(destination == "PauzeBreathing"){
                        navController.navigate(PauzeNavDestination.Breathing)
                    }
                }
                NavHost(navController = navController, startDestination = PauzeNavDestination.Start){
                    composable<PauzeNavDestination.Start> {
                        PauzeStartScreen(this@PauzeStartActivity, navController)
                    }
                    composable<PauzeNavDestination.Breathing> {
                        PauzeBreathingScreen(navController)
                    }
                    composable<PauzeNavDestination.Sound> {
                        PauzeSoundScreen(onBackClick = {navController.popBackStack()})
                    }
                    composable<PauzeNavDestination.Visual> {
                        PauzeVisualScreen(navController)
                    }
                    composable<PauzeNavDestination.Overload> {
                        PauzeOverloadScreen(this@PauzeStartActivity, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun PauzeStartScreen(
    context: Context = LocalContext.current,
    navController: NavController = rememberNavController(),
    viewModel: PauzeStartViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PauzeStartEffect.NavigateToBreathing -> {
                    navController.navigate(PauzeNavDestination.Breathing)
                }
                is PauzeStartEffect.NavigateToAuditory -> {
                    navController.navigate(PauzeNavDestination.Sound)
                }
                is PauzeStartEffect.NavigateToVisual -> {
                    navController.navigate(PauzeNavDestination.Visual)
                }
                is PauzeStartEffect.NavigateToGuide -> {
                    navController.navigate(PauzeNavDestination.Overload)
                    viewModel.onGuideNavigationHandled()
                }
                is PauzeStartEffect.NavigateToHome -> {
                    context.startActivity(
                        Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ){
        TopBar("Pauze", onBackClick = viewModel::onBackClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "지금 바로 안정해요",
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "현재 상황을 선택하면 맞춤 안정 콘텐츠를 시작해요.",
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            NavigationButton(Destination.PauzeBreathing, viewModel::onStartBreathingClick)

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "어떤 안정이 필요한가요?",
                style = bodyTextXlBold,
                color = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                SelectionCard(
                    iconRes = R.drawable.ic_sound,
                    title = "청각",
                    titleColor = AppTheme.palette.tertiary.getColor(3),
                    description = "자연소리와 ASMR로 \n청각 자극을 낮춰요",
                    onClick = viewModel::onAuditoryClick
                )
                SelectionCard(
                    iconRes = R.drawable.ic_see,
                    title = "시각",
                    titleColor = AppTheme.palette.blue.getColor(2),
                    description = "화면을 어둡게 하고 명상 또는 호흡에 집중해요",
                    onClick = viewModel::onVisualClick
                )
                SelectionCard(
                    iconRes = R.drawable.ic_energy,
                    title = "과한 에너지 소모",
                    titleColor = AppTheme.palette.secondary.getColor(3),
                    description = "쉼 가이드를 따르거나 HSP \n큐레이션 게시판으로 이동해요.",
                    onClick = viewModel::onGuideClick
                )
            }
        }

    }
}

@Composable
fun SelectionCard(
    iconRes: Int,
    title: String,
    titleColor: Color = AppTheme.palette.gray.getColor(2),
    description: String,
    onClick: () -> Unit = {},
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(16.dp)
            .clickable(onClick = onClick)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = "$title 아이콘",
                    modifier = Modifier.size(48.dp),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = bodyTextXlBold,
                        color = titleColor
                    )
                    Text(
                        text = description,
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(4),
                        modifier = Modifier.width(152.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = "이동하기 > 화살표",
                modifier = Modifier.size(24.dp),
                tint = AppTheme.palette.gray.getColor(5)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PauzeStartPreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        val previewViewModel = PauzeStartViewModel()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
        ){
            PauzeStartScreen(
                context = LocalContext.current,
                navController = rememberNavController(),
                viewModel = previewViewModel
            )
        }

    }
}

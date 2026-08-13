package com.example.pauze.ui.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ButtonSize
import com.example.pauze.ui.component.PhaseBar
import com.example.pauze.ui.login.LoginActivity
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingMdBold

@Composable
fun OnboardingScreen(
    context: Context,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var currentPage by remember { mutableStateOf(0) }

    fun finishOnboarding() {
        viewModel.markOnboardingSeen()
        context.startActivity(
            Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        )
        (context as? Activity)?.finish()
    }

    val (title, description, icon) = when (currentPage) {
        0 -> Triple(
            buildAnnotatedString {
                append("당신의 ")
                withStyle(SpanStyle(color = AppTheme.palette.secondary.getColor(3))) {
                    append("민감함은")
                }
                append(" \n특별한 능력이에요")
            },
            "초민감자(HSP)는 전 세계 인구의 약 20%를 차지해요. 예민함은 고쳐야 할 문제가 아닌, 스스로 조절할 수 있는 특성이에요.",
            painterResource(R.drawable.ic_pauze_red)
        )
        1 -> Triple(
            buildAnnotatedString {
                withStyle(SpanStyle(color = AppTheme.palette.tertiary.getColor(3))) {
                    append("오늘의 컨디션")
                }
                append("을 \n매일 측정해요")
            },
            "수면, 소음 노출, 사회 활동 등 일상 데이터를 바탕으로 \n 현재 컨디션을 분석하고 맞춤형 알림을 드려요.",
            painterResource(R.drawable.ic_pauze_yellow)
        )
        else -> Triple(
            buildAnnotatedString {
                append("과부하 순간, \n")
                withStyle(SpanStyle(color = AppTheme.palette.primary.getColor(3))) {
                    append("PAUZE")
                }
                append(" 하세요")
            },
            "감각이 넘칠 때 PAUZE 버튼 하나로 호흡 가이드, 안정 사운드를 즉시 시작하세요. 어디서든, 오프라인에서도.",
            painterResource(R.drawable.ic_pauze_circle)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = AppTheme.palette.gray.getColor(9))
    ) {
        OnboardingPage(
            currentPage = currentPage,
            title = title,
            description = description,
            icon = icon,
            onNextClick = {
                if (currentPage < 2) currentPage++
                else finishOnboarding()
            },
            onSkipClick = {
                finishOnboarding()
            }
        )
    }
}

@Composable
fun OnboardingPage(
    currentPage: Int,
    title: AnnotatedString,
    description: String,
    icon: Painter,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    val buttonText = if (currentPage == 2) "시작하기" else "다음"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    PhaseBar(isWaiting = index > currentPage, modifier = Modifier.weight(1f))
                }
            }
            Text(
                text = "건너뛰기",
                style = bodyTextMdBold,
                color = AppTheme.palette.gray.getColor(2),
                modifier = Modifier
                    .clickable(onClick = onSkipClick)
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = icon, contentDescription = "온보딩 아이콘", modifier = Modifier.size(160.dp))
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = title,
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(label = buttonText, onClick = onNextClick, modifier = Modifier.fillMaxWidth())
    }
}
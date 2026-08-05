package com.example.pauze.ui.mypage

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pauze.BottomNavDestination
import com.example.pauze.R
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.login.LoginActivity
import com.example.pauze.ui.mypage.component.MySettings
import com.example.pauze.ui.mypage.component.MySettingsVariant
import com.example.pauze.ui.mypage.component.ProfileCard
import com.example.pauze.ui.mypage.component.SettingsSection
import com.example.pauze.ui.mypage.component.StatCard
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgMedium


@Composable
fun MyPageScreen(
    navController: NavController,
    isGuest: Boolean = true,
    viewModel: MyPageViewModel = hiltViewModel()
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showGuestDialog by remember { mutableStateOf(isGuest) }

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyPageEffect.NavigateToEdit -> navController.navigate(MyPageNavDestination.ProfileEdit)
                is MyPageEffect.NavigateToAccount -> navController.navigate(MyPageNavDestination.AccountInfo)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(title = "마이", showBackButton = false)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            ProfileCard(
                nickname = uiState.data.profile?.nickname ?:"",
                profileImageUrl = uiState.data.profile?.profileImageUrl,
                loginProvider = if ("KAKAO" in (uiState.data.profile?.socialTypes ?: emptyList())) "카카오 계정 연동" else null,
                onClick = viewModel::onProfileClick
            )

            Column {
                Text(
                    text = "나의 PAUZE 기록",
                    style = bodyTextLgMedium,
                    color = AppTheme.palette.gray.getColor(4),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(label = "총 측정", value = "${uiState.data.stats?.totalMeasurements ?: 0}회", modifier = Modifier.weight(1f))
                    StatCard(label = "연속 측정", value = "${uiState.data.stats?.consecutiveDays ?: 0}일", modifier = Modifier.weight(1f))
                    StatCard(
                        label = "평균 민감지수",
                        value = uiState.data.stats?.averageSensitivity?.let {
                            "${if (it % 1.0 == 0.0) it.toInt().toString() else "%.1f".format(it)}점"
                        } ?: "-",
                        valueColor = AppTheme.palette.tertiary.getColor(3),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            SettingsSection("알림 설정") {
                MySettings(
                    title = "일일 측정 리마인더",
                    caption = "매일 컨디션 입력 알림",
                    variant = MySettingsVariant.Toggle,
                    toggleSelected = viewModel.dailyReminder,
                    onClick = viewModel::toggleDailyReminder
                )
                MySettings(
                    title = "예민함 위험 알림",
                    caption = "수치가 높을 때 즉시 알림",
                    variant = MySettingsVariant.Toggle,
                    toggleSelected = viewModel.riskAlert,
                    onClick = viewModel::toggleRiskAlert
                )
            }

            SettingsSection("안정 콘텐츠 설정") {
                MySettings(
                    title = "호흡 가이드",
                    caption = "기본 안정 방법으로 사용",
                    variant = MySettingsVariant.Toggle,
                    toggleSelected = viewModel.breathingGuide,
                    onClick = viewModel::toggleBreathingGuide
                )
                MySettings(
                    title = "안정 사운드",
                    caption = "사운드 재생 활성화",
                    variant = MySettingsVariant.Toggle,
                    toggleSelected = viewModel.stabilitySound,
                    onClick = viewModel::toggleStabilitySound
                )
                MySettings(
                    title = "오프라인 콘텐츠",
                    caption = "사운드 미리 다운로드",
                    variant = MySettingsVariant.Toggle,
                    toggleSelected = viewModel.offlineContent,
                    onClick = viewModel::toggleOfflineContent
                )
            }

            SettingsSection("정보") {
                MySettings(
                    title = "계정 정보",
                    icon = painterResource(R.drawable.ic_information),
                    variant = MySettingsVariant.Button,
                    onClick = viewModel::onAccountInfoClick
                )
                MySettings(
                    title = "문의 및 피드백",
                    icon = painterResource(R.drawable.ic_chat),
                    variant = MySettingsVariant.Button
                )
                MySettings(
                    title = "개인정보 처리방침",
                    icon = painterResource(R.drawable.ic_security),
                    variant = MySettingsVariant.Button
                )
            }
        }
    }

    if (showGuestDialog){
        Dialog(
            title = "로그인하고 더 많은 컨텐츠를 즐겨보세요",
            content = "좋아요, 저장 기능은 로그인 후 이용할 수 있어요",
            btnCancel = "나중에 할게요",
            btnContinue = "로그인·회원가입",
            onDismissRequest = {
                showGuestDialog = false
                navController.navigate(BottomNavDestination.Home){
                    popUpTo(BottomNavDestination.Home)
                }
            },
            onContinue = {
                showGuestDialog = false
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        )
    }

}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MyPagePreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false){
        MyPageScreen(navController = rememberNavController())
    }
}
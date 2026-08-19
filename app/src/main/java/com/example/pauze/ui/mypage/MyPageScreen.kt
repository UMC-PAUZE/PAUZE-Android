package com.example.pauze.ui.mypage

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pauze.BottomNavDestination
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.component.LoginRequiredDialog
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

// 문의 및 피드백
private const val INQUIRY_URL = "https://walla.my/v/8sAHW459dJPZhZjZNRaR"

@Composable
fun MyPageScreen(
    navController: NavController,
    isGuest: Boolean = true,
    viewModel: MyPageViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showGuestDialog by remember(isGuest) { mutableStateOf(isGuest) }
    // 알림 설정 권한
    var showNotificationSettingsDialog by remember { mutableStateOf(false) }
    var pendingToggle by remember { mutableStateOf<(() -> Unit)?>(null) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) pendingToggle?.invoke() else showNotificationSettingsDialog = true
        pendingToggle = null
    }

    fun requireNotificationPermission(onGranted: () -> Unit) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            onGranted()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pendingToggle = onGranted
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            showNotificationSettingsDialog = true
        }
    }

    fun onNotificationToggle(isOn: Boolean, toggle: () -> Unit) {
        if (isOn) toggle() else requireNotificationPermission(toggle)
    }

    LifecycleResumeEffect(isGuest) {
       if (!isGuest) viewModel.refresh()
        onPauseOrDispose { }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyPageEffect.NavigateToEdit -> navController.navigate(MyPageNavDestination.ProfileEdit)
                is MyPageEffect.NavigateToAccount -> navController.navigate(MyPageNavDestination.AccountInfo)
                is MyPageEffect.OpenInquiry -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(INQUIRY_URL)))
                is MyPageEffect.OpenPolicy -> context.startActivity(
                    Intent(context, LoginActivity::class.java).putExtra("SHOW_POLICY_ONLY", true)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(title = "마이", showBackButton = false)

        if (uiState.data.isProfileLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.data.profileError != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.data.profileError ?: "정보를 불러오지 못했습니다",
                        color = AppTheme.palette.gray.getColor(2)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button("다시 시도", onClick = viewModel::refresh)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                ProfileCard(
                    nickname = uiState.data.profile?.nickname ?: "",
                    profileImageUrl = uiState.data.profile?.profileImageUrl,
                    loginProvider = if ("KAKAO" in (uiState.data.profile?.socialTypes
                            ?: emptyList())
                    ) "카카오 계정 연동" else null,
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
                        StatCard(
                            label = "총 측정",
                            value = statValue(uiState.data.isStatsLoading, uiState.data.statsError) {
                                "${uiState.data.stats?.totalMeasurements ?: 0}회"
                            },
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "연속 측정",
                            value = statValue(uiState.data.isStatsLoading, uiState.data.statsError) {
                                "${uiState.data.stats?.consecutiveDays ?: 0}일"
                            },
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "평균 민감지수",
                            value = statValue(uiState.data.isStatsLoading, uiState.data.statsError) {
                                uiState.data.stats?.averageSensitivity?.let(::formatSensitivity) ?: "-"
                            },
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
                        onClick = {
                            onNotificationToggle(
                                viewModel.dailyReminder,
                                viewModel::toggleDailyReminder
                            )
                        }
                    )
                    MySettings(
                        title = "예민함 위험 알림",
                        caption = "수치가 높을 때 즉시 알림",
                        variant = MySettingsVariant.Toggle,
                        toggleSelected = viewModel.riskAlert,
                        onClick = {
                            onNotificationToggle(
                                viewModel.riskAlert,
                                viewModel::toggleRiskAlert
                            )
                        }
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
                        variant = MySettingsVariant.Button,
                        onClick = viewModel::onInquiryClick
                    )
                    MySettings(
                        title = "개인정보 처리방침",
                        icon = painterResource(R.drawable.ic_security),
                        variant = MySettingsVariant.Button,
                        onClick = viewModel::onPolicyClick
                    )
                }
            }
        }
    }

    if (showGuestDialog){
        LoginRequiredDialog(
            onDismissRequest = {
                showGuestDialog = false
                navController.navigate(BottomNavDestination.Home){
                    popUpTo(BottomNavDestination.Home)
                }
            },
            onLoginClick = {
                showGuestDialog = false
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        )
    }

    if (showNotificationSettingsDialog) {
        Dialog(
            title = "알림 권한이 필요해요",
            content = "설정에서 알림을 허용해주세요",
            btnCancel = "취소",
            btnContinue = "설정으로 이동",
            onDismissRequest = { showNotificationSettingsDialog = false },
            onContinue = {
                showNotificationSettingsDialog = false
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                } else {
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                }
                context.startActivity(intent)
            }
        )
    }

}

private fun formatSensitivity(score: Double): String {
    val formatted = if (score % 1.0 == 0.0) score.toInt().toString() else "%.1f".format(java.util.Locale.KOREA, score)
    return "${formatted}점"
}

private fun statValue(isLoading: Boolean, error: String?, value: () -> String): String =
    if (isLoading || error != null) "-" else value()

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MyPagePreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false){
        MyPageScreen(navController = rememberNavController(), true)
    }
}

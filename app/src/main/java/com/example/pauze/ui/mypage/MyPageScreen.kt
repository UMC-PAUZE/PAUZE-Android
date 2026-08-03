package com.example.pauze.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pauze.R
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.mypage.component.MySettings
import com.example.pauze.ui.mypage.component.MySettingsVariant
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextXlMedium
import com.example.pauze.ui.theme.headingSmBold


@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

}

@Composable
private fun ProfileCard(
    nickname: String,
    loginProvider: String?,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(color = AppTheme.palette.gray.getColor(8), shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = AppTheme.palette.gray.getColor(7), shape = CircleShape),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = "프로필 이미지",
                tint = AppTheme.palette.gray.getColor(8),
                modifier = Modifier.size(width = 33.dp, height = 42.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = nickname,  style = bodyTextXlMedium, color = AppTheme.palette.gray.getColor(2))
            loginProvider?.let {
                Text(text = it, style= bodyTextMdRegular, color = AppTheme.palette.gray.getColor(5))
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = "이동하기 >",
            tint = AppTheme.palette.gray.getColor(5)
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.palette.gray.getColor(2),
){
    Column(
        modifier = modifier
            .height(78.dp)
            .background(color = AppTheme.palette.gray.getColor(8), shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = AppTheme.palette.gray.getColor(7), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(text = label, style = bodyTextSmMedium, color = AppTheme.palette.gray.getColor(4))
        Text(text = value, style = headingSmBold, color = valueColor)
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
){
    Column {
        Text(
            text = title,
            style = bodyTextLgMedium,
            color = AppTheme.palette.gray.getColor(4),
            modifier = Modifier.padding(vertical = 12.dp)
        )
        content()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MyPagePreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false){
        MyPageScreen(navController = rememberNavController())
    }
}
package com.example.pauze.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.R
import com.example.pauze.data.model.ReportPeriod
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.report.component.InsightCard
import com.example.pauze.ui.report.component.TriggerCard
import com.example.pauze.ui.report.component.AverageScoreCard
import com.example.pauze.ui.report.component.TodayConditionCard
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun ReportScreen(isGuest: Boolean = true, viewModel: ReportViewModel = viewModel()) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect){
                is ReportEffect.NavigateToConditionInput -> {
                    // todo: 오늘의 컨디션 입력 화면으로 이동
                }
                is ReportEffect.NavigateToLogin -> {
                    // todo: 로그인 화면으로 이동
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(
            title = "리포트",
            showBackButton = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Tab(
                text = "일별",
                selected = viewModel.selectedPeriod == ReportPeriod.DAILY,
                onClick = { viewModel.selectPeriod(ReportPeriod.DAILY) },
                modifier = Modifier.weight(1f)
            )
            Tab(
                text = "주별",
                selected = viewModel.selectedPeriod == ReportPeriod.WEEKLY,
                onClick = { viewModel.selectPeriod(ReportPeriod.WEEKLY) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (isGuest) {
                Spacer(modifier = Modifier.height(83.dp))
                GuestContent(viewModel::onGuestLoginClick)
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TodayConditionCard(viewModel::onConditionInputClick)
                    AverageScoreCard(viewModel.averageScore)
                    TriggerCard(viewModel.triggers)
                    InsightCard(viewModel.insight)
                    Spacer(modifier = Modifier.height(136.dp)) // 네비게이션 바 자리
                }
            }
        }
    }
}

@Composable
private fun GuestContent(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_headset),
            contentDescription = "게스트모드 헤드셋",
            modifier = Modifier.size(107.dp),
            tint = AppTheme.palette.gray.getColor(2)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "아직 기록이 없어요",
                style = headingSmBold,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )
            Text(
                text = "오늘의 컨디션을 입력하면 예민함 패턴을 분석해드려요",
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(4),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            label = "로그인하고 시작하기",
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoginClick
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ReportScreenGuestPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
        ) {
            ReportScreen(isGuest = false)
        }
    }
}

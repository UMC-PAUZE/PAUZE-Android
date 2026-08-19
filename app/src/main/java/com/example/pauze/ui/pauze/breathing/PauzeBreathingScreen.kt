package com.example.pauze.ui.pauze.breathing

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pauze.R
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.*
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pauze.data.model.BreathPhase
import com.example.pauze.ui.pauze.PreviewPauzeUsageRepository
import com.example.pauze.ui.pauze.component.PauzeBreathingCircle

@Composable
fun PauzeBreathingScreen(
    navController: NavController,
    viewModel: PauzeBreathingViewModel = hiltViewModel()
) {

    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BreathingEffect.NavigateToBack -> {
                    navController.popBackStack()
                }
                is BreathingEffect.ShowExitDialog -> {
                    showExitDialog = true
                }
            }
        }
    }

    val pattern = viewModel.patterns[viewModel.selectedTabIndex]

    // 애니메이션
    val progress = when (viewModel.breathState.phase) {
        BreathPhase.READY -> 0f
        BreathPhase.INHALE -> viewModel.breathState.second.toFloat() / pattern.inhale
        BreathPhase.HOLD -> 1f
        BreathPhase.EXHALE -> 1f - (viewModel.breathState.second.toFloat() / pattern.exhale)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
            .verticalScroll(rememberScrollState())
    ) {
        TopBar("즉각 안정", onBackClick = viewModel::onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        BreathTabBar(
            selectedIndex = viewModel.selectedTabIndex,
            onTabSelected = { viewModel.selectTab(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "${viewModel.currentCycle}/3",
                color = AppTheme.palette.gray.getColor(4),
                style = bodyTextXlMedium
            )
            Icon(
                painter = painterResource(R.drawable.ic_replay),
                contentDescription = "다시 시작",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { viewModel.reset() },
                tint = AppTheme.palette.gray.getColor(4)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PauzeBreathingCircle(
            progress = progress,
            secondsText = "${viewModel.breathState.second}",
            showCircle = viewModel.breathState.phase != BreathPhase.READY
        )

        Spacer(modifier = Modifier.height(26.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(color = AppTheme.palette.gray.getColor(8), shape = CircleShape)
                        .clickable(onClick = { viewModel.togglePlayPause() }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(if (viewModel.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = if (viewModel.isPlaying) "멈춤" else "재생",
                        tint = AppTheme.palette.gray.getColor(4),
                        modifier = Modifier.size(44.dp)
                    )
                }
                Text(
                    text = when(viewModel.breathState.phase){
                        BreathPhase.READY -> "준비하세요"
                        BreathPhase.INHALE -> "들이쉬세요"
                        BreathPhase.HOLD -> "참으세요"
                        BreathPhase.EXHALE -> "내쉬세요"
                    },
                    style = headingLgBold,
                    color = AppTheme.palette.gray.getColor(2),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                BreathStepText(
                    label = "들숨",
                    seconds = pattern.inhale,
                    isActive = viewModel.breathState.phase == BreathPhase.INHALE
                )
                BreathStepText(
                    label = "참기",
                    seconds = pattern.hold,
                    isActive = viewModel.breathState.phase == BreathPhase.HOLD
                )
                BreathStepText(
                    label = "날숨",
                    seconds = pattern.exhale,
                    isActive = viewModel.breathState.phase == BreathPhase.EXHALE
                )
            }
        }
    }

    if (showExitDialog) {
        Dialog(
            title = "즉각 안정을 중단하시겠어요?",
            content = "진행 과정은 저장되지 않습니다",
            btnCancel = "계속하기",
            btnContinue = "중단하기",
            onDismissRequest = {
                showExitDialog = false
                viewModel.onExitCancel()
            },
            onContinue = {
                showExitDialog = false
                viewModel.onExitConfirm()
            }
        )
    }
}
@Composable
private fun BreathTabBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
    ){
        Tab(
            text = "478 호흡",
            selected = selectedIndex == 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )
        Tab(
            text = "박스 호흡",
            selected = selectedIndex == 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )
        Tab(
            text = "간단 호흡",
            selected = selectedIndex == 2,
            onClick = { onTabSelected(2) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BreathStepText(label: String, seconds: Int, isActive: Boolean){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = bodyTextXlBold,
            color = AppTheme.palette.gray.getColor(if (isActive) 2 else 6)
        )
        Text(
            text = "${seconds}초",
            style = bodyTextMdMedium,
            color = AppTheme.palette.gray.getColor(if (isActive) 4 else 7)
        )
    }
}

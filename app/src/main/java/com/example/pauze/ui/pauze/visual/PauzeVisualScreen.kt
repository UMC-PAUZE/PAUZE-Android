package com.example.pauze.ui.pauze.visual

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

// 한 화면 안에서 선택·카운트다운·실행 단계를 순차 전환하기 위한 상태다.
enum class PauzeVisualStep {
    Start,
    SelectMethod,
    SelectTime,
    Countdown,
    Running
}

// 시간 선택 흐름은 공유하되 실행 화면만 호흡과 명상으로 분기한다.
enum class PauzeVisualMethod {
    BreathingGuide,
    Meditation
}

@Composable
fun PauzeVisualScreen(
    navController: NavController,
    viewModel: PauzeVisualViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 단계가 바뀌어도 사용자가 고른 방식과 시간을 유지해 이전 화면으로 돌아갈 수 있게 한다.
    var step by remember { mutableStateOf(PauzeVisualStep.SelectMethod) }
    var selectedMethod by remember { mutableStateOf<PauzeVisualMethod?>(null) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(5) }
    var selectedSecond by remember { mutableStateOf(0) }
    var countdownNumber by remember { mutableStateOf(3) }
    var showStopDialog by rememberSaveable { mutableStateOf(false) }

    val totalSeconds = selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond

    // 다이얼로그 노출 요청은 일회성 Effect로 받아 화면 상태와 ViewModel의 역할을 분리한다.
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PauzeVisualEffect.ShowStopDialog -> showStopDialog = true
                PauzeVisualEffect.HideStopDialog -> showStopDialog = false
            }
        }
    }

    when (step) {
        PauzeVisualStep.Start -> LaunchedEffect(Unit) {
            navController.popBackStack()
        }

        PauzeVisualStep.SelectMethod -> PauzeVisualMethodSelectScreen(
            selectedMethod = selectedMethod,
            onMethodSelect = { selectedMethod = it },
            isLoading = uiState.isLoading,
            hasError = selectedMethod == PauzeVisualMethod.BreathingGuide &&
                    uiState.error != null,
            onNextClick = {
                when (selectedMethod) {
                    PauzeVisualMethod.BreathingGuide -> {
                        viewModel.loadBreatheGuide {
                            step = PauzeVisualStep.SelectTime
                        }
                    }

                    PauzeVisualMethod.Meditation -> {
                        viewModel.loadVisualGuide {
                            step = PauzeVisualStep.SelectTime
                        }
                    }

                    null -> Unit
                }
            },
            onRetryClick = {
                when (selectedMethod) {
                    PauzeVisualMethod.BreathingGuide -> viewModel.loadBreatheGuide {
                        step = PauzeVisualStep.SelectTime
                    }

                    PauzeVisualMethod.Meditation -> viewModel.loadVisualGuide {
                        step = PauzeVisualStep.SelectTime
                    }

                    null -> Unit
                }
            },
            onBackClick = { step = PauzeVisualStep.Start }
        )

        PauzeVisualStep.SelectTime -> PauzeVisualTimeSelectScreen(
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            selectedSecond = selectedSecond,
            onHourSelect = { selectedHour = it },
            onMinuteSelect = { selectedMinute = it },
            onSecondSelect = { selectedSecond = it },
            onQuickTimeSelect = { hour, minute, second ->
                selectedHour = hour
                selectedMinute = minute
                selectedSecond = second
            },
            onStartClick = { step = PauzeVisualStep.Countdown },
            onBackClick = { step = PauzeVisualStep.SelectMethod }
        )

        PauzeVisualStep.Countdown -> Box(modifier = Modifier.fillMaxSize()) {
            // 시간 선택 화면 위에 오버레이를 유지해 카운트다운 전후의 레이아웃 변화를 막는다.
            PauzeVisualTimeSelectScreen(
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                selectedSecond = selectedSecond,
                onHourSelect = { selectedHour = it },
                onMinuteSelect = { selectedMinute = it },
                onSecondSelect = { selectedSecond = it },
                onQuickTimeSelect = { hour, minute, second ->
                    selectedHour = hour
                    selectedMinute = minute
                    selectedSecond = second
                },
                onStartClick = {},
                onBackClick = { step = PauzeVisualStep.SelectMethod }
            )

            PauzeVisualCountdownScreen(
                countdownNumber = countdownNumber,
                onCountdownChange = { countdownNumber = it },
                onCountdownFinish = { step = PauzeVisualStep.Running }
            )
        }

        PauzeVisualStep.Running -> when (selectedMethod) {
            PauzeVisualMethod.BreathingGuide -> PauzeVisualBreathingRunningScreen(
                totalSeconds = totalSeconds,
                breatheUrl = uiState.data.breatheUrl,
                showStopDialog = showStopDialog,
                onShowStopDialog = viewModel::showStopDialog,
                onStopClick = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                },
                onContinueClick = viewModel::hideStopDialog,
                onUsageThresholdReached = viewModel::recordCompletedUsage,
                onFinish = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                }
            )

            PauzeVisualMethod.Meditation -> PauzeVisualMeditationRunningScreen(
                totalSeconds = totalSeconds,
                visualUrl = uiState.data.visualUrl,
                showStopDialog = showStopDialog,
                onShowStopDialog = viewModel::showStopDialog,
                onStopClick = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                },
                onContinueClick = viewModel::hideStopDialog,
                onUsageThresholdReached = viewModel::recordCompletedUsage,
                onFinish = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                }
            )

            null -> LaunchedEffect(Unit) {
                step = PauzeVisualStep.SelectMethod
            }
        }
    }
}

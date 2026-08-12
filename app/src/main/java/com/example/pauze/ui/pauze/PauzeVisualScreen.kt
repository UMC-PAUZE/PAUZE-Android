package com.example.pauze.ui.pauze

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

// 시각 안정 화면 단계
enum class PauzeVisualStep {
    Start,
    SelectMethod,
    SelectTime,
    Countdown,
    Running
}

// 시각 안정 방식
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

    var step by remember { mutableStateOf(PauzeVisualStep.SelectMethod) }
    var selectedMethod by remember { mutableStateOf<PauzeVisualMethod?>(null) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(5) }
    var selectedSecond by remember { mutableStateOf(0) }
    var countdownNumber by remember { mutableStateOf(3) }
    var showStopDialog by rememberSaveable { mutableStateOf(false) }

    val totalSeconds = selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond

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
                        viewModel.loadVisualGuide {
                            step = PauzeVisualStep.SelectTime
                        }
                    }

                    PauzeVisualMethod.Meditation -> {
                        step = PauzeVisualStep.SelectTime
                    }

                    null -> Unit
                }
            },
            onRetryClick = {
                viewModel.loadVisualGuide {
                    step = PauzeVisualStep.SelectTime
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
                visualUrl = uiState.data.visualUrl,
                showStopDialog = showStopDialog,
                onShowStopDialog = viewModel::showStopDialog,
                onStopClick = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                },
                onContinueClick = viewModel::hideStopDialog,
                onFinish = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                }
            )

            PauzeVisualMethod.Meditation -> PauzeVisualMeditationRunningScreen(
                totalSeconds = totalSeconds,
                showStopDialog = showStopDialog,
                onShowStopDialog = viewModel::showStopDialog,
                onStopClick = {
                    viewModel.hideStopDialog()
                    step = PauzeVisualStep.Start
                },
                onContinueClick = viewModel::hideStopDialog,
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

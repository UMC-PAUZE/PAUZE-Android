package com.example.pauze.ui.pauze

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

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
fun PauzeVisualScreen() {
    var step by remember { mutableStateOf(PauzeVisualStep.SelectMethod) }
    var selectedMethod by remember { mutableStateOf<PauzeVisualMethod?>(null) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(5) }
    var selectedSecond by remember { mutableStateOf(0) }
    var countdownNumber by remember { mutableStateOf(3) }

    val totalSeconds = selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond

    when (step) {
        PauzeVisualStep.Start -> PauzeStartScreen()

        PauzeVisualStep.SelectMethod -> PauzeVisualMethodSelectScreen(
            selectedMethod = selectedMethod,
            onMethodSelect = { selectedMethod = it },
            onNextClick = { step = PauzeVisualStep.SelectTime },
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

        PauzeVisualStep.Running -> PauzeVisualRunningScreen(
            totalSeconds = totalSeconds,
            onStopClick = { step = PauzeVisualStep.Start },
            onFinish = { step = PauzeVisualStep.Start }
        )
    }
}

package com.example.pauze.ui.pauze
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// 시각 진정 화면 상태
enum class PauzeVisualStep {
    SelectMethod, // 호흡법, 명상 선택 화면
    SelectTime, // 시간 선택
    Countdown, // 시작 카운트다운
    Running // 호흡법 및 명상
}

// 시각 진정 상태
enum class PauzeVisualMethod {
    BreathingGuide, // 호흡법
    Meditation, // 명상
}

@Composable
fun PauzeVisualScreen() {
    var step by remember { mutableStateOf(PauzeVisualStep.SelectMethod) }
    var selectedMethod by remember { mutableStateOf<PauzeVisualMethod?>(null) }

    when (step) {
        PauzeVisualStep.SelectMethod -> {
            Text(text = "방식 선택 화면")
        }

        PauzeVisualStep.SelectTime -> {
            Text(text = "시간 선택 화면")
        }

        PauzeVisualStep.Countdown -> {
            Text(text = "카운트다운 화면")
        }

        PauzeVisualStep.Running -> {
            Text(text = "진행 중 화면")
        }
    }
}




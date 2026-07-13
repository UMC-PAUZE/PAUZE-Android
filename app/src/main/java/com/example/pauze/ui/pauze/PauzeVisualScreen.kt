package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingMdBold
import com.example.pauze.ui.theme.headingSmBold
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.example.pauze.R
import androidx.compose.foundation.layout.size
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.draw.alpha
import com.example.pauze.ui.theme.headingLgBold
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import kotlinx.coroutines.flow.filter
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.pauze.ui.component.TopBar
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextLgRegular

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

// 화면 상태에 따른 분기
@Composable
fun PauzeVisualScreen() {
    var step by remember { mutableStateOf(PauzeVisualStep.SelectMethod) } // 초기 화면

    var selectedMethod by remember { mutableStateOf<PauzeVisualMethod?>(null) } // 호흡/명상 상태

    var selectedHour by remember { mutableStateOf(0) } // 초기 시간 상태 0시간 5분 0초
    var selectedMinute by remember { mutableStateOf(5) }
    var selectedSecond by remember { mutableStateOf(0) }

    var countdownNumber by remember { mutableStateOf(3) } // 카운트 다운, 3초부터

    val totalSeconds = selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond
    var showStopDialog by remember { mutableStateOf(false) }

    when (step) {
        PauzeVisualStep.SelectMethod -> {
            PauzeVisualMethodSelectContent(
                selectedMethod = selectedMethod,
                onMethodSelect = { selectedMethod = it },
                onNextClick = { step = PauzeVisualStep.SelectTime }
            )
        }

        PauzeVisualStep.SelectTime -> {
            PauzeVisualTimeSelectContent(
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
                onStartClick = { step = PauzeVisualStep.Countdown }
            )
        }

        PauzeVisualStep.Countdown -> {
            Box(modifier = Modifier.fillMaxSize()) {
                PauzeVisualTimeSelectContent(
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
                    onStartClick = {}
                )

                PauzeVisualCountdownOverlay(
                    countdownNumber = countdownNumber,
                    onCountdownChange = { countdownNumber = it },
                    onCountdownFinish = {
                        step = PauzeVisualStep.Running
                    }
                )
            }
        }

        PauzeVisualStep.Running -> {
            PauzeVisualRunningContent(
                totalSeconds = totalSeconds,
                onStopClick = {
                    step = PauzeVisualStep.SelectTime
                }
            )
        }
    }
}

// 시각 안정 방법 선택 화면
@Composable
fun PauzeVisualMethodSelectContent(
    selectedMethod: PauzeVisualMethod?,
    onMethodSelect: (PauzeVisualMethod) -> Unit,
    onNextClick: () -> Unit
) {
    PauzeVisualStepLayout(
        title = "안정하고 싶은 방식을\n선택해주세요",
        description = "시각적 자극이 심할 때는 눈을 쉬게 해주세요.\n화면이 꺼지고, 소리와 호흡만으로 집중할 수 있는 환경이\n만들어집니다.",
        buttonText = "다음",
        buttonEnabled = selectedMethod != null,
        onButtonClick = onNextClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PauzeVisualMethodCard(
                title = "호흡 가이드",
                description = "4-7-8 호흡법으로\n신경계를 안정시켜요",
                selected = selectedMethod == PauzeVisualMethod.BreathingGuide,
                onClick = { onMethodSelect(PauzeVisualMethod.BreathingGuide) },
                modifier = Modifier.weight(1f)
            )

            PauzeVisualMethodCard(
                title = "명상",
                description = "아무 생각 없이\n소리에만 집중해요",
                selected = selectedMethod == PauzeVisualMethod.Meditation,
                onClick = { onMethodSelect(PauzeVisualMethod.Meditation) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// 호흡 가이드 / 명상 선택 카드 컴포넌트
@Composable
fun PauzeVisualMethodCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(176.dp)
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) AppTheme.palette.gray.getColor(0) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_round_headset),
            contentDescription = null,
            tint = AppTheme.palette.primary.getColor(0),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = bodyTextXlBold,
            color = AppTheme.palette.gray.getColor(0)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
            textAlign = TextAlign.Center
        )
    }
}

// 시간 선택 화면
@Composable
fun PauzeVisualTimeSelectContent(
    selectedHour: Int,
    selectedMinute: Int,
    selectedSecond: Int,
    onHourSelect: (Int) -> Unit,
    onMinuteSelect: (Int) -> Unit,
    onSecondSelect: (Int) -> Unit,
    onQuickTimeSelect: (Int, Int, Int) -> Unit,
    onStartClick: () -> Unit
) {
    val isStartEnabled = selectedHour != 0 || selectedMinute != 0 || selectedSecond != 0

    PauzeVisualStepLayout(
        title = "안정하고 싶은 시간을\n선택해주세요",
        description = "시각적 자극이 심할 때는 눈을 쉬게 해주세요.\n화면이 꺼지고, 소리와 호흡만으로 집중할 수 있는 환경이\n만들어집니다.",
        buttonText = "시작하기",
        buttonEnabled = isStartEnabled,
        onButtonClick = onStartClick
    ) {
        PauzeVisualTimeWheel(
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            selectedSecond = selectedSecond,
            onHourSelect = onHourSelect,
            onMinuteSelect = onMinuteSelect,
            onSecondSelect = onSecondSelect
        )

        Spacer(modifier = Modifier.height(40.dp))

        PauzeVisualQuickTimeRow(
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            selectedSecond = selectedSecond,
            onQuickTimeSelect = onQuickTimeSelect
        )
    }
}

// 시간 선택 휠 컴포넌트
@Composable
fun PauzeVisualTimeWheel(
    selectedHour: Int,
    selectedMinute: Int,
    selectedSecond: Int,
    onHourSelect: (Int) -> Unit,
    onMinuteSelect: (Int) -> Unit,
    onSecondSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PauzeVisualWheelColumn(
            values = (0..23).toList(),
            selectedValue = selectedHour,
            onValueChange = onHourSelect
        )

        PauzeVisualWheelColon()

        PauzeVisualWheelColumn(
            values = (0..59).toList(),
            selectedValue = selectedMinute,
            onValueChange = onMinuteSelect
        )

        PauzeVisualWheelColon()

        PauzeVisualWheelColumn(
            values = (0..59).toList(),
            selectedValue = selectedSecond,
            onValueChange = onSecondSelect
        )
    }
}

// 휠 숫자 컬럼
@Composable
fun PauzeVisualWheelColumn(
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {
    val loopCount = 100
    val middleLoop = loopCount / 2
    val valueSize = values.size

    val startIndex = remember(values, selectedValue) {
        val selectedIndex = values.indexOf(selectedValue).coerceAtLeast(0)
        middleLoop * valueSize + selectedIndex
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = startIndex
    )

    val flingBehavior = rememberSnapFlingBehavior(
        lazyListState = listState
    )

    val centeredIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }

    LaunchedEffect(selectedValue) {
        val currentValue = values[centeredIndex % valueSize]

        if (currentValue != selectedValue && !listState.isScrollInProgress) {
            val selectedIndex = values.indexOf(selectedValue).coerceAtLeast(0)
            val currentLoop = centeredIndex / valueSize
            val targetIndex = currentLoop * valueSize + selectedIndex

            listState.animateScrollToItem(targetIndex)
        }
    }

    LaunchedEffect(listState, values, selectedValue) {
        snapshotFlow {
            listState.isScrollInProgress to listState.firstVisibleItemIndex
        }.collect { (isScrollInProgress, index) ->
            if (!isScrollInProgress) {
                val value = values[index % valueSize]

                if (value != selectedValue) {
                    onValueChange(value)
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        contentPadding = PaddingValues(vertical = 64.dp),
        modifier = Modifier
            .width(62.dp)
            .height(168.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(loopCount * valueSize) { index ->
            val value = values[index % valueSize]

            PauzeVisualWheelNumber(
                text = "%02d".format(value),
                selected = index == centeredIndex
            )
        }
    }
}

// 휠 숫자 컴포넌트
@Composable
fun PauzeVisualWheelNumber(
    text: String,
    selected: Boolean
) {
    Text(
        text = text,
        style = headingLgBold.copy(
            fontSize = 40.sp,
            lineHeight = 40.sp
        ),
        color = AppTheme.palette.gray.getColor(0),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(62.dp)
            .height(40.dp)
            .alpha(if (selected) 1f else 0.18f)
    )
}

// 휠 콜론
@Composable
fun PauzeVisualWheelColon() {
    Text(
        text = " : ",
        style = headingLgBold.copy(
            fontSize = 40.sp,
            lineHeight = 40.sp
        ),
        color = AppTheme.palette.gray.getColor(0)
    )
}

// 빠른 시간 선택 Row
@Composable
fun PauzeVisualQuickTimeRow(
    selectedHour: Int,
    selectedMinute: Int,
    selectedSecond: Int,
    onQuickTimeSelect: (Int, Int, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PauzeVisualQuickTimeChip(
            text = "00 : 05 : 00",
            selected = selectedHour == 0 && selectedMinute == 5 && selectedSecond == 0,
            onClick = { onQuickTimeSelect(0, 5, 0) }
        )

        PauzeVisualQuickTimeChip(
            text = "00 : 10 : 00",
            selected = selectedHour == 0 && selectedMinute == 10 && selectedSecond == 0,
            onClick = { onQuickTimeSelect(0, 10, 0) }
        )

        PauzeVisualQuickTimeChip(
            text = "00 : 15 : 00",
            selected = selectedHour == 0 && selectedMinute == 15 && selectedSecond == 0,
            onClick = { onQuickTimeSelect(0, 15, 0) }
        )

        PauzeVisualQuickTimeChip(
            text = "00 : 20 : 00",
            selected = selectedHour == 0 && selectedMinute == 20 && selectedSecond == 0,
            onClick = { onQuickTimeSelect(0, 20, 0) }
        )
    }
}

// 빠른 시간 선택 칩
@Composable
fun PauzeVisualQuickTimeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(102.dp)
            .height(46.dp)
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .border(
                width = if (selected) 1.5.dp else 0.dp,
                color = if (selected) {
                    AppTheme.palette.gray.getColor(4)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(size = 16.dp)
            )
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = text,
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
            textAlign = TextAlign.Center
        )
    }
}

// 카운트다운 화면
@Composable
fun PauzeVisualCountdownOverlay(
    countdownNumber: Int,
    onCountdownChange: (Int) -> Unit,
    onCountdownFinish: () -> Unit
) {
    LaunchedEffect(Unit) {
        for (number in 3 downTo 1) {
            onCountdownChange(number)
            delay(1000L)
        }

        onCountdownFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = countdownNumber.toString(),
                style = headingLgBold.copy(
                    fontSize = 64.sp,
                    lineHeight = 64.sp
                ),
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "잠시 후 시작됩니다\n편안한 자세를 잡아주세요",
                style = bodyTextLgRegular,
                color = AppTheme.palette.gray.getColor(4),
                textAlign = TextAlign.Center
            )
        }
    }
}

// 시각 안정 선택 화면 및 시간 선택 화면 공통 레이아웃 컴포넌트
@Composable
fun PauzeVisualStepLayout(
    title: String,
    description: String,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar(
            title = "시각 안정",
            modifier = Modifier.padding(horizontal = 7.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(5)
            )

            Spacer(modifier = Modifier.height(72.dp))

            content()

            Spacer(modifier = Modifier.weight(1f))

            PauzeVisualBottomButton(
                text = buttonText,
                enabled = buttonEnabled,
                onClick = onButtonClick
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

//진행 중 화면
@Composable
fun PauzeVisualRunningContent(
    totalSeconds: Int,
    onStopClick: () -> Unit
) {
    var remainingSeconds by remember(totalSeconds) {mutableStateOf(totalSeconds)}

    LaunchedEffect(totalSeconds) {
        while (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds = (remainingSeconds - 1).coerceAtLeast(0)
        }
    }

    var minute = remainingSeconds / 60
    var second = remainingSeconds % 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%02d : %02d".format(minute, second),
                style = headingLgBold.copy(
                    fontSize = 64.sp,
                    lineHeight = 64.sp
                ),
                color = AppTheme.palette.gray.getColor(8)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "화면을 탭하면 종료됩니다.",
                style = bodyTextLgRegular,
                color = AppTheme.palette.gray.getColor(8)
            )
        }
    }
}


// 버튼 공통 컴포넌트
@Composable
fun PauzeVisualBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = if (enabled) {
                    AppTheme.palette.gray.getColor(2)
                } else {
                    AppTheme.palette.gray.getColor(7)
                },
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            style = headingSmBold,
            color = if (enabled) {
                AppTheme.palette.gray.getColor(9)
            } else {
                AppTheme.palette.gray.getColor(4)
            },
            textAlign = TextAlign.Center
        )
    }
}

// 시각 안정 선택 화면 Preview(미선택)
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PauzeVisualScreenPreview() {
    PAUZEAndroidTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        MainPaletteTheme {
            PauzeVisualScreen()
        }
    }
}

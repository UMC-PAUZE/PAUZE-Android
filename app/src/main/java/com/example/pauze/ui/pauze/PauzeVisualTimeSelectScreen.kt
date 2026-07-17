package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.ui.pauze.component.PauzeVisualStepLayout
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.headingLgBold

@Composable
fun PauzeVisualTimeSelectScreen(
    selectedHour: Int,
    selectedMinute: Int,
    selectedSecond: Int,
    onHourSelect: (Int) -> Unit,
    onMinuteSelect: (Int) -> Unit,
    onSecondSelect: (Int) -> Unit,
    onQuickTimeSelect: (Int, Int, Int) -> Unit,
    onStartClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val isStartEnabled = selectedHour != 0 || selectedMinute != 0 || selectedSecond != 0

    PauzeVisualStepLayout(
        title = "안정하고 싶은 시간을\n선택해주세요",
        description = "시각적 자극이 심할 때는 눈을 쉬게 해주세요.\n화면이 꺼지고, 소리와 호흡만으로 집중할 수 있는 환경이\n만들어집니다.",
        buttonText = "시작하기",
        buttonEnabled = isStartEnabled,
        onButtonClick = onStartClick,
        onBackClick = onBackClick
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

@Composable
private fun PauzeVisualTimeWheel(
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

@Composable
private fun PauzeVisualWheelColumn(
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

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val centeredIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
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

@Composable
private fun PauzeVisualWheelNumber(
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

@Composable
private fun PauzeVisualWheelColon() {
    Text(
        text = " : ",
        style = headingLgBold.copy(
            fontSize = 40.sp,
            lineHeight = 40.sp
        ),
        color = AppTheme.palette.gray.getColor(0)
    )
}

@Composable
private fun PauzeVisualQuickTimeRow(
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

@Composable
private fun PauzeVisualQuickTimeChip(
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
                color = if (selected) AppTheme.palette.gray.getColor(4) else Color.Transparent,
                shape = RoundedCornerShape(size = 16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
            textAlign = TextAlign.Center
        )
    }
}

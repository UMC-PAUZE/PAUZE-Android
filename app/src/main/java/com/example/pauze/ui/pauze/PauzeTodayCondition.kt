package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingMdBold

private data class ConditionQuestion(
    val title: String,
    val description: String,
    val choices: List<String>
)

private val conditionQuestions = listOf(
    ConditionQuestion(
        title = "지난 밤, 몇 시간동안\n수면을 취했나요?",
        description = "수면의 양과 질은 예민함에 큰 영향을 줘요.",
        choices = listOf("4시간 미만", "4~6시간", "6~8시간", "8시간 이상")
    ),
    ConditionQuestion(
        title = "오늘 소음 노출은\n어느 정도였나요?",
        description = "대중교통, 사무실 소음 등 모든 소음을 포함해요",
        choices = listOf("조용했어요", "감당 가능한 정도였어요", "불편했어요", "힘들 정도였어요")
    ),
    ConditionQuestion(
        title = "오늘의 시각 정보량은\n어땠나요?",
        description = "화면 시청, 광고, 밝은 조명 등 시각 자극 전체를 포함해요",
        choices = listOf("거의 없음", "약간 있음", "꽤 많았어요", "매우 많았어요")
    ),
    ConditionQuestion(
        title = "오늘은 사회적 활동을\n얼마나 했나요?",
        description = "대화, 회의, 모임 등 타인과의 상호작용 시간을\n알려주세요",
        choices = listOf("혼자였어요", "조금 있었어요", "꽤 있었어요", "많았어요")
    ),
    ConditionQuestion(
        title = "지금 가지고 있는 에너지는\n얼마나 되나요?",
        description = "현재 느끼는 신체적, 정서적 에너지를 알려주세요.",
        choices = listOf("완전 방전", "낮아요", "보통이에요", "충분해요")
    )
)

@Composable
fun PauzeTodayCondition(
    modifier: Modifier = Modifier
) {
    val answers = remember { mutableStateListOf<Int?>(null, null, null, null, null) }
    var currentQuestionIndex = remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val currentQuestion = conditionQuestions[currentQuestionIndex.intValue]
    val isPreviousEnabled = currentQuestionIndex.intValue > 0
    val isNextEnabled = answers[currentQuestionIndex.intValue] != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            title = "오늘의 컨디션",
            onBackClick = {},
            backgroundColor = AppTheme.palette.base.getColor(0)
        )

        Row(
            modifier = Modifier.width(312.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            conditionQuestions.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(
                            color = if (index <= currentQuestionIndex.intValue) {
                                AppTheme.palette.gray.getColor(1)
                            } else {
                                AppTheme.palette.gray.getColor(8)
                            },
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.width(312.dp)) {
            Text(
                text = currentQuestion.title,
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(1)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentQuestion.description,
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(5)
            )
        }

        // TopBar 하단에서 첫 번째 선택지까지 194dp 간격을 유지합니다.
        Spacer(modifier = Modifier.height(70.dp))

        Column(
            modifier = Modifier.width(312.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            currentQuestion.choices.forEachIndexed { choiceIndex, choice ->
                val isSelected = answers[currentQuestionIndex.intValue] == choiceIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) AppTheme.palette.gray.getColor(1)
                            else AppTheme.palette.gray.getColor(6),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .clickable { answers[currentQuestionIndex.intValue] = choiceIndex },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = choice,
                        style = bodyTextXlBold,
                        color = if (isSelected) AppTheme.palette.gray.getColor(1)
                        else AppTheme.palette.gray.getColor(4)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .width(312.dp)
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ConditionNavigationButton(
                text = "이전",
                enabled = isPreviousEnabled,
                modifier = Modifier.weight(1f),
                onClick = { currentQuestionIndex.intValue -= 1 }
            )
            ConditionNavigationButton(
                text = "다음",
                enabled = isNextEnabled,
                modifier = Modifier.weight(1f),
                onClick = {
                    if (currentQuestionIndex.intValue < conditionQuestions.lastIndex) {
                        currentQuestionIndex.intValue += 1
                    }
                }
            )
        }
    }
}

@Composable
private fun ConditionNavigationButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(
                color = if (enabled) {
                    if (text == "다음") AppTheme.palette.gray.getColor(1)
                    else AppTheme.palette.gray.getColor(7)
                } else {
                    AppTheme.palette.gray.getColor(8)
                },
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = bodyTextLgBold,
            color = if (enabled) {
                if (text == "다음") AppTheme.palette.base.getColor(0)
                else AppTheme.palette.gray.getColor(1)
            } else {
                AppTheme.palette.gray.getColor(9)
            }
        )
    }
}

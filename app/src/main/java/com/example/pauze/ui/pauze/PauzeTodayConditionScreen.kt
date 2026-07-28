package com.example.pauze.ui.pauze

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.MainActivity
import com.example.pauze.ui.component.CondtionAnswer
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.component.PhaseBar
import com.example.pauze.ui.component.SensitivityScoreBar
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.headingMdBold

class PauzeTodayConditionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                PauzeTodayCondition(onExitClick = ::finish)
            }
        }
    }
}

@Composable
fun PauzeTodayCondition(
    modifier: Modifier = Modifier,
    onExitClick: () -> Unit = {},
    viewModel: PauzeTodayConditionViewModel = viewModel()
) {
    val context = LocalContext.current
    val conditionState by viewModel.state.collectAsState()
    val conditionQuestions by viewModel.conditionQuestions.collectAsState()
    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    val currentQuestion = conditionQuestions[conditionState.currentQuestionIndex]

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TodayConditionEffect.ShowExitDialog -> showExitDialog = true
                TodayConditionEffect.NavigateBack -> onExitClick()
                TodayConditionEffect.NavigateToMainActivity -> {
                    context.startActivity(
                        Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                    )
                }
                TodayConditionEffect.NavigateToPauzeStartActivity -> {
                    context.startActivity(Intent(context, PauzeStartActivity::class.java))
                }
            }
        }
    }

    if (conditionState.showResult) {
        PauzeTodayConditionResult(
            score = conditionState.sensitivityScore,
            onHomeClick = viewModel::navigateToMainActivity,
            onPauzeStartClick = viewModel::navigateToPauzeStartActivity
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            title = "오늘의 컨디션",
            onBackClick = viewModel::onBackClick,
            backgroundColor = AppTheme.palette.base.getColor(0)
        )

        Row(
            modifier = Modifier.width(312.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            conditionQuestions.indices.forEach { index ->
                PhaseBar(
                    isWaiting = index > conditionState.currentQuestionIndex,
                    modifier = Modifier.weight(1f)
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
                val isSelected = conditionState.answers[conditionState.currentQuestionIndex] == choiceIndex
                CondtionAnswer(
                    text = choice,
                    isSelected = isSelected,
                    onClick = { viewModel.selectAnswer(choiceIndex) }
                )
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
                enabled = conditionState.isPreviousEnabled,
                modifier = Modifier.weight(1f),
                onClick = viewModel::moveToPreviousQuestion
            )
            ConditionNavigationButton(
                text = "다음",
                enabled = conditionState.isNextEnabled,
                modifier = Modifier.weight(1f),
                onClick = viewModel::moveToNextQuestion
            )
        }
    }

    if (showExitDialog) {
        Dialog(
            title = "오늘의 컨디션 작성하기를\n중단하시겠어요?",
            content = "작성한 내용은 저장되지 않습니다.",
            btnCancel = "중단하기",
            btnContinue = "계속하기",
            onDismissRequest = {
                showExitDialog = false
                viewModel.confirmExit()
            },
            onContinue = { showExitDialog = false }
        )
    }
}

@Composable
private fun PauzeTodayConditionResult(
    score: Int,
    onHomeClick: () -> Unit,
    onPauzeStartClick: () -> Unit
) {
    val normalizedScore = score.coerceIn(0, 100)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(184.dp))

        Column(
            modifier = Modifier
                .width(312.dp)
                .background(
                    color = AppTheme.palette.gray.getColor(8),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "측정 완료!",
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(1)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "오늘의 예민도",
                style = bodyTextLgBold,
                color = AppTheme.palette.gray.getColor(4)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = normalizedScore.toString(),
                    style = headingMdBold.copy(fontSize = 64.sp, lineHeight = 64.sp),
                    color = AppTheme.palette.gray.getColor(1)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "/ 100",
                    style = bodyTextXlBold,
                    color = AppTheme.palette.gray.getColor(5),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            SensitivityScoreBar(score = normalizedScore)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = AppTheme.palette.tertiary.getColor(8),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "보통",
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.tertiary.getColor(1)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "비교적 안정적인 상태예요",
                    style = bodyTextSmRegular,
                    color = AppTheme.palette.gray.getColor(4)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "오늘의 컨디션 분석을 완료했어요.\n자세한 결과는 홈 화면에서 확인할 수 있어요.",
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(5),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .width(312.dp)
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultActionButton(
                text = "홈으로",
                isPrimary = false,
                modifier = Modifier.weight(1f),
                onClick = onHomeClick
            )
            ResultActionButton(
                text = "지금 안정하기",
                isPrimary = true,
                modifier = Modifier.weight(1f),
                onClick = onPauzeStartClick
            )
        }
    }
}

@Composable
private fun ResultActionButton(
    text: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(
                color = if (isPrimary) AppTheme.palette.gray.getColor(1)
                else AppTheme.palette.gray.getColor(7),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = bodyTextLgBold,
            color = if (isPrimary) AppTheme.palette.base.getColor(0)
            else AppTheme.palette.gray.getColor(1)
        )
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=441")
@Composable
private fun PauzeTodayConditionPreview() {
    MainPaletteTheme {
        PauzeTodayCondition()
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

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pauze.MainActivity
import com.example.pauze.R
import com.example.pauze.data.model.CreateTodayConditionRequest
import com.example.pauze.data.model.CreateTodayConditionResult
import com.example.pauze.data.model.GetTodayConditionResponseDto
import com.example.pauze.data.model.SensitivityLevel
import com.example.pauze.data.repository.TodayConditionRepository
import com.example.pauze.ui.component.CondtionAnswer
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.component.PhaseBar
import com.example.pauze.ui.component.SensitivityScoreBar
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingMdBold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    viewModel: PauzeTodayConditionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val conditionState = uiState.data
    val conditionQuestions = conditionState.conditionQuestions
    val submissionError = conditionState.submissionError
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
                    context.startActivity(
                        Intent(context, PauzeStartActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                    )
                }
            }
        }
    }

    if (conditionState.showResult) {
        PauzeTodayConditionResult(
            score = conditionState.sensitivityScore,
            sensitivityLevel = conditionState.sensitivityLevel
                ?: conditionState.sensitivityScore.toSensitivityLevel(),
            onHomeClick = viewModel::navigateToMainActivity,
            onDetailClick = viewModel::navigateToMainActivity,
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

        submissionError?.let { message ->
            Text(
                text = message,
                style = bodyTextMdRegular,
                color = AppTheme.palette.secondary.getColor(3),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(312.dp)
                    .padding(bottom = 12.dp)
            )
        }

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
                text = if (
                    conditionState.isSubmitting &&
                    conditionState.currentQuestionIndex == conditionQuestions.lastIndex
                ) {
                    "저장 중..."
                } else {
                    "다음"
                },
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
    sensitivityLevel: SensitivityLevel,
    onHomeClick: () -> Unit,
    onDetailClick: () -> Unit,
    onPauzeStartClick: () -> Unit
) {
    val normalizedScore = score.coerceIn(0, 100)
    val sensitivityLevelText = sensitivityLevel.label
    val sensitivityLevelColor = when (sensitivityLevel) {
        SensitivityLevel.LOW -> AppTheme.palette.primary.getColor(3)
        SensitivityLevel.NORMAL -> AppTheme.palette.tertiary.getColor(3)
        SensitivityLevel.HIGH -> AppTheme.palette.secondary.getColor(3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.base.getColor(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(168.dp))

        Text(
            text = "측정 완료!",
            style = headingMdBold,
            color = AppTheme.palette.gray.getColor(1)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "오늘의 컨디션 분석을 완료했어요.\n자세한 결과는 홈 화면에서 확인할 수 있어요.",
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(4),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier
                .width(312.dp)
                .background(
                    color = AppTheme.palette.gray.getColor(8),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(28.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "현재 민감 지수",
                    style = bodyTextLgRegular,
                    color = AppTheme.palette.gray.getColor(2)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sensitivityLevelText,
                    style = bodyTextLgBold,
                    color = sensitivityLevelColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
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
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = onDetailClick)
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "자세히 알아보기",
                    style = bodyTextMdBold,
                    color = AppTheme.palette.gray.getColor(2)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_forward),
                    contentDescription = "자세히 알아보기",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
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
    val previewViewModel = remember {
        PauzeTodayConditionViewModel(PreviewTodayConditionRepository)
    }

    MainPaletteTheme {
        PauzeTodayCondition(viewModel = previewViewModel)
    }
}

private object PreviewTodayConditionRepository : TodayConditionRepository {
    override suspend fun createTodayCondition(
        request: CreateTodayConditionRequest
    ): CreateTodayConditionResult = CreateTodayConditionResult(
        conditionId = 1,
        sensitivityScore = 53,
        sensitivityLevel = SensitivityLevel.NORMAL
    )

    override suspend fun getTodayCondition(): GetTodayConditionResponseDto? = null
}

private fun Int.toSensitivityLevel(): SensitivityLevel = when (coerceIn(0, 100)) {
    in 0..39 -> SensitivityLevel.LOW
    in 40..69 -> SensitivityLevel.NORMAL
    else -> SensitivityLevel.HIGH
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

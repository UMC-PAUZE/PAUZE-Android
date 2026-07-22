package com.example.pauze.ui.home

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.R
import com.example.pauze.data.model.Activity
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.Noise
import com.example.pauze.data.model.OverallIndex
import com.example.pauze.data.model.Sleeping
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ConditionBar
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingMdMedium
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun HomeScreen(
    context: Context,
    viewModel: HomeViewModel = viewModel()
){
    val condition by viewModel.dummy.collectAsState()

    val width = context.resources.configuration.screenWidthDp
    val bgPadding = 24
    val conditionBoxPadding = 16
    val conditionBarWidth = width - ((bgPadding + conditionBoxPadding) * 2)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is HomeEffect.MoveToTodayCondition -> {

                }
                is HomeEffect.MoveToBreathingBtn -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = bgPadding.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.palette.gray.getColor(9))
                .padding(top = 40.dp, bottom = 16.dp),
        ){
            Row {
                Image(painter = painterResource(R.drawable.pauze_home), contentDescription = "pauze 로고")
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(R.drawable.ic_alarm), contentDescription = "알람 아이콘")
            }
        }
        Spacer(modifier = Modifier.height(17.dp))
        Text("000님", style = bodyTextLgRegular, color = AppTheme.palette.gray.getColor(2))
        Text(
            if(viewModel.isTodayConditionExists) "오늘은 조용한 곳에서 안정을 \n취하는 게 어떨까요?"
                else "숙면하셨나요?\n오늘의 컨디션을 작성해보세요",
            style = headingMdMedium,
            color = AppTheme.palette.gray.getColor(2))

        if(!viewModel.isTodayConditionExists){
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    "오늘의 컨디션 입력하기",
                    onClick = { viewModel.moveToTodayCondition() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        ConditionBox(condition = condition, boxPadding = conditionBoxPadding, barWidth = conditionBarWidth)
        Spacer(modifier = Modifier.height(24.dp))
        MoveToBreathingBtn(onClick = { viewModel.moveToBreathing() })
    }
}

@Composable
fun ConditionBox(condition: Condition?, boxPadding: Int, barWidth: Int){
    if(condition != null){
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = AppTheme.palette.gray.getColor(7),
                    ),
                    shape = RoundedCornerShape(20.dp),
                )
                .background(
                    shape = RoundedCornerShape(20.dp),
                    color = AppTheme.palette.gray.getColor(8)
                )
                .padding(boxPadding.dp)
        ){
            Column{
                Row{
                    Text("어제 민감 지수", style = bodyTextLgRegular, color = AppTheme.palette.gray.getColor(2))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        condition.index.label,
                        style = bodyTextLgBold,
                        color = when(condition.index){
                            OverallIndex.Low -> AppTheme.palette.primary.getColor(3)        // change later
                            OverallIndex.Moderate -> AppTheme.palette.tertiary.getColor(3)  // change later
                            OverallIndex.High -> AppTheme.palette.secondary.getColor(3)     // change later
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(condition.score.toString(), style = bodyTextXlBold.copy(fontSize = 64.sp), color = AppTheme.palette.gray.getColor(2))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("/ 100", style = bodyTextMdRegular.copy(fontSize = 24.sp), color = AppTheme.palette.gray.getColor(4))
                }
                Spacer(modifier = Modifier.height(12.dp))
                ConditionBar(condition.score, barWidth.dp)
                Spacer(modifier = Modifier.height(16.dp))
                ConditionDetailBox(condition = condition)
            }
        }
    }
}

@Composable
fun ConditionDetailBox(condition: Condition){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ConditionDetailBoxContainer(modifier = Modifier.weight(1f)) {
            Column{
                Text("수면", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(4))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    condition.sleeping.label,
                    style = headingSmBold,
                    color = when(condition.sleeping){
                        Sleeping.Low -> AppTheme.palette.secondary.getColor(3)
                        Sleeping.Moderate -> AppTheme.palette.tertiary.getColor(3)
                        Sleeping.High -> AppTheme.palette.primary.getColor(3)        // change later
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        ConditionDetailBoxContainer(modifier = Modifier.weight(1f)) {
            Column{
                Text("소음", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(4))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    condition.noise.label,
                    style = headingSmBold,
                    color = when(condition.noise){
                        Noise.Low -> AppTheme.palette.primary.getColor(3)        // change later
                        Noise.Moderate -> AppTheme.palette.tertiary.getColor(3)
                        Noise.High -> AppTheme.palette.secondary.getColor(3)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        ConditionDetailBoxContainer(modifier = Modifier.weight(1f)) {
            Column{
                Text("사회 활동", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(4))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    condition.activity.label,
                    style = headingSmBold,
                    color = when(condition.activity){
                        Activity.Low -> AppTheme.palette.primary.getColor(3)        // change later
                        Activity.Moderate -> AppTheme.palette.tertiary.getColor(3)
                        Activity.High -> AppTheme.palette.secondary.getColor(3)
                    }
                )
            }
        }
    }
}

@Composable
fun ConditionDetailBoxContainer(modifier: Modifier, content: @Composable (BoxScope.() -> Unit)){
    Box(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(16.dp),
                color = AppTheme.palette.gray.getColor(8)
            )
            .border(
                width = 1.dp,
                color = AppTheme.palette.gray.getColor(7),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(12.dp),
        content = content
    )
}
@Composable
fun MoveToBreathingBtn(onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.palette.tertiary.getColor(3),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ){
            Image(
                painter = painterResource(R.drawable.ic_logo_yellow),
                contentDescription = "앱 로고",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column{
                Text("즉각 안정 시작하기", style = bodyTextXlBold, color = AppTheme.palette.tertiary.getColor(9))
                Text("1분 호흡으로 바로 안정해요", style = bodyTextSmRegular, color = AppTheme.palette.tertiary.getColor(9))
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = "즉각 안정 화면으로 이동",
                colorFilter = ColorFilter.tint(color = AppTheme.palette.tertiary.getColor(7))
            )
        }
    }
}
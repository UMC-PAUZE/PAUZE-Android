package com.example.pauze.ui.home

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.BottomNavDestination
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.R
import com.example.pauze.data.model.Activity
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.Noise
import com.example.pauze.data.model.OverallIndex
import com.example.pauze.data.model.Sleeping
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.SensitivityScoreBar
import com.example.pauze.ui.component.Destination
import com.example.pauze.ui.component.NavigationButton
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.component.TopBarVariant
import com.example.pauze.ui.pauze.PauzeStartActivity
import com.example.pauze.ui.pauze.PauzeTodayConditionActivity
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingMdMedium
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun HomeScreen(
    context: Context,
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val bgPadding = 24
    val conditionBoxPadding = 16

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is HomeEffect.MoveToTodayCondition -> {
                    context.startActivity(Intent(context, PauzeTodayConditionActivity::class.java))
                }
                is HomeEffect.MoveToBreathingBtn -> {
                    val intent = Intent(context, PauzeStartActivity::class.java)
                    intent.putExtra("Pauze Destination", "PauzeBreathing")
                    context.startActivity(intent)
                }
                is HomeEffect.MoveToReportScreen -> {
                    navController.navigate(BottomNavDestination.Report){
                        popUpTo(BottomNavDestination.Report)
                        launchSingleTop = true
                        restoreState = true
                    }
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
        if(uiState.isLoading){
            CircularProgressIndicator()
        }
        else if(uiState.error != null){
            Text("오류가 발생했습니다\n다시 시도해주세요", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
        }
        else {
            TopBar(variant = TopBarVariant.Home)
            Spacer(modifier = Modifier.height(17.dp))
            Text("000님", style = bodyTextLgRegular, color = AppTheme.palette.gray.getColor(2))
            Text(
                if(uiState.data.isTodayConditionExists) "오늘은 조용한 곳에서 안정을 \n취하는 게 어떨까요?"
                else "숙면하셨나요?\n오늘의 컨디션을 작성해보세요",
                style = headingMdMedium,
                color = AppTheme.palette.gray.getColor(2))
            Spacer(modifier = Modifier.height(16.dp))
            if(!uiState.data.isTodayConditionExists){
                Column {
                    Button(
                        "오늘의 컨디션 입력하기",
                        onClick = { viewModel.moveToTodayCondition() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
            NavigationButton(
                toWhere = Destination.PauzeBreathing,
                onClick = { viewModel.moveToBreathing() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ConditionBox(
                condition = uiState.data.condition,
                boxPadding = conditionBoxPadding,
                navigateToReport = { viewModel.moveToReportScreen() }
            )
        }
    }
}

@Composable
fun ConditionBox(condition: Condition?, boxPadding: Int, navigateToReport: () -> Unit){
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
                SensitivityScoreBar(condition.score)
                Spacer(modifier = Modifier.height(16.dp))
                ConditionDetailBox(condition = condition)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "자세히 알아보기",
                        modifier = Modifier.clickable(onClick = navigateToReport).padding(vertical = 8.dp),
                        style = bodyTextMdBold,
                        color = AppTheme.palette.gray.getColor(2)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        modifier = Modifier.clickable(onClick = navigateToReport),
                        painter = painterResource(R.drawable.ic_forward),
                        contentDescription = "자세히 알아보기",
                        tint = AppTheme.palette.gray.getColor(2)
                    )
                }
            }
        }
    }
}

@Composable
fun ConditionDetailBox(condition: Condition){
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ConditionDetailBoxContainer(item = "과다")
            Spacer(modifier = Modifier.width(8.dp))
            ConditionDetailBoxContainer(item = "4시간 미만")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ConditionDetailBoxContainer(item = "보통")
            Spacer(modifier = Modifier.width(8.dp))
            ConditionDetailBoxContainer(item = "편안")
            Spacer(modifier = Modifier.width(8.dp))
            ConditionDetailBoxContainer(item = "적음")
        }
    }
}

@Composable
fun ConditionDetailBoxContainer(item: String){
    Box(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(100.dp),
                color = AppTheme.palette.gray.getColor(8)
            )
            .border(
                width = 1.dp,
                color = AppTheme.palette.gray.getColor(7),
                shape = RoundedCornerShape(100.dp),
            )
    ){
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_sound),
                contentDescription = "소리",
                tint = AppTheme.palette.secondary.getColor(3)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "과다",
                style = bodyTextMdMedium,
                color = AppTheme.palette.secondary.getColor(3)
            )
        }
    }
}

package com.example.pauze.ui.pauze

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.MainActivity
import com.example.pauze.R
import com.example.pauze.data.model.InstantAction
import com.example.pauze.data.model.RestGuide
import com.example.pauze.ui.component.Destination
import com.example.pauze.ui.component.NavigationButton
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextXlBold

@Composable
fun PauzeOverloadScreen(
    context: Context,
    navController: NavController,
    viewModel: PauzeOverloadViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is PauzeOverloadEffect.BackStack -> {
                    navController.popBackStack()
                }
                is PauzeOverloadEffect.NavigateToFind -> {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("Bottom Navigation Destination", "Find")
                    context.startActivity(intent)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ){
        TopBar(
            "과한 에너지 소모",
            onBackClick = { viewModel.backStack() }
        )
        Spacer(modifier = Modifier.height(24.dp))
        if(uiState.isLoading){
            CircularProgressIndicator()
        }
        else if(uiState.error != null) {
            Text("오류가 발생했습니다\n다시 시도해주세요", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
        }
        else {
            Text("지금 바로 할 수 있어요", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow {
                items(uiState.data.instantActions.size){ index ->
                    InstantActions(action = uiState.data.instantActions[index])
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
            Text("쉼 가이드", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(uiState.data.restGuideList.size) { index ->
                    RestGuide(guide = uiState.data.restGuideList[index])
                }
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    NavigationButton(toWhere = Destination.Find, onClick = { viewModel.navigateToFind() })
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun InstantActions(action: InstantAction){
    Box(
        modifier = Modifier
            .width(148.dp).height(158.dp)
            .padding(end = 16.dp)
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ){
        Column {
            Image(
                painter = painterResource(action.image),
                contentDescription = "이미지"
            )
            Spacer(modifier = Modifier.height(24.dp))
            TimeBox(duration = action.duration)
            Spacer(modifier = Modifier.height(8.dp))
            Text(action.title, style = bodyTextLgBold, color = Color(0xFFFFFFFF))
        }
    }
}

@Composable
fun RestGuide(guide: RestGuide){
    var isExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ){
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier.size(54.dp).background(
                        color = AppTheme.palette.gray.getColor(2),
                        shape = RoundedCornerShape(8.dp)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column{
                    TimeBox(duration = guide.duration)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(guide.title, style = bodyTextLgBold, color = AppTheme.palette.gray.getColor(2))
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.clickable(onClick = {isExpanded = !isExpanded}),
                    painter = if(isExpanded) painterResource(R.drawable.ic_arrow_up)
                        else painterResource(R.drawable.ic_arrow_down),
                    contentDescription = "여닫기 버튼",
                    tint = AppTheme.palette.gray.getColor(2)
                )
            }
            if(isExpanded){
                Spacer(modifier = Modifier.height(12.dp))
                Text(guide.content, style = bodyTextMdRegular, color = AppTheme.palette.gray.getColor(4))
            }
        }
    }
}

@Composable
fun TimeBox(duration: Int){
    Box(
        modifier = Modifier
            .background(
                color = AppTheme.palette.gray.getColor(6),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ){
        Text(
            if(duration == 0) "즉시"
            else if(duration >= 60) "${duration / 60}분"
            else "${duration}초",
            style = bodyTextSmMedium,
            color = AppTheme.palette.gray.getColor(2)
        )
    }
}
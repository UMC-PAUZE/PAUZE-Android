package com.example.pauze.ui.pauze

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.Destination
import com.example.pauze.ui.component.NavigationButton
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextXlBold

@Composable
fun PauzeOverloadScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ){
        TopBar(
            "과한 에너지 소모",
            modifier = Modifier.padding(top = 40.dp, bottom = 16.dp),
            onBackClick = {}
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("지금 바로 할 수 있어요", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow {
            items(5){ index ->
                InstantActions()
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text("쉼 가이드", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(5) { index ->
                RestGuide()
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        NavigationButton(toWhere = Destination.Find, onClick = {})
    }
}

@Composable
fun InstantActions(){
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
                painter = painterResource(R.drawable.ic_headset),
                contentDescription = "헤드셋"
            )
            Spacer(modifier = Modifier.height(24.dp))
            TimeBox()
            Spacer(modifier = Modifier.height(8.dp))
            Text("눈 감고 10초 호흡", style = bodyTextLgBold, color = Color(0xFFFFFFFF))
        }
    }
}

@Composable
fun RestGuide(){
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
                    TimeBox()
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("혼자만의 공간 찾기", style = bodyTextLgBold, color = AppTheme.palette.gray.getColor(2))
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.clickable(onClick = {isExpanded = !isExpanded}),
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = "여닫기 버튼",
                    tint = AppTheme.palette.gray.getColor(2)
                )
            }
            if(isExpanded){
                Spacer(modifier = Modifier.height(12.dp))
                Text("5~10분간 혼자 있을 수 있는 조용한 곳으로 이동하세요. 화장실, 계단실, 빈 회의실도 좋아요.", style = bodyTextMdRegular, color = AppTheme.palette.gray.getColor(4))
            }
        }
    }
}

@Composable
fun TimeBox(){
    Box(
        modifier = Modifier
            .background(
                color = AppTheme.palette.gray.getColor(6),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ){
        Text("10초", style = bodyTextSmMedium, color = AppTheme.palette.gray.getColor(2))
    }
}
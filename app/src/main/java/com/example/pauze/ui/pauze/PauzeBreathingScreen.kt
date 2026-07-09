package com.example.pauze.ui.pauze

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pauze.R
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.*
@Composable
fun PauzeBreathingScreen() {

    var selectedTabIndex by remember { mutableStateOf(0) } //처음 478 호흡으로 선택

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
    ) {
        TopBar("즉각 안정")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp)
        ){
            Tab(
                text = "478 호흡",
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                modifier = Modifier.weight(1f)
            )
            Tab(
                text = "박스 호흡",
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                modifier = Modifier.weight(1f)
            )
            Tab(
                text = "간단 호흡",
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "0/3", //변수 처리
                color = AppTheme.palette.gray.getColor(4),
                style = bodyTextXlMedium
            )
            Icon(
                painter = painterResource(R.drawable.ic_replay),
                contentDescription = "다시 시작",
                modifier = Modifier.size(28.dp),
                tint = AppTheme.palette.gray.getColor(4)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .size(312.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(8),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(218.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(7),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(6),
                        shape = CircleShape
                    )
            )

            Text(
                text = "1",
                color = AppTheme.palette.gray.getColor(2),
                fontSize = 64.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fontFamily
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Text(
                text = "들이쉬세요",
                style = headingLgBold,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(text = "들숨", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
                    Text(text = "4초", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(4))
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(text = "참기", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(6))
                    Text(text = "7초", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(7))
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(text = "날숨", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(6))
                    Text(text = "8초", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(7))
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun PauzeBreathingPreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
        ) {
            PauzeBreathingScreen()
        }
    }
}
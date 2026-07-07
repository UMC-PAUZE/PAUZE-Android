package com.example.pauze.ui.pauze

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import com.example.pauze.ui.theme.headingMdBold

@Composable
fun PauzeStartScreen() {
    Column {
        TopBar("Pauze")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "지금 바로 안정해요",
                style = headingMdBold,
                color = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "현재 상황을 선택하면 맞춤 안정 콘텐츠를 시작해요.",
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.palette.primary.getColor(5),
                        shape = RoundedCornerShape(size = 20.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_logo),
                            contentDescription = "로고",
                            modifier = Modifier.size(48.dp)
                        )
                        Column {
                            Text(
                                text = "즉각 안정 시작하기",
                                style = bodyTextXlBold,
                                color = AppTheme.palette.primary.getColor(9)
                            )
                            Text(
                                text = "1분 호흡으로 바로 안정해요.",
                                style = bodyTextSmRegular,
                                color = AppTheme.palette.primary.getColor(7)
                            )
                        }
                    }
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_forward),
                        contentDescription = "이동하기 > 화살표",
                        tint = AppTheme.palette.primary.getColor(7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "어떤 안정이 필요한가요?",
                style = bodyTextXlBold,
                color = AppTheme.palette.gray.getColor(2)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                SelectionCard(iconRes = R.drawable.ic_headset, title = "청각", description = "자연소리와 ASMR로 \n청각 자극을 낮춰요")
                SelectionCard(iconRes = R.drawable.ic_headset, title = "시각", description = "화면을 어둡게 하고 명상 또는 호흡에 집중해요")
                SelectionCard(iconRes = R.drawable.ic_headset, title = "과한 에너지 소모", description = "쉼 가이드를 따르거나 HSP \n큐레이션 게시판으로 이동해요.")
            }
        }

    }
}

@Composable
fun SelectionCard(
    iconRes: Int,
    title: String,
    description: String,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.palette.gray.getColor(8), // 0xFF2D2E28로 변경해야 됨
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = "헤드셋 로고",
                    modifier = Modifier.size(48.dp),
                    tint = AppTheme.palette.gray.getColor(2)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = bodyTextXlBold,
                        color = AppTheme.palette.gray.getColor(2)
                    )
                    Text(
                        text = description,
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(5),
                        modifier = Modifier.width(152.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = "이동하기 > 화살표",
                modifier = Modifier.size(24.dp),
                tint = AppTheme.palette.gray.getColor(5)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PauzeStartPreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
        ){
            PauzeStartScreen()
        }

    }
}
package com.example.pauze.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold

enum class Destination { PauzeBreathing, Find }

@Composable
fun NavigationButton(toWhere: Destination, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if(toWhere == Destination.PauzeBreathing) AppTheme.palette.tertiary.getColor(3)
                            else AppTheme.palette.primary.getColor(5),
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
                painter =
                    if(toWhere == Destination.PauzeBreathing) painterResource(R.drawable.ic_logo_yellow)
                            else painterResource(R.drawable.ic_find),
                contentDescription = if(toWhere == Destination.PauzeBreathing) "즉각 안정" else "발견",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column{
                Text(
                    if(toWhere == Destination.PauzeBreathing) "즉각 안정 시작하기"
                            else "HSP 이해하러 가기",
                    style = bodyTextXlBold,
                    color = if(toWhere == Destination.PauzeBreathing) AppTheme.palette.tertiary.getColor(9)
                        else AppTheme.palette.primary.getColor(9)
                )
                Text(
                    if(toWhere == Destination.PauzeBreathing) "1분 호흡으로 바로 안정해요"
                    else "발견 탭으로 이동하여 나를 이해하는\n시간을 가져요.",
                    style = bodyTextSmRegular,
                    color = if(toWhere == Destination.PauzeBreathing) AppTheme.palette.tertiary.getColor(9)
                        else AppTheme.palette.primary.getColor(9)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = "해당 화면으로 이동",
                colorFilter = if(toWhere == Destination.PauzeBreathing)
                    ColorFilter.tint(color = AppTheme.palette.tertiary.getColor(7))
                    else null
            )
        }
    }
}
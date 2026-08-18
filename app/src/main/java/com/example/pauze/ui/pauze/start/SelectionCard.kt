package com.example.pauze.ui.pauze.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold

@Composable
fun SelectionCard(
    iconRes: Int,
    title: String,
    titleColor: Color = AppTheme.palette.gray.getColor(2),
    description: String,
    onClick: () -> Unit = {},
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(16.dp)
            .clickable(onClick = onClick)
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
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = "$title 아이콘",
                    modifier = Modifier.size(48.dp),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = bodyTextXlBold,
                        color = titleColor
                    )
                    Text(
                        text = description,
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(4),
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
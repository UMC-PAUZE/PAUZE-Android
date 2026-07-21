package com.example.pauze.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme

@Composable
fun PhaseBar(
    isWaiting: Boolean,
    modifier: Modifier
){
    HorizontalDivider(
        thickness = 4.dp,
        color = if(isWaiting) AppTheme.palette.gray.getColor(8)
            else AppTheme.palette.gray.getColor(2),
        modifier = modifier.shadow(
            elevation = 2.dp,
            shape = RoundedCornerShape(91.7.dp),
            clip = true
        )
    )
}
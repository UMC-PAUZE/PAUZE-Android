package com.example.pauze.ui.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.headingSmBold

@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.palette.gray.getColor(2),
){
    Column(
        modifier = modifier
            .height(78.dp)
            .background(color = AppTheme.palette.gray.getColor(8), shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = AppTheme.palette.gray.getColor(7), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(text = label, style = bodyTextSmMedium, color = AppTheme.palette.gray.getColor(4))
        Text(text = value, style = headingSmBold, color = valueColor)
    }
}

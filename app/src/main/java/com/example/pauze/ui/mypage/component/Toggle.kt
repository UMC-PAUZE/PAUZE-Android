package com.example.pauze.ui.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme

@Composable
fun Toggle(
    selected: Boolean,
    onToggle: () -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val trackColor = when {
        selected && enabled -> AppTheme.palette.primary.getColor(5)
        !enabled -> AppTheme.palette.gray.getColor(8)
        else -> AppTheme.palette.gray.getColor(7)
    }
    val thumbColor = if (enabled) AppTheme.palette.gray.getColor(2) else AppTheme.palette.gray.getColor(7)

    Box(
        modifier = modifier
            .size(48.dp)
            .clickable(enabled = enabled, onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(19.dp)
                .background(color = trackColor, shape = CircleShape)
                .padding(2.dp),
            contentAlignment = if (selected) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .background(color = thumbColor, shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun TogglePreview(){
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("선택+활성", color = AppTheme.palette.gray.getColor(4))
                Toggle(selected = true, enabled = true)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("비선택+활성", color = AppTheme.palette.gray.getColor(4))
                Toggle(selected = false, enabled = true)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("비선택+비활성", color = AppTheme.palette.gray.getColor(4))
                Toggle(selected = false, enabled = false)
            }
        }
    }
}
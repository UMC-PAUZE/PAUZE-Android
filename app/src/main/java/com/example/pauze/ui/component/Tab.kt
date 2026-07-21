package com.example.pauze.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold

@Composable
fun Tab(
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    leadingIconResId: Int? = null,
){
    Box(
        modifier = modifier
            .background(
                color = if(selected) AppTheme.palette.gray.getColor(7) else Color.Transparent,
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        val contentColor = when {
            selected -> AppTheme.palette.gray.getColor(2)
            !enabled -> AppTheme.palette.gray.getColor(7)
            else -> AppTheme.palette.gray.getColor(4)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIconResId?.let { iconResId ->
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
            }
            Text(
                text = text,
                style = bodyTextLgBold,
                color = contentColor
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TabPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.palette.gray.getColor(9))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Tab(text = "선택됨", selected = true, modifier = Modifier.weight(1f))
                Tab(text = "선택안됨\n활성", selected = false, modifier = Modifier.weight(1f))
                Tab(text = "선택안됨\n비활성", selected = false, enabled = false, modifier = Modifier.weight(1f))
            }
        }
    }
}

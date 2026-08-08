package com.example.pauze.ui.mypage.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgMedium

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
){
    Column {
        Text(
            text = title,
            style = bodyTextLgMedium,
            color = AppTheme.palette.gray.getColor(4),
            modifier = Modifier.padding(vertical = 12.dp)
        )
        content()
    }
}

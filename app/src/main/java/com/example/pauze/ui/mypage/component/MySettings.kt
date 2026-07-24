package com.example.pauze.ui.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextSmRegular

enum class MySettingsVariant { Button, Toggle }

@Composable
fun MySettings(
    title: String,
    modifier: Modifier = Modifier,
    variant: MySettingsVariant = MySettingsVariant.Button,
    caption: String? = null,
    icon: Painter? = null,
    toggleSelected: Boolean = false,
    onClick: () -> Unit = {},
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column {
                Text(text = title, style = bodyTextLgMedium, color = AppTheme.palette.gray.getColor(2))
                if (caption != null) {
                    Text(text = caption, style = bodyTextSmRegular, color = AppTheme.palette.gray.getColor(4))
                }
            }
        }
        if (variant == MySettingsVariant.Toggle) {
            Toggle(selected = toggleSelected, onToggle = onClick)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 400)
@Composable
private fun MySettingsPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.palette.gray.getColor(9))
                .padding(24.dp)
        ) {
            MySettings(
                title = "제목",
                caption = "캡션",
                icon = painterResource(R.drawable.ic_headset),
                variant = MySettingsVariant.Button
            )
            MySettings(
                title = "제목",
                caption = "캡션",
                icon = painterResource(R.drawable.ic_headset),
                variant = MySettingsVariant.Toggle,
                toggleSelected = true
            )
        }
    }
}
package com.example.pauze.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold

enum class TopBarVariant { Default, Home }
@Composable
fun TopBar(
    title: String = "",
    modifier: Modifier = Modifier,
    variant: TopBarVariant = TopBarVariant.Default,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    rightIcon: (@Composable () -> Unit)? = null,
    backgroundColor: Color = AppTheme.palette.gray.getColor(9),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(56.dp)
        ) {
            when(variant){
                TopBarVariant.Home -> {
                    Image(
                        painter = painterResource(R.drawable.pauze_login),
                        contentDescription = "Pauze 로고 Home",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 24.dp)
                            .height(24.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_alarm),
                        contentDescription = "알림",
                        tint = AppTheme.palette.gray.getColor(2),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp)
                            .size(24.dp)
                    )
                }
                TopBarVariant.Default -> {
                    if (showBackButton) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "뒤로가기",
                            tint = AppTheme.palette.gray.getColor(2),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 17.dp)
                                .size(24.dp)
                                .clickable(onClick = onBackClick)
                        )
                    }

                    Text(
                        text = title,
                        style = bodyTextLgBold,
                        color = AppTheme.palette.gray.getColor(2),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 48.dp)
                    )

                    if (rightIcon != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 24.dp)
                                .size(24.dp)
                        ) { rightIcon() }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.palette.gray.getColor(9)),
        ) {
            TopBar(
                title = "Text",
                rightIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_box),
                        contentDescription = "보관함",
                        tint = AppTheme.palette.gray.getColor(2)
                    )
                }
            )

            TopBar(
                variant = TopBarVariant.Home
            )
        }
    }
}

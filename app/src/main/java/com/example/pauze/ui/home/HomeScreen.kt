package com.example.pauze.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.R

@Composable
fun HomeScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(0))
            .padding(horizontal = 24.dp)
    ) {
        Image(painter = painterResource(R.drawable.pauze_logo), contentDescription = "포즈 로고")
    }
}
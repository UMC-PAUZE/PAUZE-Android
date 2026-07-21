package com.example.pauze.ui.login

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.headingMdBold

@Composable
fun SignUpCompletedScreen(
    context: Context,
    viewModel: SignUpCompletedViewModel = viewModel()
){
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                SignUpCompletedEffect.NavigateToHome -> {
                    val intent = Intent(context, Class.forName("com.example.pauze.ui.home.HomeActivity"))
                    startActivity(context, intent, null)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(127.dp, 127.dp),
            painter = painterResource(R.drawable.ic_headset),
            contentDescription = "headset"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "가입이 완료되었습니다!",
            style = headingMdBold,
            color = AppTheme.palette.gray.getColor(2),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "${viewModel.name}님이 일상에서 안정을 찾아가는\n여정을 시작해볼게요",
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(2),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            "시작하기",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 48.dp),
            onClick = { viewModel.navigateToHome() },
            enabled = true
        )
    }
}
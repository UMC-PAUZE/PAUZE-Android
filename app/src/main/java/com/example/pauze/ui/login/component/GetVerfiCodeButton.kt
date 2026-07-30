package com.example.pauze.ui.login.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.pauze.ui.BaseViewModel
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.login.KakaoSignUpViewModel
import com.example.pauze.ui.login.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme

@Composable
fun GetVerifCodeButton(
    viewModel: ViewModel,
    kakaoSignUp: Boolean
){
    Button(
        "인증코드 받기",
        onClick = {
            if(kakaoSignUp){
                (viewModel as KakaoSignUpViewModel).updatePhase()
            } else {
                (viewModel as SignUpViewModel).updatePhase()
            } },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        color = AppTheme.palette.gray.getColor(7),
        contentColor = AppTheme.palette.gray.getColor(2)
    )
}
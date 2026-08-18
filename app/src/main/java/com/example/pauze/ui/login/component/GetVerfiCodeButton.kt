package com.example.pauze.ui.login.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.login.signup.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme

// 인증코드 요청 버튼
@Composable
fun GetVerifCodeButton(
    viewModel: SignUpViewModel
){
    Button(
        "인증코드 받기",
        onClick = {
            // 로컬 회원가입일 때만 요청
            if(!viewModel.isKakaoAccountExists){
                viewModel.sendCodeForSignUp()
                viewModel.updatePhase()
            }
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        color = AppTheme.palette.gray.getColor(7),
        contentColor = AppTheme.palette.gray.getColor(2)
    )
}
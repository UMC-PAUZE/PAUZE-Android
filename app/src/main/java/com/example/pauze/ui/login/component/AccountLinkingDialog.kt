package com.example.pauze.ui.login.component

import androidx.compose.runtime.Composable
import com.example.pauze.ui.component.Dialog

// 연동(Local <-> Kakao) 여부 묻는 다이얼로그
@Composable
fun AccountLinkingDialog(onDismissRequest: () -> Unit, onContinue: () -> Unit){
    Dialog(
        title = "이미 가입된 계정이 있어요",
        content = "기존 계정과 카카오 계정을 연결하시겠어요?",
        btnCancel = "닫기",
        btnContinue = "연동하기",
        onDismissRequest = onDismissRequest,
        onContinue = onContinue
    )
}
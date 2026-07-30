package com.example.pauze.ui.login.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.login.KakaoSignUpViewModel
import com.example.pauze.ui.login.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun NameAndNicknameField(
    viewModel: ViewModel,
    mode: TextFieldMode,
    kakaoSignUp: Boolean
){
    val isUserNameMode = mode == TextFieldMode.UserName

    ModeBasedTextField(
        mode = mode,
        value = if (kakaoSignUp && isUserNameMode) (viewModel as KakaoSignUpViewModel).name
                else if (kakaoSignUp) (viewModel as KakaoSignUpViewModel).nickname
                else if (isUserNameMode) (viewModel as SignUpViewModel).name
                else (viewModel as SignUpViewModel).nickname,
        onValueChanged = {
            if (kakaoSignUp && isUserNameMode) (viewModel as KakaoSignUpViewModel).name = it
            else if (kakaoSignUp) (viewModel as KakaoSignUpViewModel).nickname = it
            else if (isUserNameMode) (viewModel as SignUpViewModel).name = it
            else (viewModel as SignUpViewModel).nickname = it },
        imeAction = if(isUserNameMode) ImeAction.Next else ImeAction.Done
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        if(isUserNameMode) "2자 이상 입력해주세요"
        else "10자 이내로 입력해주세요",
        style = bodyTextSmRegular,
        color = if (kakaoSignUp && isUserNameMode && (viewModel as KakaoSignUpViewModel).name.length == 1
            || !kakaoSignUp && isUserNameMode && (viewModel as SignUpViewModel).name.length == 1
            || kakaoSignUp && !isUserNameMode && (viewModel as KakaoSignUpViewModel).nickname.length > 10
            || !kakaoSignUp && !isUserNameMode && (viewModel as SignUpViewModel).nickname.length > 10)
            AppTheme.palette.secondary.getColor(4)
        else AppTheme.palette.gray.getColor(5)
    )
}
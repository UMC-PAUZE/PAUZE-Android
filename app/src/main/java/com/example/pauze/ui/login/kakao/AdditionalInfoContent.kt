package com.example.pauze.ui.login.kakao

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.login.component.GetVerifCodeButton
import com.example.pauze.ui.login.component.NameAndNicknameField
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun AdditionalInfoContent(
    viewModel: KakaoSignUpViewModel
){
    var isEmailFocused by remember { mutableStateOf(false) }

    Column {
        NameAndNicknameField(viewModel, TextFieldMode.UserName, true)
        Spacer(modifier = Modifier.height(12.dp))
        NameAndNicknameField(viewModel, TextFieldMode.Nickname, true)
        Spacer(modifier = Modifier.height(12.dp))
        isEmailFocused = ModeBasedTextField(
            mode = TextFieldMode.SetEmail,
            value = viewModel.email,
            onValueChanged = { viewModel.email = it },
            imeAction = ImeAction.Done,
            onCheckClick = { viewModel.toggleEmailExist() },
            checkClickValue = { viewModel.checkEmailAlreadyExistOrNot() }
        )
        if(viewModel.email != "" && !isEmailFocused){
            Text(
                if(viewModel.isEmailNoExisted) "사용할 수 있는 이메일입니다"
                else "이미 사용된 이메일입니다" ,
                style = bodyTextSmRegular,
                color = if(viewModel.isEmailNoExisted) AppTheme.palette.primary.getColor(4)
                else AppTheme.palette.secondary.getColor(4)
            )
            if(viewModel.isEmailNoExisted){
                Spacer(modifier = Modifier.height(12.dp))
                GetVerifCodeButton(viewModel, true)
            }
        }
    }
}

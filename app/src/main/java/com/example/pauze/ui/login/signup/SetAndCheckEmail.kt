package com.example.pauze.ui.login.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun SetAndCheckEmail(
    viewModel: SignUpViewModel
): Boolean {
    var isFocused by remember { mutableStateOf(false) }

    Column {
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.SetEmail,
            value = viewModel.email,
            onValueChanged = { viewModel.email = it },
            imeAction = ImeAction.Done,
            onCheckClick = { viewModel.toggleEmailExist() },
            checkClickValue = { viewModel.checkEmailAlreadyExistOrNot() }
        )
        if(viewModel.email != "" && !isFocused){
            Text(
                if(viewModel.isEmailNoExisted) "사용할 수 있는 이메일입니다"
                else "이미 사용된 이메일입니다" ,
                style = bodyTextSmRegular,
                color = if(viewModel.isEmailNoExisted) AppTheme.palette.primary.getColor(4)
                    else AppTheme.palette.secondary.getColor(4)
            )
        }
    }
    return viewModel.email != "" && !isFocused && viewModel.checkEmailAlreadyExistOrNot()
}
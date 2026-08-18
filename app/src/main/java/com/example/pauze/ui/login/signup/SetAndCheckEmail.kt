package com.example.pauze.ui.login.signup

import android.content.Context
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
import com.example.pauze.ui.login.component.AccountLinkingDialog
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun SetAndCheckEmail(
    context: Context,
    viewModel: SignUpViewModel
): Boolean {
    var isFocused by remember { mutableStateOf(false) }

    Column {
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.SetEmail,
            value = viewModel.email,
            onValueChanged = { viewModel.email = it },
            imeAction = ImeAction.Done,
            onCheckClick = { viewModel.checkEmailAvailable() },
            checkClickValue = (viewModel.isEmailExists == true && viewModel.emailAvailableStatus != "KAKAO")
        )

        if(viewModel.email.isEmpty()){
            viewModel.isEmailExists = null
        }

        if(viewModel.isEmailExists != null && viewModel.emailAvailableStatus != "KAKAO" && viewModel.email.isNotEmpty() && !isFocused){
            Text(
                if(viewModel.isEmailExists == true) "이미 사용된 이메일입니다"
                else "사용할 수 있는 이메일입니다",
                style = bodyTextSmRegular,
                color = if(viewModel.isEmailExists == true) AppTheme.palette.secondary.getColor(4)
                    else AppTheme.palette.primary.getColor(4)
            )
        }

        if(viewModel.showLinkDialog) {
            AccountLinkingDialog(
                onDismissRequest =  { viewModel.showLinkDialog = false },
                onContinue = {
                    viewModel.kakaoLogin(context)
                }
            )
        }
    }
    return viewModel.email != "" && !isFocused && (viewModel.isEmailExists == false)
}
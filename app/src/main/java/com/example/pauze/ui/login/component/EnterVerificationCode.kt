package com.example.pauze.ui.login.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.login.kakao.KakaoSignUpViewModel
import com.example.pauze.ui.login.linking.LinkingViewModel
import com.example.pauze.ui.login.signup.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun EnterVerificationCode(
    viewModel: ViewModel,
    isLinking: Boolean
): Boolean{
    var isFocused by remember { mutableStateOf(false) }

    Column {
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.Verif,
            value = if(isLinking) (viewModel as LinkingViewModel).code else (viewModel as SignUpViewModel).code,
            onValueChanged = { if(isLinking) (viewModel as LinkingViewModel).code = it else (viewModel as SignUpViewModel).code = it },
            imeAction = ImeAction.Done,
            onCheckClick = { if(isLinking) (viewModel as LinkingViewModel).verifyEmail() else (viewModel as SignUpViewModel).verifyEmail() },
            checkClickValue = if(isLinking) (viewModel as LinkingViewModel).isVerified else (viewModel as SignUpViewModel).isVerified
        )

        if(isLinking && (viewModel as LinkingViewModel).code.isEmpty()){
            viewModel.isVerified = null
        } else if (!isLinking && (viewModel as SignUpViewModel).code.isEmpty()){
            viewModel.isVerified = null
        }

        if(!isFocused
            && (isLinking && (viewModel as LinkingViewModel).code != "" && viewModel.isVerified != null)
            || (!isLinking && (viewModel as SignUpViewModel).code != "" && viewModel.isVerified != null)){
            Text(
                if((isLinking && (viewModel as LinkingViewModel).isVerified == true)
                    || (!isLinking && (viewModel as SignUpViewModel).isVerified == true)) "인증에 성공했습니다"
                    else "인증에 실패했습니다",
                style = bodyTextSmRegular,
                color = if((isLinking && (viewModel as LinkingViewModel).isVerified == true)
                    || (!isLinking && (viewModel as SignUpViewModel).isVerified == true))
                    AppTheme.palette.primary.getColor(4)
                    else AppTheme.palette.secondary.getColor(4)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Text(
                if(isLinking) (viewModel as LinkingViewModel).time else (viewModel as SignUpViewModel).time,
                style = bodyTextMdRegular,
                color = AppTheme.palette.primary.getColor(5)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "코드 재전송",
                modifier = Modifier
                    .clickable(onClick = {
                        if(isLinking) {
                            (viewModel as LinkingViewModel).sendEffectForTimer()
                            //viewModel.sendCodeForSignUp()
                        }
                        else {
                            (viewModel as SignUpViewModel).sendEffectForTimer()
                            viewModel.sendCodeForSignUp()
                        }
                    })
                    .padding(vertical = 8.dp),
                style = bodyTextMdBold,
                color = AppTheme.palette.primary.getColor(2)
            )
        }
    }
    return !isFocused && (isLinking && (viewModel as LinkingViewModel).isVerified == true) || (!isLinking && (viewModel as SignUpViewModel).isVerified == true)
}
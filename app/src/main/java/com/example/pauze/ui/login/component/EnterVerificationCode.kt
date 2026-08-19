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
import com.example.pauze.ui.login.linking.LinkingViewModel
import com.example.pauze.ui.login.signup.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular

// 인증코드 입력 필드
@Composable
fun EnterVerificationCode(
    viewModel: ViewModel,
    isLinkingScreen: Boolean
): Boolean{
    var isFocused by remember { mutableStateOf(false) }

    Column {
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.Verif,
            value = if(isLinkingScreen) (viewModel as LinkingViewModel).code else (viewModel as SignUpViewModel).code,
            onValueChanged = {
                if(isLinkingScreen){
                    val linkingViewModel = viewModel as LinkingViewModel
                    linkingViewModel.updateCode(it)
                    linkingViewModel.updateIsVerified(null)
                }
                else{
                    val signUpViewModel = viewModel as SignUpViewModel
                    signUpViewModel.updateCode(it)
                    signUpViewModel.updateIsVerified(null)
                }
            },
            imeAction = ImeAction.Done,
            onCheckClick = {
                if(isLinkingScreen) (viewModel as LinkingViewModel).verifyEmail()
                else (viewModel as SignUpViewModel).verifyEmail()
            },
            checkClickValue = if(isLinkingScreen) (viewModel as LinkingViewModel).isVerified
                else (viewModel as SignUpViewModel).isVerified
        )

        // 인증코드 일치 결과 반영
        if(!isFocused
            && (isLinkingScreen && (viewModel as LinkingViewModel).code != "" && viewModel.isVerified != null)
            || (!isLinkingScreen && (viewModel as SignUpViewModel).code != "" && viewModel.isVerified != null)){
            Text(
                if((isLinkingScreen && (viewModel as LinkingViewModel).isVerified == true)
                    || (!isLinkingScreen && (viewModel as SignUpViewModel).isVerified == true)) "인증에 성공했습니다"
                    else "인증에 실패했습니다",
                style = bodyTextSmRegular,
                color = if((isLinkingScreen && (viewModel as LinkingViewModel).isVerified == true)
                    || (!isLinkingScreen && (viewModel as SignUpViewModel).isVerified == true))
                    AppTheme.palette.primary.getColor(4)
                    else AppTheme.palette.secondary.getColor(4)
            )
        }
        // 코드 재전송
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Text(
                if(isLinkingScreen) (viewModel as LinkingViewModel).time else (viewModel as SignUpViewModel).time,
                style = bodyTextMdRegular,
                color = AppTheme.palette.primary.getColor(5)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "코드 재전송",
                modifier = Modifier
                    .clickable(onClick = {
                        if(isLinkingScreen) {
                            (viewModel as LinkingViewModel).sendEffectForTimer()
                            viewModel.sendCodeForLinking()
                        }
                        else {
                            (viewModel as SignUpViewModel).sendEffectForTimer()
                            // Kakao -> Local 연동
                            if(viewModel.isKakaoAccountExists){
                                viewModel.confirmKakaoAccount()
                            // 로컬 회원가입
                            } else {
                                viewModel.sendCodeForSignUp()
                            }
                        }
                    })
                    .padding(vertical = 8.dp),
                style = bodyTextMdBold,
                color = AppTheme.palette.primary.getColor(2)
            )
        }
    }
    return !isFocused && (isLinkingScreen && (viewModel as LinkingViewModel).isVerified == true) || (!isLinkingScreen && (viewModel as SignUpViewModel).isVerified == true)
}
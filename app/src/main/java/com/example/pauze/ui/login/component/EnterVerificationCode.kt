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
import com.example.pauze.ui.login.signup.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun EnterVerificationCode(
    viewModel: ViewModel,
    kakaoSignUp: Boolean
): Boolean{
    var isFocused by remember { mutableStateOf(false) }

    Column {
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.Verif,
            value = if(kakaoSignUp) (viewModel as KakaoSignUpViewModel).verifCode else (viewModel as SignUpViewModel).verifCode,
            onValueChanged = {
                if(kakaoSignUp){
                    (viewModel as KakaoSignUpViewModel).verifCode = it
                } else {
                    (viewModel as SignUpViewModel).verifCode = it
                } },
            imeAction = ImeAction.Done,
            onCheckClick = {
                if(kakaoSignUp)
                    (viewModel as KakaoSignUpViewModel).checkVerifCodeRight()
                else
                    (viewModel as SignUpViewModel).checkVerifCodeRight() },
            checkClickValue = {
                if(kakaoSignUp)
                    (viewModel as KakaoSignUpViewModel).checkVerifCodeRight()
                else
                    (viewModel as SignUpViewModel).checkVerifCodeRight() }
        )
        if(!isFocused && ((kakaoSignUp && (viewModel as KakaoSignUpViewModel).verifCode != "") || (!kakaoSignUp && (viewModel as SignUpViewModel).verifCode != ""))){
            Text(
                if((kakaoSignUp && (viewModel as KakaoSignUpViewModel).checkVerifCodeRight())
                        || (!kakaoSignUp && (viewModel as SignUpViewModel).checkVerifCodeRight())) "인증에 성공했습니다"
                    else "인증에 실패했습니다",
                style = bodyTextSmRegular,
                color = if((kakaoSignUp && (viewModel as KakaoSignUpViewModel).checkVerifCodeRight())
                            || (!kakaoSignUp && (viewModel as SignUpViewModel).checkVerifCodeRight())) AppTheme.palette.primary.getColor(4)
                    else AppTheme.palette.secondary.getColor(4)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Text(
                if(kakaoSignUp) (viewModel as KakaoSignUpViewModel).time else (viewModel as SignUpViewModel).time,
                style = bodyTextMdRegular,
                color = AppTheme.palette.primary.getColor(5)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "코드 재전송",
                modifier = Modifier
                    .clickable(onClick = {
                        if(kakaoSignUp)
                            (viewModel as KakaoSignUpViewModel).sendEffectForTimer()
                        else
                            (viewModel as SignUpViewModel).sendEffectForTimer()
                    })
                    .padding(vertical = 8.dp),
                style = bodyTextMdBold,
                color = AppTheme.palette.primary.getColor(2)
            )
        }
    }
    return !isFocused && ((kakaoSignUp && (viewModel as KakaoSignUpViewModel).checkVerifCodeRight()) || (!kakaoSignUp && (viewModel as SignUpViewModel).checkVerifCodeRight()))
}
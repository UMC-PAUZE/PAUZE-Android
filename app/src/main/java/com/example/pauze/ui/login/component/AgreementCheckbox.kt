package com.example.pauze.ui.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.pauze.R
import com.example.pauze.ui.login.kakao.KakaoSignUpViewModel
import com.example.pauze.ui.login.signup.SignUpViewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun AgreementCheckbox(
    viewModel: ViewModel,
    isAgreed : Boolean,
    focusManager: FocusManager,
    kakaoSignup: Boolean
){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier.clickable{
                if(kakaoSignup) (viewModel as KakaoSignUpViewModel).updateIsAgreed(!isAgreed)
                else (viewModel as SignUpViewModel).updateIsAgreed(!isAgreed)
                focusManager.clearFocus()
            },
            painter = painterResource(if(isAgreed) R.drawable.ic_checkbox_checked
            else R.drawable.ic_checkbox_unchecked
            ),
            contentDescription = "약관 동의 체크박스"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "개인정보 처리방침에 동의합니다 (필수)",
                style = bodyTextMdMedium,
                color = AppTheme.palette.gray.getColor(2)
            )
            Text(
                "수집한 정보는 서비스 제공 및 예민함 분석에만 사용됩니다.",
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(4)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            modifier = Modifier.clickable{
                if(kakaoSignup) (viewModel as KakaoSignUpViewModel).checkPolicy()
                else (viewModel as SignUpViewModel).checkPolicy()
            },
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = "Navigate to privacy policy screen",
            colorFilter = ColorFilter.tint(AppTheme.palette.gray.getColor(5))
        )
    }
}
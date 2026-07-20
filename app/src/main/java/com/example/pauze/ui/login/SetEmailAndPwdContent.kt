package com.example.pauze.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.login.component.ModeBasedTextField
import com.example.pauze.ui.login.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun SetEmailAndPwdContent(): Boolean{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Column {
        ModeBasedTextField(
            mode = TextFieldMode.SetEmail,
            value = email,
            onValueChanged = { email = it },
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(12.dp))
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.SetPwd,
            value = password,
            onValueChanged = { password = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if(!isFocused && password.length > 1 && password.length < 8) "비밀번호는 8자리 이상이어야 해요"
                else "영문,숫자,특수문자 포함 8자 이상 입력해주세요",
            style = bodyTextSmRegular,
            color = if(!isFocused && password.length > 1 && password.length < 8) AppTheme.palette.secondary.getColor(5)
                else AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.clickable{
                    isChecked = !isChecked
                },
                painter = painterResource(if(isChecked) R.drawable.ic_checkbox_checked
                    else R.drawable.ic_checkbox_unchecked
                ),
                contentDescription = "checkbox for privacy policy"
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
                    // 네비게이션 로직 작성
                },
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = "Navigate to privacy policy screen",
                colorFilter = ColorFilter.tint(AppTheme.palette.gray.getColor(5))
            )
        }
    }

    return email != "" && password.length > 7 && isChecked
}
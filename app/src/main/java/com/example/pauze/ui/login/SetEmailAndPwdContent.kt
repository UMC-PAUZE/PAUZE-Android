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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.R
import com.example.pauze.ui.login.component.ModeBasedTextField
import com.example.pauze.ui.login.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextSmRegular
import org.intellij.lang.annotations.RegExp
import java.sql.DriverManager.println

@Composable
fun SetEmailAndPwdContent(
    viewModel: SignUpViewModel
): Boolean{

    val isAgreed = viewModel.isAgreed
    var isFocused by remember { mutableStateOf(false) }
    val pwdCheck = java.util.regex.Pattern.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).+$", viewModel.password)

    Column {
        ModeBasedTextField(
            mode = TextFieldMode.SetEmail,
            value = viewModel.email,
            onValueChanged = { viewModel.email = it },
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(12.dp))
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.SetPwd,
            value = viewModel.password,
            onValueChanged = { viewModel.password = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if(viewModel.password == "") "영문,숫자,특수문자 포함 8자 이상 입력해주세요"
            else if(!isFocused && viewModel.password.length < 8) "비밀번호는 8자리 이상이어야 해요"
            else if(!isFocused && !pwdCheck) "영문, 숫자, 특수문자를 최소 한 글자 이상 포함해주세요"
            else "사용 가능한 비밀번호입니다",
            style = bodyTextSmRegular,
            color = if(viewModel.password == "") AppTheme.palette.gray.getColor(5)
                else if(!isFocused && (viewModel.password.length > 1 && viewModel.password.length < 8 || !pwdCheck) )
                AppTheme.palette.secondary.getColor(4)
                else AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.clickable{
                    viewModel.updateIsAgreed(!isAgreed)
                },
                painter = painterResource(if(isAgreed) R.drawable.ic_checkbox_checked
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
                    viewModel.checkPolicy()
                },
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = "Navigate to privacy policy screen",
                colorFilter = ColorFilter.tint(AppTheme.palette.gray.getColor(5))
            )
        }
    }

    return viewModel.email != "" && viewModel.password.length > 7 && isAgreed
}
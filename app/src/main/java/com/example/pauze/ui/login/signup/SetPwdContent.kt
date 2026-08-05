package com.example.pauze.ui.login.signup

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.login.component.AgreementCheckbox
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import java.util.regex.Pattern

@Composable
fun SetPwdContent(
    viewModel: SignUpViewModel
): Boolean{

    val focusManager = LocalFocusManager.current
    var isPwdFocused by remember { mutableStateOf(false) }
    val pwdCheck = Pattern.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).+$", viewModel.password)

    Column {
        isPwdFocused = ModeBasedTextField(
            mode = TextFieldMode.SetPwd,
            value = viewModel.password,
            onValueChanged = { viewModel.password = it },
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if(isPwdFocused || viewModel.password == "") "영문,숫자,특수문자 포함 8자 이상 입력해주세요"
            else if(viewModel.password.length < 8) "비밀번호는 8자리 이상이어야 해요"
            else if(!pwdCheck) "영문, 숫자, 특수문자를 최소 한 글자 이상 포함해주세요"
            else "사용 가능한 비밀번호입니다",
            style = bodyTextSmRegular,
            color = if(viewModel.password == "") AppTheme.palette.gray.getColor(5)
            else if(!isPwdFocused && (viewModel.password.length > 1 && viewModel.password.length < 8 || !pwdCheck) )
                AppTheme.palette.secondary.getColor(4)
            else AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(12.dp))
        ModeBasedTextField(
            mode = TextFieldMode.SetPwd,
            value = viewModel.pwdCheck,
            onValueChanged = { viewModel.pwdCheck = it },
            imeAction = ImeAction.Done,
            checkPasswordSame = { viewModel.password == viewModel.pwdCheck }
        )
        if( viewModel.pwdCheck.isNotEmpty() && viewModel.password != viewModel.pwdCheck){
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "비밀번호가 일치하지 않습니다",
                style = bodyTextSmRegular,
                color = AppTheme.palette.secondary.getColor(4)
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        AgreementCheckbox(viewModel, viewModel.isAgreedToTerm, true, focusManager, false)
        Spacer(modifier = Modifier.height(12.dp))
        AgreementCheckbox(viewModel, viewModel.isAgreedToPolicy, false, focusManager, false)
    }

    return viewModel.password.length > 7 && pwdCheck && viewModel.isAgreedToTerm && viewModel.isAgreedToPolicy
}
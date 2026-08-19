package com.example.pauze.ui.login.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular

// 이름 및 닉네임
@Composable
fun SetNameAndNickname(
    viewModel: SignUpViewModel
): Boolean {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
        indication = null
        ){
            focusManager.clearFocus()
        }
    ){
        // 이름
        ModeBasedTextField(
            mode = TextFieldMode.UserName,
            value = viewModel.name,
            onValueChanged = { viewModel.updateName(it) },
            imeAction = ImeAction.Next
        )
        Text(
            "2자 이상 입력해주세요",
            style = bodyTextSmRegular,
            color = if(viewModel.name.length == 1)
                AppTheme.palette.secondary.getColor(4)
            else AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(12.dp))
        // 닉네임
        ModeBasedTextField(
            mode = TextFieldMode.Nickname,
            value = viewModel.nickname,
            onCheckClick = { viewModel.checkNicknameAvailable() },
            checkClickValue = viewModel.isNicknameAvailable,
            onValueChanged = {
                viewModel.updateNickname(it)
                viewModel.updateNicknameAvailability(null)
            },
            imeAction = ImeAction.Done
        )
        Text(
            when (viewModel.isNicknameAvailable) {
                true -> "사용 가능한 닉네임입니다"
                false -> "이미 사용된 닉네임입니다"
                else -> "10자 이내로 입력해주세요"
            },
            style = bodyTextSmRegular,
            color = if(viewModel.nickname.length > 10
                || viewModel.isNicknameAvailable == false)
                AppTheme.palette.secondary.getColor(4)
            else if(viewModel.isNicknameAvailable == true)
                AppTheme.palette.primary.getColor(4)
            else AppTheme.palette.gray.getColor(5)
        )
    }
    return viewModel.name.length > 1 && viewModel.nickname.length < 10 && viewModel.isNicknameAvailable == true
}

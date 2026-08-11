package com.example.pauze.ui.login.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.pauze.ui.component.BirthdayBottomSheet
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.SetBirthday
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.login.component.NameAndNicknameField
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

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
        ModeBasedTextField(
            mode = TextFieldMode.UserName,
            value = viewModel.name,
            onValueChanged = { viewModel.name = it },
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
        ModeBasedTextField(
            mode = TextFieldMode.Nickname,
            value = viewModel.nickname,
            onCheckClick = { /*추후 구현*/},
            onValueChanged = { viewModel.nickname = it },
            imeAction = ImeAction.Done
        )
        Text(
            "10자 이내로 입력해주세요",
            style = bodyTextSmRegular,
            color = if(viewModel.nickname.length > 10)
                AppTheme.palette.secondary.getColor(4)
            else AppTheme.palette.gray.getColor(5)
        )
    }
    return viewModel.name.length > 1 && viewModel.nickname.length < 10
}

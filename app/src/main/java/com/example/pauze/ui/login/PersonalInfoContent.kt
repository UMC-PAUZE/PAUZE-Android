package com.example.pauze.ui.login

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
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun PersonalInfoContent(
    viewModel: SignUpViewModel
): Boolean {
    val focusManager = LocalFocusManager.current
    var tempDay by remember { mutableStateOf<LocalDate?>(null)}
    var showBottomSheet by remember { mutableStateOf(false) }

    val customDateFormat = LocalDate.Format{
        year()
        char('-')
        monthNumber()
        char('-')
        day()
    }

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
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "2자 이상 입력해주세요",
            style = bodyTextSmRegular,
            color = if(viewModel.name.length == 1) AppTheme.palette.secondary.getColor(4)
                else AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(12.dp))
        SetBirthday(
            birthday = viewModel.birthday?.format(customDateFormat) ?: "생년월일을 입력해주세요",
            onClick = {
                focusManager.clearFocus()
                showBottomSheet = true
            }
        )

        if(showBottomSheet){
            BirthdayBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onDateChanged = { tempDay = it },
                onClick = { viewModel.birthday = tempDay; showBottomSheet = false }
            )
        }
    }
    return viewModel.name.length > 1 && viewModel.birthday != null
}

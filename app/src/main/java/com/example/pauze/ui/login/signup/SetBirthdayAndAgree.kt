package com.example.pauze.ui.login.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.BirthdayBottomSheet
import com.example.pauze.ui.component.SetBirthday
import com.example.pauze.ui.login.component.AgreementCheckbox
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

// 생일 입력, 약관 및 개인정보 처리방침 동의
@Composable
fun SetBirthdayAndAgree(
    viewModel: SignUpViewModel
): Boolean {
    val focusManager = LocalFocusManager.current
    var tempDay by remember { mutableStateOf<LocalDate?>(null)}
    val customDateFormat = LocalDate.Format{
        year()
        char('-')
        monthNumber()
        char('-')
        day()
    }
    // 생일
    SetBirthday(
        birthday = viewModel.birthday?.format(customDateFormat) ?: "생년월일을 입력해주세요",
        onClick = {
            focusManager.clearFocus()
            viewModel.showBirthdayPicker(true)
        }
    )
    Spacer(modifier = Modifier.height(48.dp))
    // 약관 동의
    AgreementCheckbox(viewModel, viewModel.isAgreedToTerm, true, focusManager, false)
    Spacer(modifier = Modifier.height(12.dp))
    AgreementCheckbox(viewModel, viewModel.isAgreedToPolicy, false, focusManager, false)

    if(viewModel.showBirthdayPicker){
        BirthdayBottomSheet(
            onDismissRequest = { viewModel.showBirthdayPicker(false) },
            onDateChanged = { tempDay = it },
            onClick = {
                viewModel.updateBirthday(tempDay)
                viewModel.showBirthdayPicker(false)
            }
        )
    }

    return viewModel.birthday != null && viewModel.isAgreedToTerm && viewModel.isAgreedToPolicy
}
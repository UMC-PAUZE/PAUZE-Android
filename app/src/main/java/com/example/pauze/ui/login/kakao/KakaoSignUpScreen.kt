package com.example.pauze.ui.login.kakao

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pauze.ui.component.BirthdayBottomSheet
import com.example.pauze.ui.component.BirthdayPicker
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.SetBirthday
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.login.component.EnterVerificationCode
import com.example.pauze.ui.login.LoginNavDestination
import com.example.pauze.ui.login.component.AgreementCheckbox
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.headingMdMedium
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun KakaoSignUpScreen(
    navController: NavController,
    viewModel: KakaoSignUpViewModel = viewModel()
){
    val focusManager = LocalFocusManager.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.effect) {
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        val isAgreedToTerm = savedStateHandle?.get<Boolean>("isAgreedToTerm")
        val isAgreedToPolicy = savedStateHandle?.get<Boolean>("isAgreedToPolicy")
        if(isAgreedToTerm != null){
            viewModel.updateIsAgreedToTerm(isAgreedToTerm)
            savedStateHandle.remove<Boolean>("isAgreedToTerm")
        }
        if(isAgreedToPolicy != null){
            viewModel.updateIsAgreedToPolicy(isAgreedToPolicy)
            savedStateHandle.remove<Boolean>("isAgreedToPolicy")
        }

        viewModel.effect.collect { effect ->
            when(effect){
                is KakaoSignUpEffect.BackStack -> {
                    navController.popBackStack()
                }
                is KakaoSignUpEffect.NavigateToPolicy -> {
                    navController.navigate(LoginNavDestination.Policy(effect.isTermOfUse))
                }
                is KakaoSignUpEffect.NavigateToCompleted -> {
                    navController.navigate(LoginNavDestination.Completed(viewModel.name))
                }
                is KakaoSignUpEffect.ShowBirthdayPicker -> {
                    viewModel.showBirthdayPicker = true
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ){
                focusManager.clearFocus()
            }
     ){
        Spacer(modifier = Modifier.height(16.dp))
        TopBar(
            "추가 정보 입력",
            onBackClick = { viewModel.backStack() }
        )
        Column(modifier = Modifier.padding(24.dp)){
            Text(
                "서비스 이용을 위해 아래 정보를\n추가로 입력해주세요",
                style = headingMdMedium,
                color = AppTheme.palette.gray.getColor(2)
            )
            Spacer(modifier = Modifier.height(48.dp))
            isCompleted = AdditionalInfoContent(focusManager, viewModel)
            Spacer(modifier = Modifier.height(48.dp))
            AgreementCheckbox(viewModel, viewModel.isAgreedToTerm, true, focusManager, true)
            Spacer(modifier = Modifier.height(12.dp))
            AgreementCheckbox(viewModel, viewModel.isAgreedToPolicy, false, focusManager, true)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                "가입 완료하기",
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.signUp() },
                enabled = isCompleted && viewModel.isAgreedToTerm && viewModel.isAgreedToPolicy,
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AdditionalInfoContent(
    focusManager: FocusManager,
    viewModel: KakaoSignUpViewModel
): Boolean {
    var tempDay by remember { mutableStateOf<LocalDate?>(null)}
    val customDateFormat = LocalDate.Format{
        year()
        char('-')
        monthNumber()
        char('-')
        day()
    }

    Column {
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
        Spacer(modifier = Modifier.height(12.dp))
        SetBirthday(
            birthday = viewModel.birthday?.format(customDateFormat) ?: "생년월일을 입력해주세요",
            onClick = {
                focusManager.clearFocus()
                viewModel.showBirthdayPicker()
            }
        )

        if(viewModel.showBirthdayPicker){
            BirthdayBottomSheet(
                onDismissRequest = { viewModel.showBirthdayPicker = false },
                onDateChanged = { tempDay = it },
                onClick = { viewModel.birthday = tempDay; viewModel.showBirthdayPicker = false }
            )
        }
    }
    return viewModel.name.length > 1 && viewModel.nickname.length < 10 && viewModel.birthday != null
}
package com.example.pauze.ui.login.linking

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.login.LoginNavDestination
import com.example.pauze.ui.login.component.EnterVerificationCode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.headingMdMedium

@Composable
fun AccountLinkingScreen(
    navController: NavController,
    viewModel: LinkingViewModel = viewModel()
){
    val focusManager = LocalFocusManager.current
    var isVerified by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is LinkingEffect.RestartVerifTimer -> {
                    viewModel.startTimer()
                }
                is LinkingEffect.BackStack -> {
                    navController.popBackStack()
                }
                is LinkingEffect.NavigateToLogin -> {
                    navController.navigate(LoginNavDestination.Login)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ){
                focusManager.clearFocus()
            }
            .padding(horizontal = 24.dp),
    ) {
        TopBar(
            "연동하기",
            onBackClick = { viewModel.backStack() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            if(viewModel.phase == 0) "연동하기 위해\n이메일을 인증해주세요"
            else "인증코드를 입력하고\n본인인증을 완료해주세요",
            style = headingMdMedium,
            color = AppTheme.palette.gray.getColor(2)
        )
        Spacer(modifier = Modifier.height(48.dp))
        if(viewModel.phase == 0){
            ModeBasedTextField(
                mode = TextFieldMode.SetEmail,
                value = viewModel.email,
                onValueChanged = { viewModel.email = it },
                imeAction = ImeAction.Done,
                onCheckClick = { viewModel.updatePhase() },
                checkClickValue = { true },
            )
        } else {
            isVerified = EnterVerificationCode(viewModel, true)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                "완료",
                onClick = {
                    viewModel.navigateToLogin()
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                enabled = isVerified,
            )
        }

    }
}
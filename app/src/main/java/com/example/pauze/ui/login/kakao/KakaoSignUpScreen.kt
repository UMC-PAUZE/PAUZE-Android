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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.login.component.EnterVerificationCode
import com.example.pauze.ui.login.LoginNavDestination
import com.example.pauze.ui.login.component.AgreementCheckbox
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.headingMdMedium

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
        val agreedFromPolicy = savedStateHandle?.get<Boolean>("isAgreed")
        if(agreedFromPolicy != null){
            viewModel.updateIsAgreed(agreedFromPolicy)
            savedStateHandle.remove<Boolean>("isAgreed")
        }

        viewModel.effect.collect { effect ->
            when(effect){
                KakaoSignUpEffect.RestartVerifTimer -> {
                    viewModel.startTimer()
                }
                KakaoSignUpEffect.BackStack -> {
                    navController.popBackStack()
                }
                KakaoSignUpEffect.NavigateToPolicy -> {
                    navController.navigate(LoginNavDestination.Policy)
                }
                KakaoSignUpEffect.NavigateToCompleted -> {
                    navController.navigate(LoginNavDestination.Completed(viewModel.name))
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
            if(viewModel.phase == 0) "추가 정보 입력" else "회원가입",
            onBackClick = { viewModel.backStack() }
        )
        Column(modifier = Modifier.padding(24.dp)){
            Text(
                if(viewModel.phase == 0) "서비스 이용을 위해 아래 정보를\n추가로 입력해주세요"
                else "인증코드를 입력하고\n이용약관에 동의해주세요",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = headingMdMedium,
                color = AppTheme.palette.gray.getColor(2)
            )
            Spacer(modifier = Modifier.height(48.dp))
            if(viewModel.phase == 0) {
                AdditionalInfoContent(viewModel)
            } else {
                isCompleted = EnterVerificationCode(viewModel, true)
                Spacer(modifier = Modifier.height(24.dp))
                AgreementCheckbox(viewModel, viewModel.isAgreed, focusManager, true)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    if(isCompleted) "시작하기" else "다음",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.signUp() },
                    enabled = isCompleted,
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
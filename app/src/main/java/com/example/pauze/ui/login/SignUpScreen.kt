package com.example.pauze.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.PhaseBar
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.headingMdMedium

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()      // todo: Hilt로 변경
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
                SignUpEffect.RestartVerifTimer -> {
                    viewModel.startTimer()
                }
                SignUpEffect.BackStack -> {
                    navController.popBackStack()
                }
                SignUpEffect.NavigateToPolicy -> {
                    navController.navigate(LoginNavDestination.Policy)
                }
                SignUpEffect.NavigateToCompleted -> {
                    navController.navigate(LoginNavDestination.Completed(viewModel.name))
                }
                SignUpEffect.ShowBirthdayPicker -> {
                    viewModel.showBirthdayPicker = true
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
            .padding(horizontal = 24.dp)
    ) {
        TopBar(
            "회원가입",
            modifier = Modifier.padding(),
            onBackClick = { viewModel.backStack() }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = false)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 1)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 2)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 3)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            when(viewModel.phase){
                0 -> "이름과 생년월일을\n알려주세요"
                1 -> "이메일을 입력하고\n중복확인을 완료해주세요"
                2 -> "인증코드를 입력하고\n본인인증을 완료해주세요"
                else -> "비밀번호를 설정하고\n이용약관에 동의해주세요"
            },
            style = headingMdMedium,
            color = AppTheme.palette.gray.getColor(2)
        )
        Spacer(modifier = Modifier.height(48.dp))
        isCompleted = when(viewModel.phase){
            0 -> PersonalInfoContent(viewModel)
            1 -> SetAndCheckEmail(viewModel)
            2 -> EnterVerificationCode(viewModel)
            else -> SetPwdContent(viewModel)
        }
        if(viewModel.phase == 1){
            if(isCompleted){
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    "인증코드 받기",
                    onClick = { viewModel.updatePhase() },
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.palette.gray.getColor(7),
                    contentColor = AppTheme.palette.gray.getColor(2)
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                if(viewModel.phase == 3) "가입 완료하기" else "다음",
                onClick = {
                    if(isCompleted){
                        viewModel.updatePhase()
                    }
                    if(viewModel.phase == 4) {
                        viewModel.signUp()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                enabled = isCompleted,
            )
        }
    }
}
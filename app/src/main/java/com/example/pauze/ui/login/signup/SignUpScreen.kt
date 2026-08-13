package com.example.pauze.ui.login.signup

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
import androidx.compose.foundation.layout.width
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.PhaseBar
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.login.component.EnterVerificationCode
import com.example.pauze.ui.login.LoginNavDestination
import com.example.pauze.ui.login.component.GetVerifCodeButton
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.headingMdMedium

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
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
                is SignUpEffect.RestartVerifTimer -> {
                    viewModel.startTimer()
                }
                is SignUpEffect.BackStack -> {
                    navController.popBackStack()
                }
                is SignUpEffect.NavigateToPolicy -> {
                    navController.navigate(LoginNavDestination.Policy(effect.isTermOfUse))
                }
                is SignUpEffect.NavigateToCompleted -> {
                    navController.navigate(LoginNavDestination.Completed(viewModel.name))
                }
                is SignUpEffect.NavigateToLink -> {
                    navController.navigate(LoginNavDestination.Link)
                }
                is SignUpEffect.ShowLinkDialog -> {
                    viewModel.showLinkDialog = true
                }
                is SignUpEffect.ShowBirthdayPicker -> {
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
    ) {
        TopBar(
            "회원가입",
            onBackClick = { viewModel.backStack() }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = false)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 1)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 2)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 3)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = viewModel.phase < 4)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(24.dp)){
            Text(
                when(viewModel.phase){
                    0 -> "이메일을 입력하고\n중복확인을 완료해주세요"
                    1 -> "인증코드를 입력하고\n본인인증을 완료해주세요"
                    2 -> "안전하게 사용할\n비밀번호를 만들어주세요"
                    3 -> "이름과 닉네임을\n설정해주세요"
                    else -> "생년월일을 선택하고\n약관에 동의해주세요"
                },
                style = headingMdMedium,
                color = AppTheme.palette.gray.getColor(2)
            )
            Spacer(modifier = Modifier.height(48.dp))
            isCompleted = when(viewModel.phase){
                0 -> SetAndCheckEmail(viewModel)
                1 -> EnterVerificationCode(viewModel, false)
                2 -> SetPwdContent(viewModel)
                3 -> SetNameAndNickname(viewModel)
                else -> SetBirthdayAndAgree(viewModel)
            }
            if(viewModel.phase == 0){
                if(isCompleted){
                    Spacer(modifier = Modifier.height(12.dp))
                    GetVerifCodeButton(viewModel, false)
                }
            } else {
                Spacer(modifier = Modifier.padding(horizontal = 24.dp).weight(1f))
                Button(
                    if(viewModel.phase == 4) "가입 완료하기" else "다음",
                    onClick = {
                        if(isCompleted){
                            viewModel.updatePhase()
                        }
                        if(viewModel.phase == 5) {
                            viewModel.signUp()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                    enabled = isCompleted,
                )
            }
        }
    }
}
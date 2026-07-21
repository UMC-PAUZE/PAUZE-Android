package com.example.pauze.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.login.component.ModeBasedTextField
import com.example.pauze.ui.login.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = LoginNavDestination.Login){
                    composable<LoginNavDestination.Login> {
                        LoginScreen(navController)
                    }
                    composable<LoginNavDestination.Home> {

                    }
                    composable<LoginNavDestination.SignUp> {
                        SignUpScreen(navController)
                    }
                    composable<LoginNavDestination.Policy> {
                        PrivacyPolicyScreen(navController)
                    }
                    composable<LoginNavDestination.Completed> {
                        SignUpCompletedScreen(context = this@LoginActivity)
                    }
                }
            }
        }
    }
}


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()     // todo: Hilt로 변경
){

    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // 로그인 성공 여부 collect하기
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is LoginEffect.NavigateToHome -> {

                }
                is LoginEffect.NavigateToSignUp -> {
                    navController.navigate(LoginNavDestination.SignUp(isAgreed = false))
                }
                is LoginEffect.ShowDialog -> {
                    showDialog = true
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.pauze_logo),
            contentDescription = "pauze app logo",
        )
        Spacer(modifier = Modifier.height(48.dp))
        ModeBasedTextField(
            mode = TextFieldMode.Email,
            value = email,
            onValueChanged = { email = it },
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(12.dp))
        ModeBasedTextField(
            mode = TextFieldMode.Pwd,
            value = password,
            onValueChanged = { password = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            "로그인",
            onClick = {
                if(email != "" && password != ""){
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            color = if(email != "" && password != "") AppTheme.palette.gray.getColor(2) else AppTheme.palette.gray.getColor(8),
            contentColor = AppTheme.palette.gray.getColor(9),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .background(color = AppTheme.palette.gray.getColor(5))
                    .height(1.dp)
                    .weight(1f)
            )
            Text(
                "또는",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(5)
            )
            Spacer(
                modifier = Modifier
                    .background(color = AppTheme.palette.gray.getColor(5))
                    .height(1.dp)
                    .weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFEE500),
                    shape = RoundedCornerShape(size = 100.dp))
                .clickable{
                    viewModel.loginWithKakao()
                }
                .padding(horizontal = 28.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.kakao_logo),
                    contentDescription = "kakao logo",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("카카오로 시작하기", style = bodyTextXlBold, color = Color(0xCC000000))
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            "게스트로 둘러보기",
            onClick = { viewModel.toGuestMode() },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("계정이 없으신가요?", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(5))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "회원가입",
                modifier = Modifier.clickable{
                    viewModel.toSignUp()
                },
                style = bodyTextMdBold,
                color = AppTheme.palette.gray.getColor(2)
            )
        }

        if(showDialog){
            Dialog(
                title = "로그인 오류",
                content = "이메일이나 비밀번호가 일치하지 않습니다",
                btnCancel = "다시 입력하기",
                onDismissRequest = { showDialog = false}
            )
        }
    }
}

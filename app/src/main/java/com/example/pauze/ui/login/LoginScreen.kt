package com.example.pauze.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold

class LoginScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                LoginLayout()
            }
        }
    }
}


@Composable
fun LoginLayout(){
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPwdVisible by remember { mutableStateOf(false) }
    var isFieldFocused by remember { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxWidth().border(
                    width = 1.dp,
                    color = if (isFieldFocused) AppTheme.palette.gray.getColor(3)
                            else AppTheme.palette.gray.getColor(6),
                    shape = RoundedCornerShape(size = 16.dp)
                ).onFocusChanged {
                    isFieldFocused = it.isFocused
                }.padding(
                    horizontal = 16.dp, vertical = 14.dp
                )
        ){
            Text(
                "이메일",
                color = AppTheme.palette.gray.getColor(5),
                style = bodyTextMdRegular,
            )
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                decorationBox = { innerTextField ->
                    if(email.isEmpty()){
                        Text(
                            "텍스트",
                            color = AppTheme.palette.gray.getColor(5),
                            style = bodyTextLgMedium,
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Row(modifier = Modifier.weight(1f)){
                                innerTextField()
                            }
                            IconButton(onClick = {
                                email = ""
                            }) {
                                Image(painter = painterResource(R.drawable.cancel_circle), contentDescription = "cancel")
                            }
                        }
                    }
                },
                textStyle = bodyTextLgMedium.copy(color = AppTheme.palette.gray.getColor(2))
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth().border(
                    width = 1.dp,
                    color = if (isFieldFocused) AppTheme.palette.gray.getColor(3)
                            else AppTheme.palette.gray.getColor(6),
                    shape = RoundedCornerShape(size = 16.dp)
                ).onFocusChanged {
                    isFieldFocused = it.isFocused
                }.padding(
                    horizontal = 16.dp, vertical = 14.dp
                )

        ){
            Text(
                "비밀번호",
                color = AppTheme.palette.gray.getColor(5),
                style = bodyTextMdRegular,
            )
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPwdVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                decorationBox = { innerTextField ->
                    if(password.isEmpty()){
                        Text(
                            "텍스트",
                            color = AppTheme.palette.gray.getColor(5),
                            style = bodyTextLgMedium,
                        )
                    }
                    else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Row(modifier = Modifier.weight(1f)){
                                innerTextField()
                            }
                            IconButton(onClick = {
                                isPwdVisible = !isPwdVisible
                            }) {
                                // todo: 추후 비밀번호 표시 아이콘 나오면 수정
                                Image(
                                    painter = painterResource(R.drawable.pwd_eye),
                                    contentDescription = "cancel"
                                )
                            }
                        }
                    }
                },
                textStyle = bodyTextLgMedium.copy(color = AppTheme.palette.gray.getColor(2))
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if(email.isEmpty() || password.isEmpty())
                        AppTheme.palette.gray.getColor(8)
                        else AppTheme.palette.gray.getColor(2),
                    shape = RoundedCornerShape(size = 100.dp))
                .clickable(
                    onClick = { showDialog = true }
                )
                .padding(horizontal = 28.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ){
            Text("로그인", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(9))
        }
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
                    // todo: 카카오 로그인 구현
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = AppTheme.palette.gray.getColor(2),
                    shape = RoundedCornerShape(size = 100.dp))
                .clickable{
                    // todo: 홈화면으로 이동
                }
                .padding(horizontal = 28.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ){
            Text("게스트로 둘러보기", style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(9))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("계정이 없으신가요?", style = bodyTextMdMedium, color = AppTheme.palette.gray.getColor(5))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "카카오로 시작하기",
                modifier = Modifier.clickable{
                    // todo: 회원가입 스크린으로 이동
                },
                style = bodyTextMdBold,
                color = AppTheme.palette.gray.getColor(2)
            )
        }

        if(showDialog){
            Dialog(
                onDismissRequest = { showDialog = false },
            ) {
                Column(
                    modifier = Modifier.background(
                        color = AppTheme.palette.gray.getColor(8),
                        shape = RoundedCornerShape(24.dp),)
                        .padding(top = 24.dp, start = 12.dp, end = 12.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        "로그인 오류",
                        style = bodyTextLgBold,
                        color = AppTheme.palette.gray.getColor(2),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "이메일이나 비번이 일치하지 않습니다",
                        style = bodyTextSmRegular,
                        color = AppTheme.palette.gray.getColor(2)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AppTheme.palette.gray.getColor(5),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "다시 입력하기",
                            style = bodyTextLgBold,
                            color = AppTheme.palette.gray.getColor(2)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginLayoutPreview(){
    PAUZEAndroidTheme {
        LoginLayout()
    }
}
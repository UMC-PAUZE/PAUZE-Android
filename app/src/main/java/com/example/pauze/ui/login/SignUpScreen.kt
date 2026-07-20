package com.example.pauze.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.PhaseBar
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.headingMdMedium
import java.sql.DriverManager.println

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                SignUpScreen()
            }
        }
    }
}

@Composable
fun SignUpScreen(){
    var phase by remember { mutableIntStateOf(1) }
    var isCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ) {
        TopBar("회원가입")
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = false)
            Spacer(modifier = Modifier.width(4.dp))
            PhaseBar(modifier = Modifier.weight(1f), isWaiting = phase == 0)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            if(phase == 0) "이름과 생년월일을\n알려주세요" else "이메일을 입력하고\n비밀번호를 설정해주세요",
            style = headingMdMedium,
            color = AppTheme.palette.gray.getColor(2)
        )
        Spacer(modifier = Modifier.height(48.dp))
        isCompleted = if(phase == 0) PersonalInfoContent() else SetEmailAndPwdContent()
        Spacer(modifier = Modifier.weight(1f))
        Button(
            if(phase == 0) "다음" else "가입하기",
            onClick = {
                if(isCompleted){
                    phase = phase + 1
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
            color = if(isCompleted) AppTheme.palette.gray.getColor(2)
                else AppTheme.palette.gray.getColor(8),
            contentColor = AppTheme.palette.gray.getColor(9),)
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){
    PAUZEAndroidTheme {
        SignUpScreen()
    }
}
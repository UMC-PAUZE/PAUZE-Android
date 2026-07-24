package com.example.pauze.ui.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular

enum class TextFieldMode { Email, SetEmail, Pwd, SetPwd, UserName, }
enum class Actions { Reset, Pwd, Check }

@Composable
fun ModeBasedTextField(
    mode: TextFieldMode,
    value: String,
    onValueChanged: (String) -> Unit,
    onCheckClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(when(mode){
        TextFieldMode.Pwd -> false
        TextFieldMode.SetPwd -> false
        else -> true
    }) }

    Column(
        modifier = Modifier
            .fillMaxWidth().border(
                width = 1.dp,
                color = if (isFocused) AppTheme.palette.gray.getColor(3)
                else AppTheme.palette.gray.getColor(6),
                shape = RoundedCornerShape(size = 16.dp)
            ).onFocusChanged {
                isFocused = it.isFocused
            }.padding(
                horizontal = 16.dp, vertical = 14.dp
            )
    ){
        Text(
            when(mode){
                TextFieldMode.UserName -> "이름"
                TextFieldMode.Email -> "이메일"
                TextFieldMode.SetEmail -> "이메일"
                else -> "비밀번호"
            },
            color = AppTheme.palette.gray.getColor(5),
            style = bodyTextMdRegular,
        )
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = { onValueChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = when (mode) {
                TextFieldMode.UserName -> KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
                TextFieldMode.Pwd, TextFieldMode.SetPwd -> KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
                else -> KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            },
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Next) },
                onDone = { focusManager.clearFocus() }
            ),
            decorationBox = { innerTextField ->
                if(value.isEmpty()){
                    Text(
                        when(mode){
                            TextFieldMode.SetEmail -> "example@gmail.com"
                            TextFieldMode.SetPwd -> "비밀번호를 입력해주세요"
                            TextFieldMode.UserName -> "실명을 입력해주세요"
                            else -> "텍스트"
                        },
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
                        when(mode){
                            TextFieldMode.SetEmail -> Row {
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                ActionButton(actions = Actions.Check) { onCheckClick }
                            }
                            TextFieldMode.Pwd -> ActionButton(actions = Actions.Pwd, isVisible = isVisible) { isVisible = !isVisible }
                            TextFieldMode.SetPwd -> Row {
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                ActionButton(actions = Actions.Pwd, isVisible = isVisible) { isVisible = !isVisible }
                            }
                            else -> ActionButton(actions = Actions.Reset) { onValueChanged("") }
                        }

                    }
                }
            },
            textStyle = bodyTextLgMedium.copy(color = AppTheme.palette.gray.getColor(2))
        )
    }
}

@Composable
fun ActionButton(actions: Actions, isVisible: Boolean = false, onClick: () -> Unit){
    when(actions){
        Actions.Reset -> IconButton(onClick = onClick) {
            Image(painter = painterResource(R.drawable.cancel_circle), contentDescription = "cancel")
        }
        Actions.Pwd -> IconButton(onClick = onClick) {
            Image(
                painter = if(isVisible) painterResource(R.drawable.pwd_eye_on)
                else painterResource(R.drawable.pwd_eye_off),
                contentDescription = "hide and show password"
            )
        }
        Actions.Check -> TextButton(onClick = onClick) {
            Text("중복확인", color = AppTheme.palette.gray.getColor(2),
                style = bodyTextMdRegular,)
        }
    }
}
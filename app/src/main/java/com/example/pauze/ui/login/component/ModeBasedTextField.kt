package com.example.pauze.ui.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager

enum class TextFieldMode { Email, SetEmail, Pwd, SetPwd, UserName, }
enum class Actions { Reset, Pwd, Check }

@Composable
fun ModeBasedTextField(
    mode: TextFieldMode,
    value: String,
    onValueChanged: (String) -> Unit,
    isVisible: Boolean = false,
    onVisibilityChanged: (Boolean) -> Unit,
    onCheckClick: () -> Unit,
    isFocused: Boolean = false,
    onFocusChanged: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth().border(
                width = 1.dp,
                color = if (isFocused) AppTheme.palette.gray.getColor(3)
                else AppTheme.palette.gray.getColor(6),
                shape = RoundedCornerShape(size = 16.dp)
            ).onFocusChanged {
                onFocusChanged
            }.padding(
                horizontal = 16.dp, vertical = 14.dp
            )
    ){
        Text(
            when(mode){
                TextFieldMode.Email -> "이메일"
                TextFieldMode.SetEmail -> "이메일"
                TextFieldMode.Pwd -> "비밀번호"
                TextFieldMode.SetPwd -> "비밀번호"
                TextFieldMode.UserName -> "이름"
            },
            color = AppTheme.palette.gray.getColor(5),
            style = bodyTextMdRegular,
        )
        BasicTextField(
            value = value,
            onValueChange = { onValueChanged(value) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = when(mode){
                TextFieldMode.Email -> KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
                TextFieldMode.SetEmail -> KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
                TextFieldMode.Pwd -> KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
                TextFieldMode.SetPwd -> KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
                TextFieldMode.UserName -> KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            },
            keyboardActions = KeyboardActions(
                onNext = { onFocusChanged },
                onDone = { onFocusChanged }
            ),

            decorationBox = { innerTextField ->
                if(value.isEmpty()){
                    Text(
                        when(mode){
                            TextFieldMode.Email -> "텍스트"
                            TextFieldMode.SetEmail -> "example@gmail.com"
                            TextFieldMode.Pwd -> "텍스트"
                            TextFieldMode.SetPwd -> "비밀번호를 입력해주세요"
                            TextFieldMode.UserName -> "실명을 입력해주세요"
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
                        // mode에 따라 수정
                        when(mode){
                            TextFieldMode.Email -> ActionButton(actions = Actions.Reset, ) { onValueChanged("") }
                            TextFieldMode.SetEmail -> Row {
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                ActionButton(actions = Actions.Check) { onCheckClick }
                            }
                            TextFieldMode.Pwd -> ActionButton(actions = Actions.Pwd) { onVisibilityChanged(!isVisible) }
                            TextFieldMode.SetPwd -> Row {
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                ActionButton(actions = Actions.Pwd) { onVisibilityChanged(!isVisible) }
                            }
                            TextFieldMode.UserName -> ActionButton(actions = Actions.Reset) { onValueChanged("") }
                        }

                    }
                }
            },
            textStyle = bodyTextLgMedium.copy(color = AppTheme.palette.gray.getColor(2))
        )
    }
}
@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    innerTextField: @Composable () -> Unit,
    actionButtons: @Composable RowScope.() -> Unit
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            innerTextField()
        }
        actionButtons()
    }
}

@Composable
fun ActionButton(actions: Actions, onClick: () -> Unit){
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
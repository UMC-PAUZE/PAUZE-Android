package com.example.pauze.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import androidx.compose.foundation.background
import androidx.compose.material3.TextField
import androidx.compose.ui.tooling.preview.Preview

enum class TextFieldMode { Email, SetEmail, Pwd, SetPwd, PwdCheck, UserName, Nickname, Bio, Verif }
enum class Actions { Reset, Pwd, EmailCheck, VerifCheck }

@Composable
fun ModeBasedTextField(
    mode: TextFieldMode,
    value: String,
    onValueChanged: (String) -> Unit,
    imeAction: ImeAction,
    commentText: String? = null,
    isError: Boolean = false,
    checkPasswordSame: () -> Boolean = { true },
    onCheckClick: () -> Unit = {},
    checkClickValue: Boolean? = null,
    showCheckButton: Boolean = true
): Boolean {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(when(mode){
        TextFieldMode.Pwd -> false
        TextFieldMode.SetPwd -> false
        else -> true
    }) }
    val nameCheck = java.util.regex.Pattern.compile("[!@#$%^&*]").matcher(value).find()
    val pwdCheck = java.util.regex.Pattern.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).+$", value)
    val nickNameCheck = value.length > 10

    Column(modifier = Modifier.fillMaxWidth()) {
    Column(
        modifier = Modifier
            .fillMaxWidth().border(
                width = 1.dp,
                color = when {
                    isError -> AppTheme.palette.secondary.getColor(4)
                    (mode == TextFieldMode.UserName && (value.length == 1
                            || nameCheck
                            || value.trim() != value))
                            || (mode == TextFieldMode.SetPwd
                            && !isFocused
                            && (value.length > 1 && value.length < 8
                            || !pwdCheck))
                            || (mode == TextFieldMode.Nickname && (nickNameCheck || checkClickValue == false))
                            || (mode == TextFieldMode.SetPwd && !checkPasswordSame())
                            || ((mode == TextFieldMode.SetEmail && checkClickValue == true || mode == TextFieldMode.Verif && checkClickValue == false))
                                 -> AppTheme.palette.secondary.getColor(4)
                    (mode == TextFieldMode.Nickname
                            || mode == TextFieldMode.Bio
                            || (mode == TextFieldMode.Verif && checkClickValue == true))
                            && isFocused
                        -> AppTheme.palette.primary.getColor(3)
                    isFocused -> AppTheme.palette.gray.getColor(3)
                    else -> AppTheme.palette.gray.getColor(6)
                },
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
                TextFieldMode.Nickname -> "닉네임"
                TextFieldMode.Bio -> "한 줄 소개(선택)"
                TextFieldMode.Verif -> "인증코드"
                else -> "비밀번호"
            },
            color = AppTheme.palette.gray.getColor(5),
            style = bodyTextMdRegular,
        )
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (mode == TextFieldMode.Bio && newValue.length > 30) {
                    onValueChanged(newValue.take(30))
                } else {
                    onValueChanged(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = when (mode) {
                TextFieldMode.UserName, TextFieldMode.Nickname, TextFieldMode.Bio -> KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = imeAction
                )
                TextFieldMode.Pwd, TextFieldMode.SetPwd -> KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = imeAction
                )
                else -> KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = imeAction
                )
            },
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Next) },
                onDone = { focusManager.clearFocus() }
            ),
            decorationBox = { innerTextField ->
                if(value == ""){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(modifier = Modifier.weight(1f)){
                            Text(
                                when(mode){
                                    TextFieldMode.SetEmail -> "example@gmail.com"
                                    TextFieldMode.SetPwd -> "비밀번호를 입력해주세요"
                                    TextFieldMode.UserName -> "실명을 입력해주세요"
                                    TextFieldMode.Nickname -> "닉네임을 설정해보세요."
                                    TextFieldMode.Bio -> "나를 한 문장으로 표현해보세요."
                                    TextFieldMode.Verif -> "인증코드 6자리를 입력해주세요"
                                    else -> "텍스트"
                                },
                                color = AppTheme.palette.gray.getColor(5),
                                style = bodyTextLgMedium,
                            )
                        }
                        if(mode == TextFieldMode.SetPwd){
                            ActionButton(actions = Actions.Pwd, isVisible = isVisible) { isVisible = !isVisible }
                        }
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(modifier = Modifier.weight(1f)){
                            innerTextField()
                        }
                        when(mode){
                            TextFieldMode.SetEmail -> Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                Spacer(modifier = Modifier.width(8.dp))
                                ActionButton(actions = Actions.EmailCheck) {
                                    onCheckClick()
                                    isFocused = false
                                }
                            }
                            TextFieldMode.Pwd -> ActionButton(actions = Actions.Pwd, isVisible = isVisible) { isVisible = !isVisible }
                            TextFieldMode.SetPwd -> Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                Spacer(modifier = Modifier.width(8.dp))
                                ActionButton(actions = Actions.Pwd, isVisible = isVisible) { isVisible = !isVisible }
                            }
                            TextFieldMode.Verif -> Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                ActionButton(actions = Actions.Reset) { onValueChanged("") }
                                Spacer(modifier = Modifier.width(8.dp))
                                ActionButton(actions = Actions.VerifCheck) {
                                    onCheckClick()
                                    isFocused = false
                                }
                            }
                            TextFieldMode.Nickname -> Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (showCheckButton) {
                                    ActionButton(actions = Actions.EmailCheck) {
                                        onCheckClick()
                                        isFocused = false
                                    }
                                }
                            }
                            else -> ActionButton(actions = Actions.Reset) { onValueChanged("") }
                        }

                    }
                }
            },
            textStyle = bodyTextLgMedium.copy(color = AppTheme.palette.gray.getColor(2))
        )
    }
    if (commentText != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            commentText,
            color = if (isError) AppTheme.palette.secondary.getColor(4) else AppTheme.palette.gray.getColor(5),
            style = bodyTextSmRegular
        )
    }
    }
    return isFocused
}

@Composable
fun ActionButton(actions: Actions, isVisible: Boolean = false, onClick: () -> Unit){
    when(actions){
        Actions.Reset -> Image(
            modifier = Modifier.clickable(onClick = onClick).padding(0.dp),
            painter = painterResource(R.drawable.cancel_circle),
            contentDescription = "cancel"
        )
        Actions.Pwd -> Image(
            modifier = Modifier.clickable(onClick = onClick).padding(vertical = 8.dp, horizontal = 0.dp),
            painter = if(isVisible) painterResource(R.drawable.pwd_eye_on)
                else painterResource(R.drawable.pwd_eye_off),
            contentDescription = "hide and show password"
        )
        Actions.EmailCheck -> Text(
            "중복확인",
            modifier = Modifier.clickable(onClick = onClick),
            color = AppTheme.palette.gray.getColor(2),
            style = bodyTextMdRegular
        )
        Actions.VerifCheck -> Text(
            "인증확인",
            modifier = Modifier.clickable(onClick = onClick),
            color = AppTheme.palette.gray.getColor(2),
            style = bodyTextMdRegular
        )
    }
}

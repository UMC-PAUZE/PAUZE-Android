package com.example.pauze.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.login.BirthdayBottomSheet
import com.example.pauze.ui.login.SetBirthday
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    var nickname by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf<LocalDate?>(null) }
    var tempDay by remember { mutableStateOf<LocalDate?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val dateFormat = LocalDate.Format {
        year(); char('.'); char(' ')
        monthNumber(); char('.'); char(' ')
        day()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
    ) {
        TopBar("프로필 편집", onBackClick = onBackClick)

        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 29.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(88.dp)) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(
                                color = AppTheme.palette.gray.getColor(7),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_person),
                            contentDescription = "프로필 이미지",
                            tint = AppTheme.palette.gray.getColor(8),
                            modifier = Modifier.size(width = 57.dp, height = 73.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .offset(x = 55.dp, y = 56.dp)
                            .size(40.dp)
                            .background(
                                color = AppTheme.palette.gray.getColor(4),
                                shape = CircleShape
                            )
                            .clickable(onClick = onCameraClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_camera),
                            contentDescription = "프로필 사진 변경",
                            tint = AppTheme.palette.gray.getColor(9),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(55.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModeBasedTextField(
                    mode = TextFieldMode.Nickname,
                    value = nickname,
                    onValueChanged = { nickname = it },
                    imeAction = ImeAction.Next
                )

                ModeBasedTextField(
                    mode = TextFieldMode.Bio,
                    value = bio,
                    onValueChanged = { bio = it },
                    imeAction = ImeAction.Done,
                    commentText = "${bio.length}/30"
                )

                SetBirthday(
                    birthday = birthday?.format(dateFormat) ?: "생년월일을 입력해보세요.",
                    onClick = { showBottomSheet = true }
                )
            }
        }

        if (showBottomSheet) {
            BirthdayBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onDateChanged = { tempDay = it },
                onClick = { birthday = tempDay; showBottomSheet = false }
            )
        }

        Button(
            label = "저장하기",
            onClick = onSaveClick,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 48.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ProfileEditScreenPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        ProfileEditScreen()
    }
}
package com.example.pauze.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme

@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
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
                            .align(Alignment.BottomEnd)
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

            // TODO: 입력필드 컴포넌트 추가
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
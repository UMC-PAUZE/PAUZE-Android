package com.example.pauze.ui.mypage

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.component.SetBirthday
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme

@Composable
fun ProfileEditScreen(
    navController: NavController,
    viewModel: ProfileEditViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEditEffect.NavigateToBack -> navController.popBackStack()
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { viewModel.onImagePicked(it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
    ) {
        TopBar("프로필 편집", onBackClick = viewModel::onBackClick)

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
                        val imageModel = viewModel.newProfileImageUri ?: viewModel.profileImageUrl
                        if (imageModel != null) {
                            AsyncImage(
                                model = imageModel,
                                contentDescription = "프로필 이미지",
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_person),
                                contentDescription = "프로필 이미지",
                                tint = AppTheme.palette.gray.getColor(8),
                                modifier = Modifier.size(width = 57.dp, height = 73.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .offset(x = 55.dp, y = 56.dp)
                            .size(40.dp)
                            .background(
                                color = AppTheme.palette.gray.getColor(4),
                                shape = CircleShape
                            )
                            .clickable {
                                pickImageLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
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
                    value = viewModel.nickname,
                    onValueChanged = { viewModel.updateNickname(it) },
                    imeAction = ImeAction.Next,
                    commentText = viewModel.loadError,
                    isError = viewModel.loadError != null
                )

                ModeBasedTextField(
                    mode = TextFieldMode.Bio,
                    value = viewModel.bio,
                    onValueChanged = { viewModel.updateBio(it) },
                    imeAction = ImeAction.Done,
                    commentText = "${viewModel.bio.length}/30"
                )

                SetBirthday(
                    birthday = viewModel.birthday ?: "생년월일을 입력해주세요",
                    onClick = {  },
                    showDropdownIcon = false
                )
            }
        }

        Button(
            label = "저장하기",
            onClick = viewModel::onSaveClick,
            enabled = viewModel.nickname.length >= 2,
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
        ProfileEditScreen(navController = rememberNavController())
    }
}
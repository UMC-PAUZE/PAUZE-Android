package com.example.pauze.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.ui.component.Dialog
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.mypage.component.MySettings
import com.example.pauze.ui.mypage.component.MySettingsVariant
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun AccountInfoScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
    viewModel: AccountInfoViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showWithdrawDialog by remember { mutableStateOf(false) }

    val dateFormat = LocalDate.Format {
        year(); char('.'); char(' ')
        monthNumber(); char('.'); char(' ')
        day()
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AccountInfoEffect.NavigateToBack -> onBackClick()
                is AccountInfoEffect.ShowWithdrawDialog -> showWithdrawDialog = true
                is AccountInfoEffect.NavigateToLogout -> onLogoutClick()
                is AccountInfoEffect.NavigateToWithdraw -> onWithdrawClick()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
    ){
        TopBar("계정 정보", onBackClick = viewModel::onBackClick )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.palette.primary.getColor(9),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                if ("KAKAO" in uiState.data.socialTypes) {
                    AccountInfoRow("연동계정") {
                        Row(
                            modifier = Modifier
                                .background(color = AppTheme.palette.primary.getColor(8), shape = RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(text = "카카오", style = bodyTextSmMedium, color = AppTheme.palette.primary.getColor(2))
                        }
                    }
                }
                AccountInfoRow("이메일", uiState.data.email ?: "")
                AccountInfoRow("가입일", uiState.data.joinedAt?.let { LocalDate.parse(it).format(dateFormat) } ?: "")

            }

            Column {
                MySettings(title = "로그아웃", onClick = viewModel::onLogoutClick)
                MySettings(
                    title = "회원 탈퇴",
                    titleColor = AppTheme.palette.secondary.getColor(4),
                    onClick = viewModel::onWithdrawClick
                )
            }

            if (showWithdrawDialog) {
                Dialog(
                    title = "탈퇴하시겠습니까?",
                    content = "탈퇴 시 저장된 데이터가 모두 사라집니다.",
                    btnCancel = "아니오",
                    btnContinue = "예",
                    onDismissRequest = { showWithdrawDialog = false },
                    onContinue = {
                        showWithdrawDialog = false
                        viewModel.onWithdrawConfirm()
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountInfoRow(label: String, value: String) {
    AccountInfoRow(label) {
        Text(text = value, style = bodyTextMdRegular, color = AppTheme.palette.primary.getColor(3))
    }
}

@Composable
private fun AccountInfoRow(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = bodyTextMdMedium, color = AppTheme.palette.primary.getColor(3))
        content()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountInfoScreenPreview() {
    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        AccountInfoScreen()
    }
}
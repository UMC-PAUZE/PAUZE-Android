package com.example.pauze.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.ui.login.component.TermAndPolicyText
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlBold


@Composable
fun TermsOfUseScreen(
    modifier : Modifier = Modifier,
    viewModel: TermsAndPolicyViewModel = viewModel()
){
    val terms = viewModel.termsOfUse

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ){
        item {
            Text(
                "PAUZE 서비스 이용약관",
                style = bodyTextXlBold,
                color = AppTheme.palette.gray.getColor(2)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "이 약관은 PAUZE 서비스 이용에 관한 기본 규칙을 정합니다.\n" +
                        "\n" +
                        "회원가입 또는 서비스 시작 시 본 약관에 동의한 것으로 간주됩니다.",
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(4)
            )
        }
        items(terms.size){ index ->
            if(terms[index].second == ""){
                Spacer(modifier = Modifier.height(48.dp))
                Text(terms[index].first, style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                TermAndPolicyText(
                    terms[index].first,
                    terms[index].second
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(136.dp))
        }
    }
}
package com.example.pauze.ui.login.agreement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlBold


@Composable
fun PrivacyPolicyScreen(
    modifier : Modifier = Modifier,
    viewModel: TermsAndPolicyViewModel = viewModel()
){
    val policies = viewModel.privacyPolicies

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ){
        item {
            Text(
                "PAUZE 개인정보 처리방침",
                style = bodyTextXlBold,
                color = AppTheme.palette.gray.getColor(2)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "PAUZE는 이용자의 개인정보를 소중히 여깁니다.\n수집한 정보는 서비스 제공 및 예민함 분석 목적으로만 사용되며, 제3자에게 제공되지 않습니다",
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(4)
            )
        }
        items(policies.size){ index ->
            if(policies[index].second == ""){
                Spacer(modifier = Modifier.height(48.dp))
                Text(policies[index].first, style = bodyTextXlBold, color = AppTheme.palette.gray.getColor(2))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                TermAndPolicyText(
                    policies[index].first,
                    policies[index].second
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(136.dp))
        }
    }
}
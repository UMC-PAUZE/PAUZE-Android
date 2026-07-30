package com.example.pauze.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlBold

@Composable
fun TermsAndPolicyScreen(
    navController: NavController,
    viewModel: TermsAndPolicyViewModel = viewModel()
){
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is TermsAndPolicyEffect.BackStack -> {
                    navController.popBackStack()
                }
                is TermsAndPolicyEffect.NavigateToSignUp -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("isAgreed", effect.isAgreed)
                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
    ){
        TopBar("이용약관 및 개인정보 처리방침", onBackClick = { viewModel.backStack() })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Tab("이용약관",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.changeTabIndex(0) },
                selected = viewModel.tab == 0
            )
            Spacer(modifier = Modifier.width(16.dp))
            Tab(
                "개인정보 처리방침",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.changeTabIndex(1) },
                selected = viewModel.tab == 1
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(viewModel.tab == 0){
            TermsOfUseScreen(Modifier.weight(1f))
        }
        else{
            PrivacyPolicyScreen(Modifier.weight(1f))
        }
        Button(
            "동의하고 돌아가기",
            onClick = { viewModel.backToSignUp(isAgreed = true) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 48.dp),
            enabled = true
        )
    }
}
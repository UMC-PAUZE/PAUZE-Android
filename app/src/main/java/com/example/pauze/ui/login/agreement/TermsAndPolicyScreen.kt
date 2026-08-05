package com.example.pauze.ui.login.agreement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.Tab
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme

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
                        ?.set(
                            if(viewModel.isTermOfUse) "isAgreedToTerm"
                            else "isAgreedToPolicy",
                            effect.isAgreed
                        )

                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
    ){
        TopBar(
            if(viewModel.isTermOfUse) "이용약관" else "개인정보 처리방침",
            onBackClick = { viewModel.backStack() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(viewModel.isTermOfUse){
            TermsOfUseScreen(Modifier.weight(1f))
        }
        else{
            PrivacyPolicyScreen(Modifier.weight(1f))
        }
        Button(
            "동의하고 돌아가기",
            onClick = { viewModel.backToSignUp(true) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 48.dp),
            enabled = true
        )
    }
}
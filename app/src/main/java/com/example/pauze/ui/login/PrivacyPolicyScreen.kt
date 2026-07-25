package com.example.pauze.ui.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular


@Composable
fun PrivacyPolicyScreen(
    navController: NavController,
    viewModel: PrivacyPolicyViewmodel = viewModel()      // todo: Hilt로 변경
){
    val policies = viewModel.policies

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when(effect){
                is PrivacyPolicyEffect.NavigateToSignUp -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("isAgreed", effect.isAgreed)
                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ){
        TopBar(
            "개인정보 처리방침",
            modifier = Modifier.padding(top = 40.dp, bottom = 16.dp),
            onBackClick = { viewModel.backToSignUp(null) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        policies.forEach { policy ->
            PolicyText(
                policy.first,
                policy.second
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            "동의하고 돌아가기",
            onClick = { viewModel.backToSignUp(isAgreed = true) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
            enabled = true
        )
    }
}

@Composable
fun PolicyText(
    title: String,
    content: String,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            title,
            style = bodyTextLgMedium,
            color = AppTheme.palette.gray.getColor(2)
        )
        Text(
            content,
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(4)
        )
    }
}
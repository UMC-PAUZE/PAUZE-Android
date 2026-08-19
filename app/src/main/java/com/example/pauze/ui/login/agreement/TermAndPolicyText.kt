package com.example.pauze.ui.login.agreement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular

@Composable
fun TermAndPolicyText(
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
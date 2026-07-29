package com.example.pauze.ui.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.ModeBasedTextField
import com.example.pauze.ui.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun EnterVerificationCode(
    viewModel: SignUpViewModel
): Boolean{
    var isFocused by remember { mutableStateOf(false) }

    Column{
        isFocused = ModeBasedTextField(
            mode = TextFieldMode.Verif,
            value = viewModel.verifCode,
            onValueChanged = { viewModel.verifCode = it },
            imeAction = ImeAction.Done,
            onCheckClick = { viewModel.checkVerifCodeRight() },
            checkClickValue = { viewModel.checkVerifCodeRight() }
        )
        if(!isFocused && viewModel.verifCode != ""){
            Text(
                if(viewModel.checkVerifCodeRight()) "인증에 성공했습니다"
                    else "인증에 실패했습니다",
                style = bodyTextSmRegular,
                color = if(viewModel.checkVerifCodeRight()) AppTheme.palette.primary.getColor(4)
                    else AppTheme.palette.secondary.getColor(4)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Text(
                viewModel.time,
                style = bodyTextMdRegular,
                color = AppTheme.palette.primary.getColor(5)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "코드 재전송",
                modifier = Modifier
                    .clickable(onClick = { viewModel.sendEffectForTimer() })
                    .padding(vertical = 8.dp),
                style = bodyTextMdBold,
                color = AppTheme.palette.primary.getColor(2)
            )
        }
    }
    return !isFocused && viewModel.checkVerifCodeRight()
}
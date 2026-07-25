package com.example.pauze.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextSmRegular
import org.intellij.lang.annotations.JdkConstants

@Composable
fun Dialog(
    title: String,
    content: String,
    btnCancel: String,
    btnContinue: String = "",
    onDismissRequest: () -> Unit,
    onContinue: () -> Unit = {},
){
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.background(
                color = AppTheme.palette.gray.getColor(8),
                shape = RoundedCornerShape(24.dp),
            )
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                style = bodyTextLgBold,
                color = AppTheme.palette.gray.getColor(2),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                content,
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(2)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = AppTheme.palette.gray.getColor(5),
                modifier = Modifier.padding(top = 12.dp,)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        btnCancel,
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = bodyTextLgBold,
                        color = AppTheme.palette.gray.getColor(2),
                        textAlign = TextAlign.Center
                    )
                }
                if(btnContinue.isNotEmpty()){
                    VerticalDivider(
                        thickness = 1.dp,
                        color = AppTheme.palette.gray.getColor(5),
                        modifier = Modifier.height(56.dp)
                    )
                    TextButton(
                        onClick = onContinue,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            btnContinue,
                            modifier = Modifier.padding(vertical = 16.dp),
                            style = bodyTextLgBold,
                            color = AppTheme.palette.gray.getColor(2),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}
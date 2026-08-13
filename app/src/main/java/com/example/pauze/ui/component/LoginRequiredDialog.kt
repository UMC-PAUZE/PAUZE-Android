package com.example.pauze.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.headingMdBold

@Composable
fun LoginRequiredDialog(
    onLoginClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            shape = RoundedCornerShape(28.dp),
            color = AppTheme.palette.gray.getColor(9),
        ) {
            Column(
                modifier = Modifier.Companion.padding(
                    start = 24.dp,
                    top = 48.dp,
                    end = 24.dp,
                    bottom = 32.dp,
                ),
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(
                        R.drawable.ic_login_required_curation,
                    ),
                    contentDescription = null,
                    modifier = Modifier.Companion.size(112.dp),
                )

                Spacer(modifier = Modifier.Companion.height(36.dp))

                Text(
                    text = "로그인하고 더 많은\n컨텐츠를 즐겨보세요",
                    style = headingMdBold,
                    color = AppTheme.palette.gray.getColor(2),
                    textAlign = TextAlign.Companion.Center,
                )

                Spacer(modifier = Modifier.Companion.height(16.dp))

                Text(
                    text = "좋아요, 저장 기능은 로그인 후 이용할 수 있어요",
                    style = bodyTextLgRegular,
                    color = AppTheme.palette.gray.getColor(4),
                    textAlign = TextAlign.Companion.Center,
                )

                Spacer(modifier = Modifier.Companion.height(36.dp))

                Button(
                    label = "로그인하고 시작하기",
                    onClick = onLoginClick,
                    modifier = Modifier.Companion.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))

                Text(
                    text = "나중에 할게요",
                    style = bodyTextLgBold,
                    color = AppTheme.palette.gray.getColor(2),
                    modifier = Modifier.Companion
                        .clickable(onClick = onDismissRequest)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                )
            }
        }
    }
}
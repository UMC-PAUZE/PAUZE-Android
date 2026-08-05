package com.example.pauze.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextMdRegular

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "원하는 소리를 검색해보세요"
) {
    var isFocused by remember { mutableStateOf(false) }
    val isActive = isFocused || query.isNotEmpty()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(312.dp)
            .height(48.dp)
            .background(
                color = if (isActive) {
                    AppTheme.palette.gray.getColor(8)
                } else {
                    AppTheme.palette.gray.getColor(9)
                },
                shape = CircleShape
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isActive) {
                        AppTheme.palette.primary.getColor(3)
                    } else {
                        AppTheme.palette.gray.getColor(8)
                    }
                ),
                shape = CircleShape
            )
            .padding(horizontal = 16.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = bodyTextMdRegular.copy(
                color = if (isActive) {
                    AppTheme.palette.gray.getColor(4)
                } else {
                    AppTheme.palette.gray.getColor(0)
                }
            ),
            cursorBrush = SolidColor(AppTheme.palette.primary.getColor(3)),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = bodyTextMdRegular,
                        color = AppTheme.palette.gray.getColor(5)
                    )
                }
                innerTextField()
            }
        )

        if (query.isNotEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.cancel_circle),
                contentDescription = "검색어 지우기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onQueryChange("") }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_search),
            contentDescription = "검색",
            tint = if (isActive) {
                AppTheme.palette.gray.getColor(4)
            } else {
                AppTheme.palette.gray.getColor(5)
            },
            modifier = Modifier.size(24.dp)
        )
    }
}

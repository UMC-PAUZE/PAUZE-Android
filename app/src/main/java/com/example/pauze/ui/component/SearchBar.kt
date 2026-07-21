package com.example.pauze.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(AppTheme.palette.gray.getColor(9), CircleShape)
            .border(BorderStroke(1.dp, AppTheme.palette.gray.getColor(8)), CircleShape)
            .padding(horizontal = 16.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = bodyTextMdRegular.copy(color = AppTheme.palette.gray.getColor(0)),
            cursorBrush = SolidColor(AppTheme.palette.gray.getColor(0)),
            singleLine = true,
            modifier = Modifier.weight(1f),
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
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_search),
            contentDescription = "검색",
            tint = AppTheme.palette.gray.getColor(5),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
private fun SearchBarPreview() {
    val query = remember { mutableStateOf("") }

    PAUZEAndroidTheme(darkTheme = true, dynamicColor = false) {
        SearchBar(
            query = query.value,
            onQueryChange = { query.value = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}

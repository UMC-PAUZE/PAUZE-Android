package com.example.pauze.ui.curation.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme

@Composable
fun CurationScrollToTopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SmallFloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
        shape = CircleShape,
        containerColor = AppTheme.palette.primary.getColor(3),
        contentColor = AppTheme.palette.gray.getColor(9),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "목록 맨 위로 이동",
            modifier = Modifier
                .size(24.dp)
                .rotate(90f),
            tint = AppTheme.palette.gray.getColor(9),
        )
    }
}

package com.example.pauze.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.model.SoundItem as SoundItemModel
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextSmRegular

/** 소리 목록과 보관함에서 공통으로 사용하는 소리 카드 아이템입니다. */
@Composable
fun SoundItem(
    sound: SoundItemModel,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val itemModifier = modifier
        .width(312.dp)
        .height(80.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(AppTheme.palette.gray.getColor(8))
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
        .padding(horizontal = 12.dp)

    Row(
        modifier = itemModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(sound.imageResId),
                contentDescription = sound.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sound.title,
                style = bodyTextLgBold,
                color = AppTheme.palette.gray.getColor(0)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sound.category,
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(5)
            )
        }

        IconButton(
            onClick = { onToggleLike(sound.id) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (sound.isLiked) R.drawable.ic_heart_on else R.drawable.ic_heart_off
                ),
                contentDescription = "좋아요",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = { onToggleBookmark(sound.id) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_download),
                contentDescription = "저장",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

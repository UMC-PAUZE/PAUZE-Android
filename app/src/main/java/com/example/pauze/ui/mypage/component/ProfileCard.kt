package com.example.pauze.ui.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pauze.R
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextXlMedium

@Composable
fun ProfileCard(
    nickname: String,
    profileImageUrl: String?,
    loginProvider: String?,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(color = AppTheme.palette.gray.getColor(8), shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = AppTheme.palette.gray.getColor(7), shape = CircleShape),
            contentAlignment = Alignment.Center
        ){
            if (profileImageUrl != null) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = "프로필 이미지",
                    tint = AppTheme.palette.gray.getColor(8),
                    modifier = Modifier.size(width = 33.dp, height = 42.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = nickname,  style = bodyTextXlMedium, color = AppTheme.palette.gray.getColor(2))
            loginProvider?.let {
                Text(text = it, style= bodyTextMdRegular, color = AppTheme.palette.gray.getColor(5))
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = "이동하기 >",
            tint = AppTheme.palette.gray.getColor(5)
        )
    }
}

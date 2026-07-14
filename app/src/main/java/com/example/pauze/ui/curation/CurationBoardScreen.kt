package com.example.pauze.ui.curation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlBold
import androidx.compose.ui.res.painterResource
import com.example.pauze.ui.theme.bodyTextLgBold
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val MINUTE_MILLIS = 60_000L
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS
private const val YEAR_MILLIS = 365 * DAY_MILLIS

private fun formatRelativeTime(
    createdAt: String,
    currentTimeMillis: Long = System.currentTimeMillis(),
): String {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault(),
    ).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone("Asia/Seoul")
    }

    val createdAtMillis = runCatching {
        dateFormat.parse(createdAt)?.time
    }.getOrNull() ?: return createdAt

    val difference = (currentTimeMillis - createdAtMillis)
        .coerceAtLeast(0L)

    return when {
        difference < MINUTE_MILLIS -> "방금 전"

        difference < HOUR_MILLIS ->
            "${difference / MINUTE_MILLIS}분 전"

        difference < DAY_MILLIS ->
            "${difference / HOUR_MILLIS}시간 전"

        difference < MONTH_MILLIS ->
            "${difference / DAY_MILLIS}일 전"

        difference < YEAR_MILLIS ->
            "${difference / MONTH_MILLIS}개월 전"

        else ->
            "${difference / YEAR_MILLIS}년 전"
    }
}

@Composable
private fun CurationPostCard(
    post: CurationPost,
    modifier: Modifier = Modifier,
    onPostClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onBookmarkClick: (Long) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.palette.gray.getColor(8))
            .clickable { onPostClick(post.postId) }
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.palette.gray.getColor(6))
                    .padding(
                        horizontal = 12.dp,
                        vertical = 4.dp,
                    ),
            ) {
                Text(
                    text = post.categoryName,
                    style = bodyTextSmMedium,
                    color = AppTheme.palette.gray.getColor(2),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "${post.readingTimeMinutes}분 읽기",
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(4),
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { onBookmarkClick(post.postId) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(
                        id = if (post.isBookmarked) {
                            R.drawable.ic_bookmark_on_curation
                        } else {
                            R.drawable.ic_bookmark_off_curation
                        }
                    ),
                    contentDescription = if (post.isBookmarked) {
                        "북마크 취소"
                    } else {
                        "북마크 추가"
                    },
                    tint = AppTheme.palette.gray.getColor(2),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppTheme.palette.gray.getColor(7)),
                contentAlignment = Alignment.Center,
            ) {
                // TODO: 이미지 로더 추가 후 thumbnailUrl 이미지 표시
            }

            Text(
                text = post.title,
                modifier = Modifier.weight(1f),
                style = bodyTextLgBold,
                color = AppTheme.palette.gray.getColor(2),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = post.summary,
            modifier = Modifier.fillMaxWidth(),
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(4),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onLikeClick(post.postId) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(
                        id = if (post.isLiked) {
                            R.drawable.ic_heart_on_curation
                        } else {
                            R.drawable.ic_heart_off_curation
                        }
                    ),
                    contentDescription = if (post.isLiked) {
                        "좋아요 취소"
                    } else {
                        "좋아요 추가"
                    },
                    tint = AppTheme.palette.secondary.getColor(3),
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = post.likeCount.toString(),
                style = bodyTextMdRegular,
                color = AppTheme.palette.secondary.getColor(3),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = formatRelativeTime(post.createdAt),
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(4),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CurationPostCardPreview() {
    PAUZEAndroidTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        Box(
            modifier = Modifier
                .background(AppTheme.palette.gray.getColor(9))
                .padding(20.dp),
        ) {
            CurationPostCard(
                post = CurationPost(
                    postId = 1L,
                    categoryId = 3L,
                    categoryName = "일상팁",
                    title = "퇴근길 감각 과부하 대처법 5가지",
                    summary = "매일 퇴근길 지하철에서 너무 지쳐서 집에 오면 아무것도 못 하겠더라고요.",
                    thumbnailUrl = null,
                    viewCount = 147,
                    likeCount = 147,
                    readingTimeMinutes = 5,
                    isLiked = false,
                    isBookmarked = false,
                    createdAt = "2026-07-02T10:30:00",
                ),
            )
        }
    }
}
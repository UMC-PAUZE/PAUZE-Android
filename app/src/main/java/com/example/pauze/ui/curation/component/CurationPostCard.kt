package com.example.pauze.ui.curation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.curation.formatRelativeTime
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextSmRegular

@Composable
fun CurationPostCard(
    post: CurationPost,
    modifier: Modifier = Modifier,
    onPostClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onBookmarkClick: (Long) -> Unit = {},
    onShareClick: (CurationPost) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onPostClick(post.postId)
            }
            .padding(vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        AppTheme.palette.gray.getColor(6),
                    )
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

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${post.readingTimeMinutes}분 읽기",
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(4),
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(
                    id = if (post.isBookmarked) {
                        R.drawable.ic_bookmark_on_curation
                    } else {
                        R.drawable.ic_bookmark_off_curation
                    },
                ),
                contentDescription = if (post.isBookmarked) {
                    "북마크 취소"
                } else {
                    "북마크 추가"
                },
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onBookmarkClick(post.postId)
                    },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = post.title,
            modifier = Modifier.fillMaxWidth(),
            style = bodyTextLgBold,
            color = AppTheme.palette.gray.getColor(2),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = post.summary,
            modifier = Modifier.fillMaxWidth(),
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(4),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(
                    id = if (post.isLiked) {
                        R.drawable.ic_heart_on_curation
                    } else {
                        R.drawable.ic_heart_off_curation
                    },
                ),
                contentDescription = if (post.isLiked) {
                    "좋아요 취소"
                } else {
                    "좋아요 추가"
                },
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onLikeClick(post.postId)
                    },
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = post.likeCount.toString(),
                style = bodyTextMdRegular,
                color = AppTheme.palette.secondary.getColor(3),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(
                    id = R.drawable.ic_share_curation,
                ),
                contentDescription = "게시글 공유",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onShareClick(post)
                    },
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = formatRelativeTime(post.createdAt),
                style = bodyTextMdRegular,
                color = AppTheme.palette.gray.getColor(4),
            )
        }
    }
}

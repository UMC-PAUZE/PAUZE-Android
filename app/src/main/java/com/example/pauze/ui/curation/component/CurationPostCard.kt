package com.example.pauze.ui.curation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.palette.gray.getColor(8))
            .clickable {
                onPostClick(post.postId)
            }
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

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "${post.readingTimeMinutes}분 읽기",
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(4),
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    onBookmarkClick(post.postId)
                },
                modifier = Modifier.size(32.dp),
            ) {
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
                    modifier = Modifier.size(20.dp),
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
                    .background(
                        AppTheme.palette.gray.getColor(7),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                // TODO: 이미지 로더 추가 후 thumbnailUrl 표시
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
            text = if (post.summary.length > 35) {
                "${post.summary.take(35)}..."
            } else {
                post.summary
            },
            modifier = Modifier.fillMaxWidth(),
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(4),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    onLikeClick(post.postId)
                },
                modifier = Modifier.size(32.dp),
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
                    modifier = Modifier.size(20.dp),
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

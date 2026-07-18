package com.example.pauze.ui.curation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.dummies.dummyCurationPosts
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextSmRegular

private val sectionTitlePattern = Regex("""^\d+\..+""")
private val paragraphSeparatorPattern = Regex("""\n\s*\n""")

@Composable
fun CurationDetailScreen(
    post: CurationPost,
    onBackClick: () -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onBookmarkClick: (Long) -> Unit = {},
    onShareClick: (Long) -> Unit = {},
) {
    var isLiked by rememberSaveable(post.postId, post.isLiked) {
        mutableStateOf(post.isLiked)
    }
    var isBookmarked by rememberSaveable(
        post.postId,
        post.isBookmarked,
    ) {
        mutableStateOf(post.isBookmarked)
    }
    var likeCount by rememberSaveable(post.postId, post.likeCount) {
        mutableIntStateOf(post.likeCount)
    }

    val paragraphs = remember(post.summary) {
        post.summary
            .split(paragraphSeparatorPattern)
            .map(String::trim)
            .filter(String::isNotEmpty)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9)),
    ) {
        TopBar(
            title = "게시글",
            onBackClick = onBackClick,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 32.dp,
            ),
        ) {
            item {
                CurationDetailHeader(post = post)
                Spacer(modifier = Modifier.height(24.dp))
            }

            itemsIndexed(
                items = paragraphs,
                key = { index, _ -> index },
            ) { index, paragraph ->
                val isSectionTitle = sectionTitlePattern.matches(
                    paragraph,
                )

                Text(
                    text = paragraph,
                    modifier = Modifier.fillMaxWidth(),
                    style = if (isSectionTitle) {
                        bodyTextLgBold
                    } else {
                        bodyTextMdRegular
                    },
                    color = AppTheme.palette.gray.getColor(2),
                )

                if (index < paragraphs.lastIndex) {
                    Spacer(
                        modifier = Modifier.height(
                            if (isSectionTitle) 12.dp else 24.dp,
                        ),
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))

                CurationDetailActions(
                    likeCount = likeCount,
                    isLiked = isLiked,
                    isBookmarked = isBookmarked,
                    onLikeClick = {
                        val willBeLiked = !isLiked
                        isLiked = willBeLiked
                        likeCount = (
                            likeCount + if (willBeLiked) 1 else -1
                            ).coerceAtLeast(0)
                        onLikeClick(post.postId)
                    },
                    onBookmarkClick = {
                        isBookmarked = !isBookmarked
                        onBookmarkClick(post.postId)
                    },
                    onShareClick = {
                        onShareClick(post.postId)
                    },
                )
            }
        }
    }
}

@Composable
private fun CurationDetailHeader(
    post: CurationPost,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
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

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${post.readingTimeMinutes}분 읽기",
                style = bodyTextSmRegular,
                color = AppTheme.palette.gray.getColor(4),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppTheme.palette.gray.getColor(7)),
                contentAlignment = Alignment.Center,
            ) {
                // TODO: 이미지 로더 추가 후 thumbnailUrl 표시
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = post.title,
                    style = bodyTextLgBold,
                    color = AppTheme.palette.gray.getColor(2),
                )

                Text(
                    text = formatRelativeTime(post.createdAt),
                    style = bodyTextMdRegular,
                    color = AppTheme.palette.gray.getColor(4),
                )
            }
        }
    }
}

@Composable
private fun CurationDetailActions(
    likeCount: Int,
    isLiked: Boolean,
    isBookmarked: Boolean,
    onLikeClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onLikeClick,
            modifier = Modifier.size(32.dp),
        ) {
            Image(
                painter = painterResource(
                    id = if (isLiked) {
                        R.drawable.ic_heart_on_curation
                    } else {
                        R.drawable.ic_heart_off_curation
                    },
                ),
                contentDescription = if (isLiked) {
                    "좋아요 취소"
                } else {
                    "좋아요 추가"
                },
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = likeCount.toString(),
            style = bodyTextMdRegular,
            color = AppTheme.palette.secondary.getColor(3),
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier.size(32.dp),
        ) {
            Image(
                painter = painterResource(
                    id = if (isBookmarked) {
                        R.drawable.ic_bookmark_on_curation
                    } else {
                        R.drawable.ic_bookmark_off_curation
                    },
                ),
                contentDescription = if (isBookmarked) {
                    "북마크 취소"
                } else {
                    "북마크 추가"
                },
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onShareClick,
            modifier = Modifier.size(40.dp),
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_share_curation,
                ),
                contentDescription = "게시글 공유",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(
    name = "큐레이션 게시글 상세",
    showBackground = true,
)
@Composable
private fun CurationDetailScreenPreview() {
    PAUZEAndroidTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        CurationDetailScreen(
            post = dummyCurationPosts.first(),
        )
    }
}

package com.example.pauze.ui.curation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
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
import com.example.pauze.ui.theme.bodyTextLgMedium
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
    onCopyLinkClick: (Long) -> Unit = {},
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
    var showShareBottomSheet by rememberSaveable {
        mutableStateOf(false)
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
                        showShareBottomSheet = true
                    },
                )
            }
        }
    }

    if (showShareBottomSheet) {
        CurationShareBottomSheet(
            onDismissRequest = {
                showShareBottomSheet = false
            },
            onCopyLinkClick = {
                onCopyLinkClick(post.postId)
            },
            onShareClick = {
                showShareBottomSheet = false
                onShareClick(post.postId)
            },
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurationShareBottomSheet(
    onDismissRequest: () -> Unit,
    onCopyLinkClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var isLinkCopied by rememberSaveable {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 48.dp,
            topEnd = 48.dp,
        ),
        containerColor = AppTheme.palette.gray.getColor(9),
        contentColor = AppTheme.palette.gray.getColor(2),
        scrimColor = Color.Black.copy(alpha = 0.72f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        bottom = 20.dp,
                    )
                    .width(56.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(AppTheme.palette.gray.getColor(4)),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 40.dp,
                    end = 40.dp,
                    bottom = 48.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CurationShareOption(
                iconRes = if (isLinkCopied) {
                    R.drawable.ic_check_curation
                } else {
                    R.drawable.ic_link_curation
                },
                text = if (isLinkCopied) {
                    "링크가 복사되었어요"
                } else {
                    "링크 복사하기"
                },
                textColor = if (isLinkCopied) {
                    AppTheme.palette.primary.getColor(4)
                } else {
                    AppTheme.palette.gray.getColor(2)
                },
                onClick = {
                    isLinkCopied = true
                    onCopyLinkClick()
                },
            )

            CurationShareOption(
                iconRes = R.drawable.ic_share_curation,
                text = "다른 앱으로 공유하기",
                textColor = AppTheme.palette.gray.getColor(2),
                onClick = onShareClick,
            )
        }
    }
}

@Composable
private fun CurationShareOption(
    iconRes: Int,
    text: String,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = bodyTextLgMedium,
            color = textColor,
        )
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

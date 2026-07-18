package com.example.pauze.ui.curation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pauze.R
import com.example.pauze.data.dummies.curationCategories
import com.example.pauze.data.model.CurationCategory
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmMedium
import com.example.pauze.ui.theme.bodyTextSmRegular
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun CurationBoardScreen(
    onPostClick: (Long) -> Unit = {},
    onBookmarkListClick: () -> Unit = {},
    viewModel: CurationBoardViewModel = viewModel(),
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 0
        }
    }

    val selectedPost = viewModel.selectedPost

    BackHandler(enabled = selectedPost != null) {
        viewModel.clearSelectedPost()
    }

    if (selectedPost != null) {
        CurationDetailScreen(
            post = selectedPost,
            onBackClick = viewModel::clearSelectedPost,
            onLikeClick = viewModel::toggleLike,
            onBookmarkClick = viewModel::toggleBookmark,
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.palette.gray.getColor(9)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopBar(
                title = "발견",
                showBackButton = false,
                rightIcon = {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_bookmark_off_curation,
                        ),
                        contentDescription = "북마크 목록",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onBookmarkListClick()
                            },
                    )
                },
            )

            CurationSearchFilter(
                keyword = viewModel.keyword,
                categories = curationCategories,
                selectedCategoryId = viewModel.selectedCategoryId,
                onKeywordChange = viewModel::updateKeyword,
                onSearch = viewModel::search,
                onCategorySelected = viewModel::selectCategory,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 16.dp,
                ),
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 88.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = viewModel.filteredPosts,
                    key = { post ->
                        post.postId
                    },
                ) { post ->
                    CurationPostCard(
                        post = post,
                        onPostClick = { postId ->
                            viewModel.selectPost(postId)
                            onPostClick(postId)
                        },
                        onLikeClick = viewModel::toggleLike,
                        onBookmarkClick = viewModel::toggleBookmark,
                    )
                }
            }
        }

        if (showScrollToTopButton) {
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 20.dp,
                        bottom = 20.dp,
                    )
                    .size(48.dp),
                shape = CircleShape,
                containerColor =
                    AppTheme.palette.primary.getColor(3),
                contentColor =
                    AppTheme.palette.gray.getColor(9),
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_arrow_back,
                    ),
                    contentDescription = "목록 맨 위로 이동",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(90f),
                    tint = AppTheme.palette.gray.getColor(9),
                )
            }
        }
    }
}

@Composable
private fun CurationSearchFilter(
    keyword: String,
    categories: List<CurationCategory>,
    selectedCategoryId: Long?,
    onKeywordChange: (String) -> Unit,
    onSearch: () -> Unit,
    onCategorySelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        BasicTextField(
            value = keyword,
            onValueChange = onKeywordChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = bodyTextLgRegular.copy(
                color = AppTheme.palette.gray.getColor(2),
            ),
            cursorBrush = SolidColor(
                AppTheme.palette.primary.getColor(4),
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearch()
                },
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            AppTheme.palette.gray.getColor(8),
                        )
                        .padding(
                            start = 16.dp,
                            end = 4.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (keyword.isEmpty()) {
                            Text(
                                text = "검색어를 입력해주세요",
                                style = bodyTextMdMedium,
                                color = AppTheme.palette.gray.getColor(6),
                            )
                        }

                        innerTextField()
                    }

                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            onSearch()
                        },
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_search_curation,
                            ),
                            contentDescription = "검색",
                            modifier = Modifier.size(24.dp),
                            tint = AppTheme.palette.gray.getColor(5),
                        )
                    }
                }
            },
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                categories.forEach { category ->
                    CurationCategoryChip(
                        category = category,
                        isSelected = (
                                selectedCategoryId ==
                                        category.categoryId
                                ),
                        onClick = onCategorySelected,
                    )
                }
            }
        }
    }
}

@Composable
private fun CurationCategoryChip(
    category: CurationCategory,
    isSelected: Boolean,
    onClick: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = {
            onClick(category.categoryId)
        },
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        color = if (isSelected) {
            AppTheme.palette.primary.getColor(4)
        } else {
            Color.Transparent
        },
        border = if (isSelected) {
            null
        } else {
            BorderStroke(
                width = 1.5.dp,
                color = AppTheme.palette.gray.getColor(7),
            )
        },
    ) {
        Text(
            text = category.categoryName,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp,
            ),
            style = bodyTextMdMedium,
            color = if (isSelected) {
                AppTheme.palette.gray.getColor(9)
            } else {
                AppTheme.palette.gray.getColor(4)
            },
        )
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

private const val MINUTE_MILLIS = 60_000L
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS
private const val MONTH_MILLIS = 30 * DAY_MILLIS
private const val YEAR_MILLIS = 365 * DAY_MILLIS

internal fun formatRelativeTime(
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

    val difference = (
            currentTimeMillis - createdAtMillis
            ).coerceAtLeast(0L)

    return when {
        difference < MINUTE_MILLIS ->
            "방금 전"

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

@Preview(
    name = "큐레이션 게시판",
    showBackground = true,
)
@Composable
private fun CurationBoardScreenPreview() {
    PAUZEAndroidTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        CurationBoardScreen()
    }
}

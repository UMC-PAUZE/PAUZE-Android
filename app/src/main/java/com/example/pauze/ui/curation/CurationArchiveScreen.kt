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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.curation.component.CurationPostCard
import com.example.pauze.ui.curation.component.CurationScrollToTopButton
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgBold
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextSmRegular
import kotlinx.coroutines.launch

enum class CurationArchiveTab {
    LIKES,
    BOOKMARKS,
}

@Composable
fun CurationArchiveScreen(
    likedPosts: List<CurationPost>,
    bookmarkedPosts: List<CurationPost>,
    isLikesLoading: Boolean = false,
    isBookmarksLoading: Boolean = false,
    hasNextLikesPage: Boolean = false,
    hasNextBookmarksPage: Boolean = false,
    onLoadMoreLikes: () -> Unit = {},
    onLoadMoreBookmarks: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onPostClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onBookmarkClick: (Long) -> Unit = {},
    onShareClick: (CurationPost) -> Unit = {},
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(CurationArchiveTab.BOOKMARKS)
    }

    // 검색 API에 keyword가 추가되면 ViewModel 상태로 이동
    var searchKeyword by rememberSaveable {
        mutableStateOf("")
    }

    val posts = when (selectedTab) {
        CurationArchiveTab.LIKES -> likedPosts
        CurationArchiveTab.BOOKMARKS -> bookmarkedPosts
    }

    val isLoading = when (selectedTab) {
        CurationArchiveTab.LIKES -> isLikesLoading
        CurationArchiveTab.BOOKMARKS -> isBookmarksLoading
    }

    val hasNextPage = when (selectedTab) {
        CurationArchiveTab.LIKES -> hasNextLikesPage
        CurationArchiveTab.BOOKMARKS -> hasNextBookmarksPage
    }

    val onLoadMore = when (selectedTab) {
        CurationArchiveTab.LIKES -> onLoadMoreLikes
        CurationArchiveTab.BOOKMARKS -> onLoadMoreBookmarks
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 0
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo

            val lastVisibleItemIndex =
                layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index
                    ?: return@derivedStateOf false

            layoutInfo.totalItemsCount > 0 &&
                    lastVisibleItemIndex >=
                    layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(selectedTab) {
        if (listState.layoutInfo.totalItemsCount > 0) {
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(
        selectedTab,
        shouldLoadMore,
        posts.size,
        isLoading,
        hasNextPage,
    ) {
        if (
            shouldLoadMore &&
            !isLoading &&
            hasNextPage
        ) {
            onLoadMore()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AppTheme.palette.gray.getColor(9),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopBar(
                title = "게시글 보관함",
                onBackClick = onBackClick,
            )

            CurationArchiveSearchBar(
                query = searchKeyword,
                onQueryChange = { keyword ->
                    searchKeyword = keyword
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 16.dp,
                        end = 20.dp,
                    ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 8.dp,
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurationArchiveTabButton(
                    text = "좋아요",
                    selected =
                        selectedTab == CurationArchiveTab.LIKES,
                    iconRes =
                        R.drawable.ic_heart_off_curation,
                    onClick = {
                        selectedTab = CurationArchiveTab.LIKES
                    },
                    modifier = Modifier.weight(1f),
                )

                CurationArchiveTabButton(
                    text = "북마크",
                    selected =
                        selectedTab == CurationArchiveTab.BOOKMARKS,
                    iconRes =
                        R.drawable.ic_bookmark_off_curation,
                    onClick = {
                        selectedTab =
                            CurationArchiveTab.BOOKMARKS
                    },
                    modifier = Modifier.weight(1f),
                )
            }

            when {
                isLoading && posts.isEmpty() -> {
                    CurationArchiveLoadingContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                }

                posts.isEmpty() -> {
                    CurationArchiveEmptyContent(
                        selectedTab = selectedTab,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                }

                else -> {
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
                    ) {
                        itemsIndexed(
                            items = posts,
                            key = { _, post ->
                                "${selectedTab.name}_${post.postId}"
                            },
                        ) { index, post ->
                            CurationPostCard(
                                post = post,
                                onPostClick = onPostClick,
                                onLikeClick = onLikeClick,
                                onBookmarkClick = onBookmarkClick,
                                onShareClick = onShareClick,
                            )

                            if (index < posts.lastIndex) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = AppTheme.palette.gray
                                        .getColor(8),
                                )
                            }
                        }

                        if (isLoading) {
                            item(
                                key = "${selectedTab.name}_loading",
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment =
                                        Alignment.Center,
                                ) {
                                    CircularProgressIndicator(
                                        modifier =
                                            Modifier.size(28.dp),
                                        color =
                                            AppTheme.palette.primary
                                                .getColor(4),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showScrollToTopButton && posts.isNotEmpty()) {
            CurationScrollToTopButton(
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
                    ),
            )
        }
    }
}

@Composable
private fun CurationArchiveSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        textStyle = bodyTextMdMedium.copy(
            color = AppTheme.palette.gray.getColor(2),
        ),
        cursorBrush = SolidColor(
            AppTheme.palette.primary.getColor(4),
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = AppTheme.palette.gray.getColor(8),
                        shape = CircleShape,
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "북마크한 글을 검색해보세요",
                            style = bodyTextMdMedium,
                            color = AppTheme.palette.gray.getColor(6),
                        )
                    }

                    innerTextField()
                }

                Icon(
                    painter = painterResource(
                        R.drawable.ic_search_curation,
                    ),
                    contentDescription = "검색",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified,
                )
            }
        },
    )
}

@Composable
private fun CurationArchiveTabButton(
    text: String,
    selected: Boolean,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (selected) {
        AppTheme.palette.gray.getColor(2)
    } else {
        AppTheme.palette.gray.getColor(4)
    }

    Box(
        modifier = modifier
            .height(40.dp)
            .background(
                color = if (selected) {
                    AppTheme.palette.gray.getColor(7)
                } else {
                    Color.Transparent
                },
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = contentColor,
            )

            Text(
                text = text,
                style = bodyTextLgBold,
                color = contentColor,
            )
        }
    }
}

@Composable
private fun CurationArchiveLoadingContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = AppTheme.palette.primary.getColor(4),
        )
    }
}

@Composable
private fun CurationArchiveEmptyContent(
    selectedTab: CurationArchiveTab,
    modifier: Modifier = Modifier,
) {
    val title = when (selectedTab) {
        CurationArchiveTab.LIKES ->
            "좋아요한 글이 없어요"

        CurationArchiveTab.BOOKMARKS ->
            "북마크한 글이 없어요"
    }

    val description = when (selectedTab) {
        CurationArchiveTab.LIKES ->
            "발견 탭에서 마음에 드는 글에 좋아요를 눌러보세요."

        CurationArchiveTab.BOOKMARKS ->
            "발견 탭에서 마음에 드는 글을 북마크해보세요."
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(
                R.drawable.ic_empty_curation,
            ),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = bodyTextLgBold,
            color = AppTheme.palette.gray.getColor(2),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
        )
    }
}


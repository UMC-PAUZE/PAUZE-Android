package com.example.pauze.ui.curation

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.pauze.ui.theme.bodyTextSmRegular
import kotlinx.coroutines.launch

@Composable
fun CurationBookmarkScreen(
    bookmarkedPosts: List<CurationPost>,
    isLoading: Boolean = false,
    hasNextPage: Boolean = false,
    onLoadMore: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onPostClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onBookmarkClick: (Long) -> Unit = {},
) {
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
                layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: return@derivedStateOf false

            layoutInfo.totalItemsCount > 0 &&
                lastVisibleItemIndex >=
                layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(
        shouldLoadMore,
        bookmarkedPosts.size,
        isLoading,
        hasNextPage,
    ) {
        if (shouldLoadMore && !isLoading && hasNextPage) {
            onLoadMore()
        }
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
                title = "북마크",
                onBackClick = onBackClick,
            )

            if (isLoading && bookmarkedPosts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.palette.primary.getColor(4),
                    )
                }
            } else if (bookmarkedPosts.isEmpty()) {
                CurationBookmarkEmptyContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 16.dp,
                        end = 20.dp,
                        bottom = 88.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = bookmarkedPosts,
                        key = { post ->
                            post.postId
                        },
                    ) { post ->
                        CurationPostCard(
                            post = post,
                            onPostClick = onPostClick,
                            onLikeClick = onLikeClick,
                            onBookmarkClick = onBookmarkClick,
                        )
                    }

                    if (isLoading) {
                        item(key = "bookmarks_loading") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    color = AppTheme.palette.primary
                                        .getColor(4),
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showScrollToTopButton) {
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
private fun CurationBookmarkEmptyContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_empty_curation),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "북마크한 글이 없어요",
            style = bodyTextLgBold,
            color = AppTheme.palette.gray.getColor(2),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "발견 탭에서 마음에 드는 글을 북마크해보세요.",
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
        )
    }
}

@Preview(
    name = "북마크 없음",
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
)
@Composable
private fun CurationBookmarkEmptyPreview() {
    PAUZEAndroidTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        CurationBookmarkScreen(
            bookmarkedPosts = emptyList(),
        )
    }
}

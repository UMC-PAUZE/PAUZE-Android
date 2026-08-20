package com.example.pauze.ui.curation

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pauze.R
import com.example.pauze.data.dummies.curationCategories
import com.example.pauze.data.model.CurationCategory
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.component.Chips
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.component.LoginRequiredDialog
import com.example.pauze.ui.curation.component.CurationPostCard
import com.example.pauze.ui.curation.component.CurationScrollToTopButton
import com.example.pauze.ui.login.LoginActivity
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.bodyTextLgRegular
import com.example.pauze.ui.theme.bodyTextMdMedium
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.headingSmBold
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun CurationBoardScreen(
    onPostClick: (Long) -> Unit = {},
    onArchiveClick: () -> Unit = {},
    deepLinkPostId: Long? = null,
    onDeepLinkConsumed: () -> Unit = {},
    viewModel: CurationBoardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val curationState = uiState.data

    val activity = LocalActivity.current

    var isLoginRequiredDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isArchiveScreenVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var sharingPost by remember {
        mutableStateOf<CurationPost?>(null)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CurationEffect.NavigateToLogin -> {
                    isLoginRequiredDialogVisible = true
                }

                CurationEffect.OpenArchive -> {
                    isArchiveScreenVisible = true
                    onArchiveClick()
                }
            }
        }
    }

    LaunchedEffect(isArchiveScreenVisible) {
        if (isArchiveScreenVisible) {
            viewModel.loadArchive()
        }
    }

    // 전달받은 ID는 한 번만 소비해 재구성이나 뒤로 가기 이후 상세 화면이 다시 열리지 않게 한다.
    LaunchedEffect(deepLinkPostId) {
        if (deepLinkPostId != null) {
            viewModel.selectPostFromDeepLink(
                deepLink = deepLinkPostId.toString(),
                postId = deepLinkPostId,
            )
            onDeepLinkConsumed()
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 0
        }
    }

    val selectedPost = curationState.selectedPost
    val posts = curationState.posts
    val hasActiveFilter =
        curationState.submittedKeyword.isNotBlank() ||
                curationState.selectedCategoryId != null

    val shouldLoadMorePosts by remember {
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
        shouldLoadMorePosts,
        curationState.postsPage,
        curationState.postsTotalPages,
        curationState.isPostsLoading,
    ) {
        // 레이아웃 갱신 중 조건이 반복 평가돼도 ViewModel의 로딩·페이지 검사와 함께 중복 요청을 막는다.
        if (
            shouldLoadMorePosts &&
            selectedPost == null &&
            !isArchiveScreenVisible &&
            !curationState.isPostsLoading &&
            curationState.hasNextPostsPage
        ) {
            viewModel.loadNextCurationPosts()
        }
    }

    BackHandler(
        enabled = selectedPost != null,
    ) {
        viewModel.clearSelectedPost()
    }

    BackHandler(
        enabled = (
                isArchiveScreenVisible &&
                        selectedPost == null
                ),
    ) {
        isArchiveScreenVisible = false
    }

    if (isLoginRequiredDialogVisible) {
        LoginRequiredDialog(
            onLoginClick = {
                isLoginRequiredDialogVisible = false

                activity?.startActivity(
                    Intent(
                        activity,
                        LoginActivity::class.java,
                    ),
                )
            },
            onDismissRequest = {
                isLoginRequiredDialogVisible = false
            },
        )
    }

    sharingPost?.let { post ->
        val shareUrl = createCurationShareUrl(post.postId)

        CurationShareBottomSheet(
            onDismissRequest = {
                sharingPost = null
            },
            onCopyLinkClick = {
                activity?.let { context ->
                    copyCurationLink(
                        context = context,
                        shareUrl = shareUrl,
                    )
                }
            },
            onShareClick = {
                sharingPost = null
                activity?.let { context ->
                    shareCurationPost(
                        context = context,
                        post = post,
                        shareUrl = shareUrl,
                    )
                }
            },
        )
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

    if (isArchiveScreenVisible) {
        CurationArchiveScreen(
            likedPosts =
                curationState.likedPosts,
            bookmarkedPosts =
                curationState.bookmarkedPosts,
            searchKeyword =
                curationState.archiveKeyword,
            submittedSearchKeyword =
                curationState.submittedArchiveKeyword,
            isLikesLoading =
                curationState.isLikesLoading,
            isBookmarksLoading =
                curationState.isBookmarksLoading,
            hasNextLikesPage =
                curationState.hasNextLikesPage,
            hasNextBookmarksPage =
                curationState.hasNextBookmarksPage,
            onLoadMoreLikes =
                viewModel::loadNextMyLikes,
            onLoadMoreBookmarks =
                viewModel::loadNextMyBookmarks,
            onSearchKeywordChange =
                viewModel::updateArchiveKeyword,
            onSearch = {
                viewModel.searchArchive()
            },
            onBackClick = {
                isArchiveScreenVisible = false
            },
            onPostClick = { postId ->
                viewModel.selectPost(postId)
                onPostClick(postId)
            },
            onLikeClick = viewModel::toggleLike,
            onBookmarkClick =
                viewModel::toggleBookmark,
            onShareClick = { post ->
                sharingPost = post
            },
        )
        return
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
                title = "발견",
                showBackButton = false,
                rightIcon = {
                    Image(
                        painter = painterResource(
                            id = R.drawable
                                .ic_curation_box,
                        ),
                        contentDescription = "게시글 보관함",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                viewModel.openArchive()
                            },
                    )
                },
            )

            CurationSearchFilter(
                keyword = curationState.keyword,
                categories = curationCategories,
                selectedCategoryId =
                    curationState.selectedCategoryId,
                onKeywordChange =
                    viewModel::updateKeyword,
                onSearch = viewModel::search,
                onCategorySelected =
                    viewModel::selectCategory,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 16.dp,
                ),
            )

            if (
                curationState.isPostsLoading &&
                curationState.posts.isEmpty()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color =
                            AppTheme.palette.primary.getColor(4),
                    )
                }
            } else if (posts.isEmpty() && hasActiveFilter) {
                CurationEmptySearchResult(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            } else if (posts.isEmpty()) {
                CurationEmptyBoard(
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
                        end = 20.dp,
                        bottom = 88.dp,
                    ),
                ) {
                    itemsIndexed(
                        items = posts,
                        key = { _, post ->
                            post.postId
                        },
                    ) { index, post ->
                        CurationPostCard(
                            post = post,
                            onPostClick = { postId ->
                                viewModel.selectPost(
                                    postId,
                                )
                                onPostClick(postId)
                            },
                            onLikeClick =
                                viewModel::toggleLike,
                            onBookmarkClick =
                                viewModel::toggleBookmark,
                            onShareClick = { post ->
                                sharingPost = post
                            },
                        )

                        if (index < posts.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = AppTheme.palette.gray
                                    .getColor(8),
                            )
                        }
                    }

                    if (curationState.isPostsLoading) {
                        item(key = "curation_posts_loading") {
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
private fun CurationEmptyBoard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(
                R.drawable.ic_empty_curation,
            ),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )

        Spacer(
            modifier = Modifier.height(16.dp),
        )

        Text(
            text = "아직 등록된 게시글이 없어요",
            style = headingSmBold,
            color = AppTheme.palette.gray.getColor(2),
        )

        Spacer(
            modifier = Modifier.height(8.dp),
        )

        Text(
            text = "새로운 글이 등록되면 이곳에서 확인할 수 있어요.",
            style = bodyTextSmRegular,
            color = AppTheme.palette.gray.getColor(4),
        )
    }
}

@Composable
private fun CurationEmptySearchResult(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(
                R.drawable.ic_empty_curation,
            ),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )

        Spacer(
            modifier = Modifier.height(16.dp),
        )

        Text(
            text = "검색 결과가 없어요",
            style = headingSmBold,
            color = AppTheme.palette.gray.getColor(2),
        )
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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BasicTextField(
            value = keyword,
            onValueChange = onKeywordChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = bodyTextLgRegular.copy(
                color =
                    AppTheme.palette.gray.getColor(2),
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
                        .clip(
                            RoundedCornerShape(24.dp),
                        )
                        .background(
                            AppTheme.palette.gray
                                .getColor(8),
                        )
                        .padding(
                            start = 16.dp,
                            end = 4.dp,
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment =
                            Alignment.CenterStart,
                    ) {
                        if (keyword.isEmpty()) {
                            Text(
                                text =
                                    "검색어를 입력해주세요",
                                style =
                                    bodyTextMdMedium,
                                color =
                                    AppTheme.palette.gray
                                        .getColor(6),
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
                                id = R.drawable
                                    .ic_search_curation,
                            ),
                            contentDescription = "검색",
                            modifier =
                                Modifier.size(24.dp),
                            tint =
                                AppTheme.palette.gray
                                    .getColor(5),
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp),
                verticalAlignment =
                    Alignment.CenterVertically,
            ) {
                categories.forEach { category ->
                    val chipWidth =
                        if (category.categoryName.length <= 2) {
                            50.dp
                        } else {
                            64.dp
                        }

                    Chips(
                        text = category.categoryName,
                        isSelected = (
                                selectedCategoryId ==
                                        category.categoryId
                                ),
                        onClick = {
                            onCategorySelected(
                                category.categoryId,
                            )
                        },
                        modifier = Modifier
                            .padding(top = 12.dp, start = 2.dp, end = 2.dp, bottom = 8.dp)
                            .size(
                            width = chipWidth,
                            height = 34.dp,
                        ),
                    )
                }
            }
        }
    }
}

private const val MINUTE_MILLIS = 60_000L
private const val HOUR_MILLIS =
    60 * MINUTE_MILLIS
private const val DAY_MILLIS =
    24 * HOUR_MILLIS
private const val MONTH_MILLIS =
    30 * DAY_MILLIS
private const val YEAR_MILLIS =
    365 * DAY_MILLIS

internal fun formatRelativeTime(
    createdAt: String,
    currentTimeMillis: Long =
        System.currentTimeMillis(),
): String {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault(),
    ).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone(
            "Asia/Seoul",
        )
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

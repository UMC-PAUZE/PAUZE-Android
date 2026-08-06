package com.example.pauze.ui.curation

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.CurationPost
import com.example.pauze.data.model.toCurationPost
import com.example.pauze.data.repository.CurationRepository
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface CurationEffect {
    object NavigateToLogin : CurationEffect
    object OpenBookmarkList : CurationEffect
}

data class CurationBoardState(
    val keyword: String = "",
    val submittedKeyword: String = "",
    val selectedCategoryId: Long? = null,
    val posts: List<CurationPost> = emptyList(),
    val postsPage: Int = 0,
    val postsTotalPages: Int = 1,
    val isPostsLoading: Boolean = false,
    val bookmarkedPosts: List<CurationPost> = emptyList(),
    val bookmarksPage: Int = 0,
    val bookmarksTotalPages: Int = 1,
    val isBookmarksLoading: Boolean = false,
    val selectedPostId: Long? = null,
) {
    val filteredPosts: List<CurationPost>
        get() = posts.filter { post ->
            val matchesCategory =
                selectedCategoryId == null ||
                        post.categoryId == selectedCategoryId

            val matchesKeyword =
                submittedKeyword.isBlank() ||
                        post.title.contains(
                            submittedKeyword,
                            ignoreCase = true,
                        ) ||
                        post.summary.contains(
                            submittedKeyword,
                            ignoreCase = true,
                        )

            matchesCategory && matchesKeyword
        }

    val selectedPost: CurationPost?
        get() = posts.firstOrNull { it.postId == selectedPostId }
            ?: bookmarkedPosts.firstOrNull {
                it.postId == selectedPostId
            }

    val hasNextPostsPage: Boolean
        get() = postsPage < postsTotalPages

    val hasNextBookmarksPage: Boolean
        get() = bookmarksPage < bookmarksTotalPages
}

@HiltViewModel
class CurationBoardViewModel @Inject constructor(
    private val curationRepository: CurationRepository,
) : BaseViewModel<CurationEffect, Unit>(
    uiState = BaseUiState(data = Unit),
) {
    private val _curationState = MutableStateFlow(
        CurationBoardState(),
    )
    val curationState = _curationState.asStateFlow()

    private var handledDeepLink: String? = null
    private var postsRequestVersion: Int = 0
    private var bookmarksRequestVersion: Int = 0

    init {
        loadCurationPosts()
    }

    fun loadCurationPosts(
        categoryId: Long? = _curationState.value.selectedCategoryId,
        keyword: String? = _curationState.value.submittedKeyword,
        page: Int = 1,
        size: Int = 10,
    ) {
        val currentState = _curationState.value

        if (
            page > 1 &&
            (
                currentState.isPostsLoading ||
                    page > currentState.postsTotalPages
                )
        ) {
            return
        }

        val requestVersion = if (page == 1) {
            ++postsRequestVersion
        } else {
            postsRequestVersion
        }

        _curationState.update { state ->
            state.copy(
                posts = if (page == 1) {
                    emptyList()
                } else {
                    state.posts
                },
                postsPage = if (page == 1) 0 else state.postsPage,
                postsTotalPages = if (page == 1) {
                    1
                } else {
                    state.postsTotalPages
                },
                isPostsLoading = true,
            )
        }

        launch {
            try {
                val result = curationRepository.getCurationPosts(
                    categoryId = categoryId,
                    keyword = keyword.takeIf { !it.isNullOrBlank() },
                    page = page,
                    size = size,
                )

                if (requestVersion != postsRequestVersion) {
                    return@launch
                }

                val posts = result.content.map { item ->
                    item.toCurationPost()
                }

                _curationState.update { state ->
                    val updatedPosts = if (page == 1) {
                        posts
                    } else {
                        (state.posts + posts).distinctBy {
                            it.postId
                        }
                    }

                    state.copy(
                        posts = updatedPosts,
                        postsPage = result.page,
                        postsTotalPages = result.totalPages,
                    )
                }
            } finally {
                if (requestVersion == postsRequestVersion) {
                    _curationState.update { state ->
                        state.copy(isPostsLoading = false)
                    }
                }
            }
        }
    }

    fun loadNextCurationPosts() {
        val state = _curationState.value

        if (state.isPostsLoading || !state.hasNextPostsPage) {
            return
        }

        loadCurationPosts(
            categoryId = state.selectedCategoryId,
            keyword = state.submittedKeyword,
            page = state.postsPage + 1,
        )
    }

    fun updateKeyword(keyword: String) {
        _curationState.update { state ->
            state.copy(keyword = keyword)
        }
    }

    fun search() {
        val submittedKeyword =
            _curationState.value.keyword.trim()

        _curationState.update { state ->
            state.copy(
                submittedKeyword = submittedKeyword,
            )
        }

        loadCurationPosts(
            categoryId =
                _curationState.value.selectedCategoryId,
            keyword = submittedKeyword,
        )
    }

    fun selectCategory(categoryId: Long?) {
        _curationState.update { state ->
            state.copy(
                selectedCategoryId = categoryId,
            )
        }

        loadCurationPosts(
            categoryId = categoryId,
            keyword =
                _curationState.value.submittedKeyword,
        )
    }

    fun selectPost(postId: Long) {
        _curationState.update { state ->
            state.copy(selectedPostId = postId)
        }

        loadCurationPostDetail(postId)
    }

    private fun loadCurationPostDetail(
        postId: Long,
    ) {
        launch {
            val detail =
                curationRepository.getCurationPostDetail(
                    postId = postId,
                )

            _curationState.update { state ->
                val existingPost = state.posts.firstOrNull {
                    post -> post.postId == postId
                } ?: state.bookmarkedPosts.firstOrNull {
                    post -> post.postId == postId
                }

                val detailPost = detail.toCurationPost(
                    summary = existingPost?.summary
                        ?: detail.content,
                )

                val updatedPosts =
                    if (state.posts.none { post ->
                            post.postId == postId
                        }
                    ) {
                        state.posts + detailPost
                    } else {
                        state.posts.map { post ->
                            if (post.postId == postId) {
                                detailPost
                            } else {
                                post
                            }
                        }
                    }

                state.copy(
                    posts = updatedPosts,
                    bookmarkedPosts =
                        state.bookmarkedPosts.map { post ->
                            if (post.postId == postId) {
                                detailPost
                            } else {
                                post
                            }
                        },
                    selectedPostId = postId,
                )
            }
        }
    }

    fun selectPostFromDeepLink(
        deepLink: String,
        postId: Long,
    ) {
        if (handledDeepLink == deepLink) return

        handledDeepLink = deepLink
        selectPost(postId)
    }

    fun clearSelectedPost() {
        handledDeepLink = null

        _curationState.update { state ->
            state.copy(selectedPostId = null)
        }
    }

    fun openBookmarkList() {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        sendEffect(CurationEffect.OpenBookmarkList)
        loadMyBookmarks()
    }

    fun loadMyBookmarks(
        page: Int = 1,
        size: Int = 10,
    ) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        val currentState = _curationState.value

        if (
            page > 1 &&
            (
                currentState.isBookmarksLoading ||
                    page > currentState.bookmarksTotalPages
                )
        ) {
            return
        }

        val requestVersion = if (page == 1) {
            ++bookmarksRequestVersion
        } else {
            bookmarksRequestVersion
        }

        _curationState.update { state ->
            state.copy(
                bookmarkedPosts = if (page == 1) {
                    emptyList()
                } else {
                    state.bookmarkedPosts
                },
                bookmarksPage = if (page == 1) {
                    0
                } else {
                    state.bookmarksPage
                },
                bookmarksTotalPages = if (page == 1) {
                    1
                } else {
                    state.bookmarksTotalPages
                },
                isBookmarksLoading = true,
            )
        }

        launch {
            try {
                val result = curationRepository.getMyBookmarks(
                    page = page,
                    size = size,
                )

                if (requestVersion != bookmarksRequestVersion) {
                    return@launch
                }

                _curationState.update { state ->
                    val loadedBookmarks = result.content.map { item ->
                        val bookmarkedPost = item.toCurationPost()
                        val existingPost = state.posts.firstOrNull {
                            it.postId == bookmarkedPost.postId
                        }

                        if (existingPost == null) {
                            bookmarkedPost
                        } else {
                            bookmarkedPost.copy(
                                content = existingPost.content,
                                viewCount = existingPost.viewCount,
                            )
                        }
                    }

                    val bookmarkedPosts = if (page == 1) {
                        loadedBookmarks
                    } else {
                        (state.bookmarkedPosts + loadedBookmarks)
                            .distinctBy { it.postId }
                    }

                    val bookmarksByPostId = bookmarkedPosts.associateBy {
                        it.postId
                    }

                    state.copy(
                        posts = state.posts.map { post ->
                            val bookmarkPost = bookmarksByPostId[post.postId]

                            if (bookmarkPost == null) {
                                post
                            } else {
                                post.copy(
                                    likeCount = bookmarkPost.likeCount,
                                    isLiked = bookmarkPost.isLiked,
                                    isBookmarked = true,
                                )
                            }
                        },
                        bookmarkedPosts = bookmarkedPosts,
                        bookmarksPage = result.page,
                        bookmarksTotalPages = result.totalPages,
                    )
                }
            } finally {
                if (requestVersion == bookmarksRequestVersion) {
                    _curationState.update { state ->
                        state.copy(isBookmarksLoading = false)
                    }
                }
            }
        }
    }

    fun loadNextMyBookmarks() {
        val state = _curationState.value

        if (
            state.isBookmarksLoading ||
            !state.hasNextBookmarksPage
        ) {
            return
        }

        loadMyBookmarks(
            page = state.bookmarksPage + 1,
        )
    }

    fun toggleLike(postId: Long) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        launch {
            val result =
                curationRepository.toggleCurationPostLike(
                    postId = postId,
                )

            _curationState.update { state ->
                val updatePost: (CurationPost) -> CurationPost = { post ->
                    if (post.postId == result.postId) {
                        val likeCountChange = when {
                            post.isLiked == result.liked -> 0
                            result.liked -> 1
                            else -> -1
                        }

                        post.copy(
                            isLiked = result.liked,
                            likeCount = (
                                post.likeCount + likeCountChange
                            ).coerceAtLeast(0),
                        )
                    } else {
                        post
                    }
                }

                state.copy(
                    posts = state.posts.map(updatePost),
                    bookmarkedPosts =
                        state.bookmarkedPosts.map(updatePost),
                )
            }
        }
    }

    fun toggleBookmark(postId: Long) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        launch {
            val result =
                curationRepository.toggleCurationPostBookmark(
                    postId = postId,
                )

            _curationState.update { state ->
                val currentPost = state.posts.firstOrNull {
                    it.postId == result.postId
                } ?: state.bookmarkedPosts.firstOrNull {
                    it.postId == result.postId
                }

                val updatedBookmarkedPosts = if (result.bookmarked) {
                    if (state.bookmarkedPosts.any {
                            it.postId == result.postId
                        }
                    ) {
                        state.bookmarkedPosts.map { post ->
                            if (post.postId == result.postId) {
                                post.copy(isBookmarked = true)
                            } else {
                                post
                            }
                        }
                    } else {
                        currentPost?.let { post ->
                            state.bookmarkedPosts +
                                post.copy(isBookmarked = true)
                        } ?: state.bookmarkedPosts
                    }
                } else {
                    state.bookmarkedPosts.filterNot { post ->
                        post.postId == result.postId
                    }
                }

                state.copy(
                    posts = state.posts.map { post ->
                        if (post.postId == result.postId) {
                            post.copy(
                                isBookmarked = result.bookmarked,
                            )
                        } else {
                            post
                        }
                    },
                    bookmarkedPosts = updatedBookmarkedPosts,
                )
            }
        }
    }

    fun retry() {
        loadCurationPosts()
    }
}

package com.example.pauze.ui.curation

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.CurationBoardState
import com.example.pauze.data.model.CurationPost
import com.example.pauze.data.model.toCurationPost
import com.example.pauze.data.repository.CurationRepository
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface CurationEffect {
    object NavigateToLogin : CurationEffect
    object OpenBookmarkList : CurationEffect
}

@HiltViewModel
class CurationBoardViewModel @Inject constructor(
    private val curationRepository: CurationRepository,
) : BaseViewModel<CurationEffect, CurationBoardState>(
    uiState = BaseUiState(data = CurationBoardState()),
) {
    private var handledDeepLink: String? = null
    private var postsRequestVersion: Int = 0
    private var likesRequestVersion: Int = 0
    private var bookmarksRequestVersion: Int = 0

    init {
        loadCurationPosts()
    }

    fun loadCurationPosts(
        categoryId: Long? = uiState.value.data.selectedCategoryId,
        keyword: String? = uiState.value.data.submittedKeyword,
        page: Int = 1,
        size: Int = 10,
    ) {
        val currentState = uiState.value.data

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

        updateData { state ->
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

                updateData { state ->
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
                    updateData { state ->
                        state.copy(isPostsLoading = false)
                    }
                }
            }
        }
    }

    fun loadNextCurationPosts() {
        val state = uiState.value.data

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
        updateData { state ->
            state.copy(keyword = keyword)
        }
    }

    fun search() {
        val submittedKeyword =
            uiState.value.data.keyword.trim()

        updateData { state ->
            state.copy(
                submittedKeyword = submittedKeyword,
            )
        }

        loadCurationPosts(
            categoryId =
                uiState.value.data.selectedCategoryId,
            keyword = submittedKeyword,
        )
    }

    fun selectCategory(categoryId: Long?) {
        updateData { state ->
            state.copy(
                selectedCategoryId = categoryId,
            )
        }

        loadCurationPosts(
            categoryId = categoryId,
            keyword =
                uiState.value.data.submittedKeyword,
        )
    }

    fun selectPost(postId: Long) {
        updateData { state ->
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

            updateData { state ->
                val existingPost = state.posts.firstOrNull {
                    post -> post.postId == postId
                } ?: state.likedPosts.firstOrNull {
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
                    likedPosts =
                        state.likedPosts.map { post ->
                            if (post.postId == postId) {
                                detailPost
                            } else {
                                post
                            }
                        },
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

        updateData { state ->
            state.copy(selectedPostId = null)
        }
    }

    fun openBookmarkList() {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        sendEffect(CurationEffect.OpenBookmarkList)
        loadMyLikes()
        loadMyBookmarks()
    }

    fun loadMyLikes(
        page: Int = 1,
        size: Int = 10,
    ) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        val currentState = uiState.value.data

        if (
            page > 1 &&
            (
                currentState.isLikesLoading ||
                    page > currentState.likesTotalPages
                )
        ) {
            return
        }

        val requestVersion = if (page == 1) {
            ++likesRequestVersion
        } else {
            likesRequestVersion
        }

        updateData { state ->
            state.copy(
                likedPosts = if (page == 1) {
                    emptyList()
                } else {
                    state.likedPosts
                },
                likesPage = if (page == 1) {
                    0
                } else {
                    state.likesPage
                },
                likesTotalPages = if (page == 1) {
                    1
                } else {
                    state.likesTotalPages
                },
                isLikesLoading = true,
            )
        }

        launch {
            try {
                val result = curationRepository.getMyLikes(
                    page = page,
                    size = size,
                )

                if (requestVersion != likesRequestVersion) {
                    return@launch
                }

                updateData { state ->
                    val loadedLikes = result.content.map { item ->
                        val likedPost = item.toCurationPost()
                        val existingPost = state.posts.firstOrNull {
                            it.postId == likedPost.postId
                        } ?: state.bookmarkedPosts.firstOrNull {
                            it.postId == likedPost.postId
                        }

                        if (existingPost == null) {
                            likedPost
                        } else {
                            likedPost.copy(
                                content = existingPost.content,
                                thumbnailUrl = existingPost.thumbnailUrl,
                                viewCount = existingPost.viewCount,
                            )
                        }
                    }

                    val likedPosts = if (page == 1) {
                        loadedLikes
                    } else {
                        (state.likedPosts + loadedLikes)
                            .distinctBy { it.postId }
                    }

                    val likesByPostId = likedPosts.associateBy {
                        it.postId
                    }

                    state.copy(
                        posts = state.posts.map { post ->
                            val likedPost = likesByPostId[post.postId]

                            if (likedPost == null) {
                                post
                            } else {
                                post.copy(
                                    likeCount = likedPost.likeCount,
                                    isLiked = true,
                                    isBookmarked = likedPost.isBookmarked,
                                )
                            }
                        },
                        likedPosts = likedPosts,
                        bookmarkedPosts =
                            state.bookmarkedPosts.map { post ->
                                val likedPost =
                                    likesByPostId[post.postId]

                                if (likedPost == null) {
                                    post
                                } else {
                                    post.copy(
                                        likeCount = likedPost.likeCount,
                                        isLiked = true,
                                    )
                                }
                            },
                        likesPage = result.page,
                        likesTotalPages = result.totalPages,
                    )
                }
            } finally {
                if (requestVersion == likesRequestVersion) {
                    updateData { state ->
                        state.copy(isLikesLoading = false)
                    }
                }
            }
        }
    }

    fun loadNextMyLikes() {
        val state = uiState.value.data

        if (
            state.isLikesLoading ||
            !state.hasNextLikesPage
        ) {
            return
        }

        loadMyLikes(
            page = state.likesPage + 1,
        )
    }

    fun loadMyBookmarks(
        page: Int = 1,
        size: Int = 10,
    ) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        val currentState = uiState.value.data

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

        updateData { state ->
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

                updateData { state ->
                    val loadedBookmarks = result.content.map { item ->
                        val bookmarkedPost = item.toCurationPost()
                        val existingPost = state.posts.firstOrNull {
                            it.postId == bookmarkedPost.postId
                        } ?: state.likedPosts.firstOrNull {
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
                        likedPosts = state.likedPosts.map { post ->
                            val bookmarkPost =
                                bookmarksByPostId[post.postId]

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
                    updateData { state ->
                        state.copy(isBookmarksLoading = false)
                    }
                }
            }
        }
    }

    fun loadNextMyBookmarks() {
        val state = uiState.value.data

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

            updateData { state ->
                val currentPost = state.posts.firstOrNull {
                    it.postId == result.postId
                } ?: state.likedPosts.firstOrNull {
                    it.postId == result.postId
                } ?: state.bookmarkedPosts.firstOrNull {
                    it.postId == result.postId
                }

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

                val updatedLikedPosts = if (result.liked) {
                    if (state.likedPosts.any {
                            it.postId == result.postId
                        }
                    ) {
                        state.likedPosts.map(updatePost)
                    } else {
                        currentPost?.let { post ->
                            listOf(updatePost(post)) + state.likedPosts
                        } ?: state.likedPosts
                    }
                } else {
                    state.likedPosts.filterNot { post ->
                        post.postId == result.postId
                    }
                }

                state.copy(
                    posts = state.posts.map(updatePost),
                    likedPosts = updatedLikedPosts,
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

            updateData { state ->
                val currentPost = state.posts.firstOrNull {
                    it.postId == result.postId
                } ?: state.likedPosts.firstOrNull {
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
                    likedPosts = state.likedPosts.map { post ->
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

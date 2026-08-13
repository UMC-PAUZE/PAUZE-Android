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
    object OpenArchive : CurationEffect
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
            state.copy(isPostsLoading = true)
        }

        launch(
            onFailure = {
                if (requestVersion == postsRequestVersion) {
                    updateData { state ->
                        state.copy(isPostsLoading = false)
                    }
                }
            },
        ) {
            val result = curationRepository.getCurationPosts(
                categoryId = categoryId,
                keyword = keyword.takeIf { !it.isNullOrBlank() },
                page = page,
                size = size,
            )
            val state = uiState.value.data

            if (requestVersion != postsRequestVersion) {
                state
            } else {
                val posts = result.content.map { item ->
                    item.toCurationPost()
                }
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
                    isPostsLoading = false,
                )
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
            state.copy(
                selectedPostId = postId,
                selectedPostDetail = null,
            )
        }

        loadCurationPostDetail(postId)
    }

    private fun loadCurationPostDetail(
        postId: Long,
    ) {
        launch(
            onFailure = {
                updateData { state ->
                    if (
                        state.selectedPostId == postId &&
                        state.selectedPost == null
                    ) {
                        state.copy(
                            selectedPostId = null,
                            selectedPostDetail = null,
                        )
                    } else {
                        state
                    }
                }
            },
        ) {
            val detail =
                curationRepository.getCurationPostDetail(
                    postId = postId,
                )

            val state = uiState.value.data
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

            val updatedPosts = state.posts.map { post ->
                if (post.postId == postId) {
                    detailPost
                } else {
                    post
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
                selectedPostDetail = if (existingPost == null) {
                    detailPost
                } else {
                    null
                },
            )
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
            state.copy(
                selectedPostId = null,
                selectedPostDetail = null,
            )
        }
    }

    fun openArchive() {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        sendEffect(CurationEffect.OpenArchive)
    }

    fun loadArchive() {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        val keyword = uiState.value.data.submittedArchiveKeyword
            .takeIf { it.isNotBlank() }

        loadMyLikes(keyword = keyword)
        loadMyBookmarks(keyword = keyword)
    }

    fun updateArchiveKeyword(keyword: String) {
        updateData { state ->
            state.copy(archiveKeyword = keyword)
        }
    }

    fun searchArchive(keyword: String = uiState.value.data.archiveKeyword) {
        val submittedKeyword = keyword.trim()

        updateData { state ->
            state.copy(
                archiveKeyword = keyword,
                submittedArchiveKeyword = submittedKeyword,
            )
        }

        loadMyLikes(
            keyword = submittedKeyword.takeIf { it.isNotBlank() },
        )
        loadMyBookmarks(
            keyword = submittedKeyword.takeIf { it.isNotBlank() },
        )
    }

    fun loadMyLikes(
        keyword: String? = uiState.value.data.submittedArchiveKeyword
            .takeIf { it.isNotBlank() },
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

        launch(
            onFailure = {
                if (requestVersion == likesRequestVersion) {
                    updateData { state ->
                        state.copy(isLikesLoading = false)
                    }
                }
            },
        ) {
            val result = curationRepository.getMyLikes(
                keyword = keyword,
                page = page,
                size = size,
            )
            val state = uiState.value.data

            if (requestVersion != likesRequestVersion) {
                state
            } else {
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
                    isLikesLoading = false,
                )
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
            keyword = state.submittedArchiveKeyword
                .takeIf { it.isNotBlank() },
            page = state.likesPage + 1,
        )
    }

    fun loadMyBookmarks(
        keyword: String? = uiState.value.data.submittedArchiveKeyword
            .takeIf { it.isNotBlank() },
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

        launch(
            onFailure = {
                if (requestVersion == bookmarksRequestVersion) {
                    updateData { state ->
                        state.copy(isBookmarksLoading = false)
                    }
                }
            },
        ) {
            val result = curationRepository.getMyBookmarks(
                keyword = keyword,
                page = page,
                size = size,
            )
            val state = uiState.value.data

            if (requestVersion != bookmarksRequestVersion) {
                state
            } else {
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
                    isBookmarksLoading = false,
                )
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
            keyword = state.submittedArchiveKeyword
                .takeIf { it.isNotBlank() },
            page = state.bookmarksPage + 1,
        )
    }

    fun toggleLike(postId: Long) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        launch(
            onSuccess = { result ->
                updateData { state ->
                    val currentPost = state.posts.firstOrNull {
                        it.postId == result.postId
                    } ?: state.likedPosts.firstOrNull {
                        it.postId == result.postId
                    } ?: state.bookmarkedPosts.firstOrNull {
                        it.postId == result.postId
                    } ?: state.selectedPostDetail?.takeIf {
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
                        selectedPostDetail =
                            state.selectedPostDetail?.let(updatePost),
                    )
                }
            },
        ) {
            curationRepository.toggleCurationPostLike(
                postId = postId,
            )
        }
    }

    fun toggleBookmark(postId: Long) {
        if (TokenRepository.accessToken.isNullOrBlank()) {
            sendEffect(CurationEffect.NavigateToLogin)
            return
        }

        launch(
            onSuccess = { result ->
                updateData { state ->
                    val currentPost = state.posts.firstOrNull {
                        it.postId == result.postId
                    } ?: state.likedPosts.firstOrNull {
                        it.postId == result.postId
                    } ?: state.bookmarkedPosts.firstOrNull {
                        it.postId == result.postId
                    } ?: state.selectedPostDetail?.takeIf {
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
                        selectedPostDetail =
                            state.selectedPostDetail?.let { post ->
                                if (post.postId == result.postId) {
                                    post.copy(
                                        isBookmarked = result.bookmarked,
                                    )
                                } else {
                                    post
                                }
                            },
                    )
                }
            },
        ) {
            curationRepository.toggleCurationPostBookmark(
                postId = postId,
            )
        }
    }

    fun retry() {
        loadCurationPosts()
    }
}

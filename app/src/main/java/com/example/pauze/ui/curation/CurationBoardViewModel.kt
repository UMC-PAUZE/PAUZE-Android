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
    // 화면 재구성이나 Activity 재사용으로 같은 딥링크가 중복 처리되는 것을 막는다.
    private var handledDeepLink: String? = null

    // 조회 조건이나 선택이 바뀐 뒤 이전 응답이 최신 상태를 덮어쓰지 않도록 요청 세대를 구분한다.
    private var postsRequestVersion: Int = 0
    private var detailRequestVersion: Int = 0
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

        // 첫 페이지는 새 조회 세대를 시작하고, 추가 페이지는 현재 조회 세대에 합류한다.
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
                    // 서버 페이지 경계가 겹쳐도 동일 게시글이 목록에 중복 노출되지 않게 한다.
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
        val requestVersion = ++detailRequestVersion

        updateData { state ->
            val selectedPostSnapshot = state.posts.firstOrNull {
                it.postId == postId
            } ?: state.likedPosts.firstOrNull {
                it.postId == postId
            } ?: state.bookmarkedPosts.firstOrNull {
                it.postId == postId
            }

            state.copy(
                selectedPostId = postId,
                // 원본 목록에서 제거돼도 상세 화면은 유지할 수 있도록 보관한다.
                selectedPostDetail = selectedPostSnapshot,
            )
        }

        loadCurationPostDetail(
            postId = postId,
            requestVersion = requestVersion,
        )
    }

    private fun loadCurationPostDetail(
        postId: Long,
        requestVersion: Int,
    ) {
        launch(
            onFailure = {
                updateData { state ->
                    if (
                        requestVersion == detailRequestVersion &&
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

            // 이전 상세 요청이 늦게 끝나도 현재 선택 상태를 덮어쓰지 않는다.
            if (
                requestVersion != detailRequestVersion ||
                state.selectedPostId != postId
            ) {
                state
            } else {
                val existingPost = state.posts.firstOrNull {
                    post -> post.postId == postId
                } ?: state.likedPosts.firstOrNull {
                    post -> post.postId == postId
                } ?: state.bookmarkedPosts.firstOrNull {
                    post -> post.postId == postId
                } ?: state.selectedPostDetail?.takeIf {
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
                    selectedPostDetail = detailPost,
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
        ++detailRequestVersion

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

        // 좋아요와 북마크 탭은 독립적으로 로딩·페이징되므로 진입 시 각각 조회한다.
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
                // 첫 페이지는 새 검색 결과이므로 이전 검색의 항목과 페이지 정보를 초기화한다.
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

                    // 좋아요 목록 응답에 없는 본문·썸네일·조회수는 이미 받은 게시글에서 보완한다.
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

                // 다른 캐시를 갱신할 때 게시글마다 목록 전체를 다시 순회하지 않도록 인덱싱한다.
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
                // 첫 페이지는 새 검색 결과이므로 이전 검색의 항목과 페이지 정보를 초기화한다.
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

                    // 북마크 목록 응답에 없는 본문·조회수는 이미 받은 게시글에서 보완한다.
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

                // 다른 캐시를 갱신할 때 게시글마다 목록 전체를 다시 순회하지 않도록 인덱싱한다.
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

                    // 동일 게시글을 여러 화면이 참조하므로 모든 캐시와 상세 스냅샷을 함께 갱신한다.
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

                    // 원본 목록에서 제거되더라도 열린 상세 스냅샷에는 변경 결과를 반영한다.
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

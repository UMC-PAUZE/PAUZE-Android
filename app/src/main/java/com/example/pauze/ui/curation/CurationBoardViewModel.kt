package com.example.pauze.ui.curation

import com.example.pauze.data.model.BaseUiState
import com.example.pauze.data.model.CurationPost
import com.example.pauze.data.model.toCurationPost
import com.example.pauze.data.repository.CurationRepository
import com.example.pauze.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.pauze.data.repository.TokenRepository

sealed interface CurationEffect {
    object NavigateToLogin : CurationEffect
}

data class CurationBoardState(
    val keyword: String = "",
    val submittedKeyword: String = "",
    val selectedCategoryId: Long? = null,
    val posts: List<CurationPost> = emptyList(),
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

    val bookmarkedPosts: List<CurationPost>
        get() = posts.filter { post ->
            post.isBookmarked
        }

    val selectedPost: CurationPost?
        get() = posts.firstOrNull { post ->
            post.postId == selectedPostId
        }
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

    init {
        loadCurationPosts()
    }

    fun loadCurationPosts(
        categoryId: Long? = _curationState.value.selectedCategoryId,
        keyword: String? = _curationState.value.submittedKeyword,
        page: Int = 1,
        size: Int = 10,
    ) {
        launch {
            val result = curationRepository.getCurationPosts(
                categoryId = categoryId,
                keyword = keyword.takeIf { !it.isNullOrBlank() },
                page = page,
                size = size,
            )

            val posts = result.content.map { item ->
                item.toCurationPost()
            }

            _curationState.update { state ->
                state.copy(posts = posts)
            }
        }
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
                }

                val detailPost = detail.toCurationPost(
                    summary = existingPost?.summary
                        ?: detail.content,
                )

                val updatedPosts =
                    if (existingPost == null) {
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
                state.copy(
                    posts = state.posts.map { post ->
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
                    },
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
                )
            }
        }
    }

    fun retry() {
        loadCurationPosts()
    }
}
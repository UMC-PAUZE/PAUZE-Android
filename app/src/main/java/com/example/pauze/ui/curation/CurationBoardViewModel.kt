package com.example.pauze.ui.curation

import com.example.pauze.data.dummies.dummyCurationPosts
import com.example.pauze.data.model.CurationPost
import com.example.pauze.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CurationBoardState(
    val keyword: String = "",
    val submittedKeyword: String = "",
    val selectedCategoryId: Long? = null,
    val posts: List<CurationPost> = dummyCurationPosts,
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

class CurationBoardViewModel : BaseViewModel<Nothing>() {
    private val _curationState = MutableStateFlow(CurationBoardState())
    val curationState = _curationState.asStateFlow()

    fun updateKeyword(keyword: String) {
        _curationState.update { state ->
            state.copy(keyword = keyword)
        }
    }

    fun search() {
        _curationState.update { state ->
            state.copy(submittedKeyword = state.keyword.trim())
        }
    }

    fun selectCategory(categoryId: Long?) {
        _curationState.update { state ->
            state.copy(selectedCategoryId = categoryId)
        }
    }

    fun selectPost(postId: Long) {
        _curationState.update { state ->
            state.copy(selectedPostId = postId)
        }
    }

    fun clearSelectedPost() {
        _curationState.update { state ->
            state.copy(selectedPostId = null)
        }
    }

    fun toggleLike(postId: Long) {
        _curationState.update { state ->
            state.copy(
                posts = state.posts.map { post ->
                    if (post.postId == postId) {
                        val willBeLiked = !post.isLiked

                        post.copy(
                            isLiked = willBeLiked,
                            likeCount = (
                                post.likeCount +
                                    if (willBeLiked) 1 else -1
                                ).coerceAtLeast(0),
                        )
                    } else {
                        post
                    }
                },
            )
        }
    }

    fun toggleBookmark(postId: Long) {
        _curationState.update { state ->
            state.copy(
                posts = state.posts.map { post ->
                    if (post.postId == postId) {
                        post.copy(
                            isBookmarked = !post.isBookmarked,
                        )
                    } else {
                        post
                    }
                },
            )
        }
    }
}

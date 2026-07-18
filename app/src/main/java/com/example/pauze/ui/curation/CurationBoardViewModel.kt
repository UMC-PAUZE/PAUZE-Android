package com.example.pauze.ui.curation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pauze.data.dummies.dummyCurationPosts
import com.example.pauze.data.model.CurationPost

class CurationBoardViewModel : ViewModel() {
    var keyword by mutableStateOf("")
        private set

    var submittedKeyword by mutableStateOf("")
        private set

    var selectedCategoryId by mutableStateOf<Long?>(null)
        private set

    var posts by mutableStateOf(dummyCurationPosts)
        private set

    var selectedPostId by mutableStateOf<Long?>(null)
        private set

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

    fun updateKeyword(keyword: String) {
        this.keyword = keyword
    }

    fun search() {
        submittedKeyword = keyword.trim()
    }

    fun selectCategory(categoryId: Long?) {
        selectedCategoryId = categoryId
    }

    fun selectPost(postId: Long) {
        selectedPostId = postId
    }

    fun clearSelectedPost() {
        selectedPostId = null
    }

    fun toggleLike(postId: Long) {
        posts = posts.map { post ->
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
        }
    }

    fun toggleBookmark(postId: Long) {
        posts = posts.map { post ->
            if (post.postId == postId) {
                post.copy(
                    isBookmarked = !post.isBookmarked,
                )
            } else {
                post
            }
        }
    }
}

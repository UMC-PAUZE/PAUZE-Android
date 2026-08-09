package com.example.pauze.data.model

data class CurationBoardState(
    val keyword: String = "",
    val submittedKeyword: String = "",
    val selectedCategoryId: Long? = null,

    val posts: List<CurationPost> = emptyList(),
    val postsPage: Int = 0,
    val postsTotalPages: Int = 1,
    val isPostsLoading: Boolean = false,

    val likedPosts: List<CurationPost> = emptyList(),
    val likesPage: Int = 0,
    val likesTotalPages: Int = 1,
    val isLikesLoading: Boolean = false,

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
        get() = posts.firstOrNull { post ->
            post.postId == selectedPostId
        } ?: likedPosts.firstOrNull { post ->
            post.postId == selectedPostId
        } ?: bookmarkedPosts.firstOrNull { post ->
            post.postId == selectedPostId
        }

    val hasNextPostsPage: Boolean
        get() = postsPage < postsTotalPages

    val hasNextLikesPage: Boolean
        get() = likesPage < likesTotalPages

    val hasNextBookmarksPage: Boolean
        get() = bookmarksPage < bookmarksTotalPages
}
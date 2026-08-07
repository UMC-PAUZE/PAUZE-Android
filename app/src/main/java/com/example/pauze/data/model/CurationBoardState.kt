package com.example.pauze.data.model

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

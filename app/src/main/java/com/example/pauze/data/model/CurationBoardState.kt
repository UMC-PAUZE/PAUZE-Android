package com.example.pauze.data.model

data class CurationBoardState(
    val keyword: String = "",
    val submittedKeyword: String = "",
    val selectedCategoryId: Long? = null,

    val posts: List<CurationPost> = emptyList(),
    val postsPage: Int = 0,
    val postsTotalPages: Int = 1,
    val isPostsLoading: Boolean = false,

    val archiveKeyword: String = "",
    val submittedArchiveKeyword: String = "",

    val likedPosts: List<CurationPost> = emptyList(),
    val likesPage: Int = 0,
    val likesTotalPages: Int = 1,
    val isLikesLoading: Boolean = false,

    val bookmarkedPosts: List<CurationPost> = emptyList(),
    val bookmarksPage: Int = 0,
    val bookmarksTotalPages: Int = 1,
    val isBookmarksLoading: Boolean = false,

    val selectedPostId: Long? = null,
    // 보관함 멤버십이 해제돼도 사용자가 보고 있던 상세 화면을 유지하기 위한 스냅샷이다.
    val selectedPostDetail: CurationPost? = null,
) {
    // 목록의 최신 상태를 우선 사용하고, 목록에서 제거된 경우에만 상세 스냅샷으로 대체한다.
    val selectedPost: CurationPost?
        get() = posts.firstOrNull { post ->
            post.postId == selectedPostId
        } ?: likedPosts.firstOrNull { post ->
            post.postId == selectedPostId
        } ?: bookmarkedPosts.firstOrNull { post ->
            post.postId == selectedPostId
        } ?: selectedPostDetail?.takeIf { post ->
            post.postId == selectedPostId
        }

    val hasNextPostsPage: Boolean
        get() = postsPage < postsTotalPages

    val hasNextLikesPage: Boolean
        get() = likesPage < likesTotalPages

    val hasNextBookmarksPage: Boolean
        get() = bookmarksPage < bookmarksTotalPages
}

package com.example.pauze.data.model

data class CurationPostListResultDto(
    val content: List<CurationPostListItemDto>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
)

data class CurationPostListItemDto(
    val postId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val estimatedReadTime: Int,
    val summary: String,
    val source: String?,
    val thumbnailUrl: String?,
    val viewCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val createdAt: String,
)

data class CurationPostDetailDto(
    val postId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val content: String,
    val source: String?,
    val thumbnailUrl: String?,
    val viewCount: Int,
    val likeCount: Int,
    val estimatedReadTime: Int,
    val isPublished: Boolean,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val createdAt: String,
    val updatedAt: String?,
)

data class CurationPostLikeResultDto(
    val postId: Long,
    val liked: Boolean,
)

data class CurationPostBookmarkResultDto(
    val postId: Long,
    val bookmarked: Boolean,
)

data class MyBookmarkListResultDto(
    val content: List<MyBookmarkListItemDto>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
)

data class MyBookmarkListItemDto(
    val bookmarkId: Long,
    val postId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val estimatedReadTime: Int,
    val summary: String,
    val thumbnailUrl: String?,
    val likeCount: Int,
    val isLiked: Boolean,
    val createdAt: String,
)

data class MyLikeListResultDto(
    val content: List<MyLikeListItemDto>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
)

data class MyLikeListItemDto(
    val likesId: Long,
    val postId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val estimatedReadTime: Int,
    val summary: String,
    val likeCount: Int,
    val isBookmarked: Boolean,
    val createdAt: String,
)

fun CurationPostListItemDto.toCurationPost(): CurationPost {
    return CurationPost(
        postId = postId,
        categoryId = categoryId,
        categoryName = categoryName,
        title = title,
        summary = summary,
        thumbnailUrl = thumbnailUrl,
        viewCount = viewCount,
        likeCount = likeCount,
        readingTimeMinutes = estimatedReadTime,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
        createdAt = createdAt,
    )
}

fun CurationPostDetailDto.toCurationPost(
    summary: String = content,
): CurationPost {
    return CurationPost(
        postId = postId,
        categoryId = categoryId,
        categoryName = categoryName,
        title = title,
        summary = summary,
        content = content,
        thumbnailUrl = thumbnailUrl,
        viewCount = viewCount,
        likeCount = likeCount,
        readingTimeMinutes = estimatedReadTime,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
        createdAt = createdAt,
    )
}

fun MyBookmarkListItemDto.toCurationPost(): CurationPost {
    return CurationPost(
        postId = postId,
        categoryId = categoryId,
        categoryName = categoryName,
        title = title,
        summary = summary,
        thumbnailUrl = thumbnailUrl,
        viewCount = 0,
        likeCount = likeCount,
        readingTimeMinutes = estimatedReadTime,
        isLiked = isLiked,
        isBookmarked = true,
        createdAt = createdAt,
    )
}

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
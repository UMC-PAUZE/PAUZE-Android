package com.example.pauze.data.model

data class CurationPost(
    val postId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String?,
    val viewCount: Int,
    val likeCount: Int,
    val readingTimeMinutes: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val createdAt: String,
)

package com.example.pauze.data.repository

import com.example.pauze.data.model.CurationPostListResultDto
import com.example.pauze.data.model.CurationPostDetailDto
import com.example.pauze.data.model.CurationPostBookmarkResultDto
import com.example.pauze.data.model.CurationPostLikeResultDto
import com.example.pauze.data.model.MyBookmarkListResultDto

interface CurationRepository {

    suspend fun getCurationPosts(
        categoryId: Long? = null,
        keyword: String? = null,
        page: Int = 1,
        size: Int = 10,
    ): CurationPostListResultDto

    suspend fun getCurationPostDetail(
        postId: Long,
    ): CurationPostDetailDto

    suspend fun toggleCurationPostLike(
        postId: Long,
    ): CurationPostLikeResultDto

    suspend fun toggleCurationPostBookmark(
        postId: Long,
    ): CurationPostBookmarkResultDto

    suspend fun getMyBookmarks(
        page: Int = 1,
        size: Int = 10,
    ): MyBookmarkListResultDto
}

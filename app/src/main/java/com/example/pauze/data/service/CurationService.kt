package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.CurationPostListResultDto
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.pauze.data.model.CurationPostDetailDto
import retrofit2.http.Path
import com.example.pauze.data.model.CurationPostBookmarkResultDto
import com.example.pauze.data.model.CurationPostLikeResultDto
import com.example.pauze.data.model.MyBookmarkListResultDto
import retrofit2.http.PATCH
import com.example.pauze.data.model.MyLikeListResultDto

interface CurationService {

    @GET("curation-posts")
    suspend fun getCurationPosts(
        @Query("categoryId") categoryId: Long? = null,
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): BaseResponse<CurationPostListResultDto>

    @GET("curation-posts/{postId}")
    suspend fun getCurationPostDetail(
        @Path("postId") postId: Long,
    ): BaseResponse<CurationPostDetailDto>

    @PATCH("curation-posts/{postId}/likes")
    suspend fun toggleCurationPostLike(
        @Path("postId") postId: Long,
    ): BaseResponse<CurationPostLikeResultDto>

    @PATCH("curation-posts/{postId}/bookmarks")
    suspend fun toggleCurationPostBookmark(
        @Path("postId") postId: Long,
    ): BaseResponse<CurationPostBookmarkResultDto>

    @GET("users/me/bookmarks")
    suspend fun getMyBookmarks(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): BaseResponse<MyBookmarkListResultDto>

    @GET("users/me/likes")
    suspend fun getMyLikes(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): BaseResponse<MyLikeListResultDto>
}

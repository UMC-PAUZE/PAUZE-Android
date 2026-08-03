package com.example.pauze.data.service

import com.example.pauze.data.model.BaseResponse
import com.example.pauze.data.model.CurationPostListResultDto
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.pauze.data.model.CurationPostDetailDto
import retrofit2.http.Path
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
}
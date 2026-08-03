package com.example.pauze.data.repository

import com.example.pauze.data.model.CurationPostListResultDto
import com.example.pauze.data.service.CurationService
import javax.inject.Inject
import com.example.pauze.data.model.CurationPostDetailDto

class CurationRepositoryImpl @Inject constructor(
    private val curationService: CurationService,
) : CurationRepository {

    override suspend fun getCurationPosts(
        categoryId: Long?,
        keyword: String?,
        page: Int,
        size: Int,
    ): CurationPostListResultDto {
        val response = curationService.getCurationPosts(
            categoryId = categoryId,
            keyword = keyword,
            page = page,
            size = size,
        )

        if (!response.isSuccess) {
            throw IllegalStateException(
                "${response.code}: ${response.message}",
            )
        }

        return response.result
            ?: throw IllegalStateException(
                "${response.code}: 응답 결과가 비어 있습니다.",
            )
    }

    override suspend fun getCurationPostDetail(
        postId: Long,
    ): CurationPostDetailDto {
        val response =
            curationService.getCurationPostDetail(postId)

        if (!response.isSuccess) {
            throw IllegalStateException(
                "${response.code}: ${response.message}",
            )
        }

        return response.result
            ?: throw IllegalStateException(
                "${response.code}: 상세 게시글 데이터가 비어 있습니다.",
            )
    }
}
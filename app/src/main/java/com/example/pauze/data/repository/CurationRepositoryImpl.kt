package com.example.pauze.data.repository

import com.example.pauze.data.model.CurationPostListResultDto
import com.example.pauze.data.service.CurationService
import javax.inject.Inject
import com.example.pauze.data.model.CurationPostDetailDto
import com.example.pauze.data.model.CurationPostBookmarkResultDto
import com.example.pauze.data.model.CurationPostLikeResultDto
import com.example.pauze.data.model.MyBookmarkListResultDto
import kotlinx.coroutines.CancellationException

class CurationRepositoryImpl @Inject constructor(
    private val curationService: CurationService,
) : CurationRepository {

    override suspend fun getCurationPosts(
        categoryId: Long?,
        keyword: String?,
        page: Int,
        size: Int,
    ): CurationPostListResultDto {
        return try {
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

            response.result
                ?: throw IllegalStateException(
                    "${response.code}: 응답 결과가 비어 있습니다.",
                )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw requestException("게시글 목록 조회", e)
        }
    }

    override suspend fun getCurationPostDetail(
        postId: Long,
    ): CurationPostDetailDto {
        return try {
            val response =
                curationService.getCurationPostDetail(postId)

            if (!response.isSuccess) {
                throw IllegalStateException(
                    "${response.code}: ${response.message}",
                )
            }

            response.result
                ?: throw IllegalStateException(
                    "${response.code}: 상세 게시글 데이터가 비어 있습니다.",
                )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw requestException("게시글 상세 조회", e)
        }
    }

    override suspend fun toggleCurationPostLike(
        postId: Long,
    ): CurationPostLikeResultDto {
        return try {
            val response =
                curationService.toggleCurationPostLike(postId)

            if (!response.isSuccess) {
                throw IllegalStateException(
                    "${response.code}: ${response.message}",
                )
            }

            response.result
                ?: throw IllegalStateException(
                    "${response.code}: 좋아요 응답 결과가 비어 있습니다.",
                )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw requestException("게시글 좋아요 변경", e)
        }
    }

    override suspend fun toggleCurationPostBookmark(
        postId: Long,
    ): CurationPostBookmarkResultDto {
        return try {
            val response =
                curationService.toggleCurationPostBookmark(postId)

            if (!response.isSuccess) {
                throw IllegalStateException(
                    "${response.code}: ${response.message}",
                )
            }

            response.result
                ?: throw IllegalStateException(
                    "${response.code}: 북마크 응답 결과가 비어 있습니다.",
                )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw requestException("게시글 북마크 변경", e)
        }
    }

    override suspend fun getMyBookmarks(
        page: Int,
        size: Int,
    ): MyBookmarkListResultDto {
        return try {
            val response = curationService.getMyBookmarks(
                page = page,
                size = size,
            )

            if (!response.isSuccess) {
                throw IllegalStateException(
                    "${response.code}: ${response.message}",
                )
            }

            response.result
                ?: throw IllegalStateException(
                    "${response.code}: 북마크 목록 결과가 비어 있습니다.",
                )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw requestException("북마크 목록 조회", e)
        }
    }

    private fun requestException(
        operation: String,
        cause: Exception,
    ): IllegalStateException {
        return IllegalStateException(
            "$operation 중 오류가 발생했습니다: ${cause.message}",
            cause,
        )
    }
}

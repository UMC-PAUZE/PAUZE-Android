package com.example.pauze.data.repository

import com.example.pauze.data.model.CurationPostListResultDto

interface CurationRepository {

    suspend fun getCurationPosts(
        categoryId: Long? = null,
        keyword: String? = null,
        page: Int = 1,
        size: Int = 10,
    ): CurationPostListResultDto
}
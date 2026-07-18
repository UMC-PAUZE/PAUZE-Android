package com.example.pauze.data.dummies

import com.example.pauze.data.model.CurationCategory
import com.example.pauze.data.model.CurationPost

val curationCategories = listOf(
    CurationCategory(
        categoryId = null,
        categoryName = "전체",
    ),
    CurationCategory(
        categoryId = 1L,
        categoryName = "연구",
    ),
    CurationCategory(
        categoryId = 2L,
        categoryName = "진정법",
    ),
    CurationCategory(
        categoryId = 3L,
        categoryName = "일상팁",
    ),
    CurationCategory(
        categoryId = 4L,
        categoryName = "기타",
    ),
)

val dummyCurationPosts = listOf(
    CurationPost(
        postId = 1L,
        categoryId = 3L,
        categoryName = "일상팁",
        title = "퇴근길 감각 과부하 대처법 5가지",
        summary = "매일 퇴근길 지하철에서 너무 지쳐서 집에 오면 아무것도 못 하겠더라고요.",
        thumbnailUrl = null,
        viewCount = 147,
        likeCount = 147,
        readingTimeMinutes = 5,
        isLiked = false,
        isBookmarked = false,
        createdAt = "2026-07-14T10:30:00",
    ),
    CurationPost(
        postId = 2L,
        categoryId = 1L,
        categoryName = "연구",
        title = "초민감자의 신경계와 감정 처리 방식 - 최신 연구",
        summary = "Elaine Aron의 연구에 따르면 HSP는 뇌의 처리 과정이 더 깊고 섬세하게 이루어집니다.",
        thumbnailUrl = null,
        viewCount = 342,
        likeCount = 342,
        readingTimeMinutes = 7,
        isLiked = true,
        isBookmarked = true,
        createdAt = "2026-07-13T10:30:00",
    ),
    CurationPost(
        postId = 3L,
        categoryId = 2L,
        categoryName = "진정법",
        title = "4-7-8 호흡법, 정말 효과 있나요?",
        summary = "반신반의하며 시작한 4-7-8 호흡법을 2주간 매일 실천해 보았습니다.",
        thumbnailUrl = null,
        viewCount = 198,
        likeCount = 198,
        readingTimeMinutes = 3,
        isLiked = false,
        isBookmarked = false,
        createdAt = "2026-07-11T10:30:00",
    ),
)

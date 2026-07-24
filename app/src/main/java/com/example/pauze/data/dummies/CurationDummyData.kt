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
        summary = """
            매일 퇴근길 지하철에서 너무 지쳐서 집에 오면 아무것도 못 하겠더라고요.

            저도 처음에는 왜 이렇게 유독 힘들지 자책했는데, HSP를 알고 나서부터 대처법을 찾기 시작했어요.

            1. 노이즈 캔슬링 이어폰 착용

            가장 효과적이에요. 소음만 차단해도 신경계 부담이 확 줄어요.

            2. 창가 자리 앉기

            열린 공간감이 압박감을 줄여줘요. 가능하면 구석 자리보다 창가를 추천해요.

            3. 시선 고정하기

            움직이는 사람들을 따라가며 눈을 움직이면 더 피로해져요. 한 지점에 시선을 고정해보세요.

            4. 손에 집중하기

            손바닥을 무릎 위에 놓고 손가락 감각에 집중하는 그라운딩 기법이에요. 내 몸으로 주의를 가져와요.

            5. 4-7-8 호흡하기

            4초 들숨, 7초 참기, 8초 날숨. PAUZE 앱 호흡 가이드를 이용해도 좋아요.

            도움이 됐으면 좋겠어요 💚
        """.trimIndent(),
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

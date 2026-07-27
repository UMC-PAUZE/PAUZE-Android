package com.example.pauze.data.dummies

import androidx.collection.objectListOf
import com.example.pauze.data.model.InstantAction
import com.example.pauze.data.model.RestGuide

val actions = objectListOf<InstantAction>(
    InstantAction(
        duration = 10,
        title = "눈 감고 10초 호흡"
    ),
    InstantAction(
        duration = 60,
        title = "어깨 스트레칭"
    ),
    InstantAction(
        duration = 30,
        title = "손목 털기"
    ),
    InstantAction(
        duration = 0,
        title = "물 한 모금"
    ),
)

val guideList = objectListOf<RestGuide>(
    RestGuide(
        image = 0,
        duration = 10,
        title = "혼자만의 공간 찾기",
        content = "5~10분간 혼자 있을 수 있는 조용한 곳으로 이동하세요. 화장실, 계단실, 빈 회의실도 좋아요."
    ),
    RestGuide(
        image = 0,
        duration = 10,
        title = "햇빛과 바랍 쐬기",
        content = "5~10분간 혼자 있을 수 있는 조용한 곳으로 이동하세요. 화장실, 계단실, 빈 회의실도 좋아요."
    ),
    RestGuide(
        image = 0,
        duration = 10,
        title = "디지털 디톡스",
        content = "5~10분간 혼자 있을 수 있는 조용한 곳으로 이동하세요. 화장실, 계단실, 빈 회의실도 좋아요."
    ),
    RestGuide(
        image = 0,
        duration = 10,
        title = "따뜻한 음료 한 잔",
        content = "5~10분간 혼자 있을 수 있는 조용한 곳으로 이동하세요. 화장실, 계단실, 빈 회의실도 좋아요."
    ),
    RestGuide(
        image = 0,
        duration = 10,
        title = "감정 일기 3줄 쓰기",
        content = "5~10분간 혼자 있을 수 있는 조용한 곳으로 이동하세요. 화장실, 계단실, 빈 회의실도 좋아요."
    ),
)
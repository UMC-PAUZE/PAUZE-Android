package com.example.pauze.data.model

//user/me - 마이페이지 조회
data class UserMeResultDto(
    val uid: String,
    val nickname: String,
    val profileImageUrl: String?,
    val socialTypes: List<String>,
    val settings: Settings,
)

data class Settings(
    val notifications: Notifications,
    val stabilityContent: StabilityContent,
)

data class Notifications(
    val reminderAlarmActive: Boolean,
    val sensitiveAlarmActive: Boolean,
)

data class StabilityContent(
    val breathingGuideEnabled: Boolean,
    val stabilitySoundEnabled: Boolean,
    val offlineContentEnabled: Boolean,
)

//user/me/profile - 상세 조회
data class UserProfileResultDto(
    val uid: String,
    val name: String,
    val nickname: String,
    val introduction: String?,
    val profileImageUrl: String?,
    val email: String,
    val socialTypes: List<String>,
    val joinedAt: String,
    val stats: Stats,
)

data class Stats(
    val totalMeasurements: Int?,
    val consecutiveDays: Int?,
    val averageSensitivity: Double?,
)

//user/me/profile - 수정
data class UserProfileUpdateResultDto(
    val uid: String,
    val name: String,
    val nickname: String,
    val introduction: String?,
    val profileImageUrl: String?,
)

// user/me/settings 요청용
data class UpdateSettingsRequest(
    val notifications: NotificationsUpdate? = null,
    val stabilityContent: StabilityContentUpdate? = null,
)

data class NotificationsUpdate(
    val reminderAlarmActive: Boolean? = null,
    val sensitiveAlarmActive: Boolean? = null,
)

data class StabilityContentUpdate(
    val breathingGuideEnabled: Boolean? = null,
    val stabilitySoundEnabled: Boolean? = null,
    val offlineContentEnabled: Boolean? = null,
)

//user/me - 탈퇴
data class WithdrawRequest(
    val confirm: Boolean,
)

// State
data class MyPageState(
    val profile: UserMeResultDto? = null,
    val stats: Stats? = null,
    val profileError: String? = null
)
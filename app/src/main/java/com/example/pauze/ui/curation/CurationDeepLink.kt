package com.example.pauze.ui.curation

import android.net.Uri

private const val PAUZE_APP_LINK_HOST = "pauze-39e6b.web.app"
private const val PAUZE_CUSTOM_SCHEME = "pauze"
private const val CURATION_ROUTE = "curation"

internal fun createCurationShareUrl(postId: Long): String =
    "https://$PAUZE_APP_LINK_HOST/$CURATION_ROUTE/$postId"

// 외부 공유 링크와 앱 내부 커스텀 스킴이 동일한 상세 진입 규칙을 사용하도록 한 곳에서 검증한다.
internal fun Uri.toCurationPostIdOrNull(): Long? {
    val postIdSegment = when {
        scheme == PAUZE_CUSTOM_SCHEME && host == CURATION_ROUTE -> {
            pathSegments.firstOrNull()
        }

        scheme == "https" &&
            host == PAUZE_APP_LINK_HOST &&
            pathSegments.firstOrNull() == CURATION_ROUTE -> {
            pathSegments.getOrNull(1)
        }

        else -> null
    }

    return postIdSegment?.toLongOrNull()
}

package com.example.pauze.ui.curation

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class CurationRelativeTimeTest {
    private val currentTimeMillis =
        Instant.parse("2026-08-21T12:00:30Z").toEpochMilli()

    @Test
    fun `UTC 시각의 Z를 반영한다`() {
        assertEquals(
            "방금 전",
            formatRelativeTime(
                createdAt = "2026-08-21T12:00:00Z",
                currentTimeMillis = currentTimeMillis,
            ),
        )
    }

    @Test
    fun `명시적인 시간대 오프셋을 반영한다`() {
        assertEquals(
            "방금 전",
            formatRelativeTime(
                createdAt = "2026-08-21T21:00:00+09:00",
                currentTimeMillis = currentTimeMillis,
            ),
        )
    }

    @Test
    fun `시간대 없는 기존 시각은 서울 시각으로 해석한다`() {
        assertEquals(
            "방금 전",
            formatRelativeTime(
                createdAt = "2026-08-21T21:00:00",
                currentTimeMillis = currentTimeMillis,
            ),
        )
    }
}

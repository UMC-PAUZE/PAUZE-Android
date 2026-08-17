package com.example.pauze.ui.pauze

import com.example.pauze.data.model.EnergyLevel
import com.example.pauze.data.model.NoiseLevel
import com.example.pauze.data.model.SleepLevel
import com.example.pauze.data.model.SocialLevel
import com.example.pauze.data.model.VisualLevel
import com.example.pauze.ui.pauze.condition.toTodayConditionRequest
import org.junit.Assert.assertEquals
import org.junit.Test

class PauzeTodayConditionViewModelTest {
    @Test
    fun `화면 선택 순서를 서버 enum으로 변환한다`() {
        val request = listOf<Int?>(0, 1, 2, 3, 0).toTodayConditionRequest()

        assertEquals(SleepLevel.LESS_4, request.sleepLevel)
        assertEquals(NoiseLevel.NORMAL, request.noiseLevel)
        assertEquals(VisualLevel.HIGH, request.visualLevel)
        assertEquals(SocialLevel.MANY, request.socialLevel)
        assertEquals(EnergyLevel.NONE, request.energyLevel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `답변이 빠져 있으면 요청을 만들지 않는다`() {
        listOf<Int?>(0, 1, null, 3, 0).toTodayConditionRequest()
    }
}

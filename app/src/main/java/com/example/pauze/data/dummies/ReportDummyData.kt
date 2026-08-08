package com.example.pauze.data.dummies

import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.EnergyLevel
import com.example.pauze.data.model.NoiseLevel
import com.example.pauze.data.model.SensitivityLevel
import com.example.pauze.data.model.SleepLevel
import com.example.pauze.data.model.SocialLevel
import com.example.pauze.data.model.VisualLevel

object ReportDummyData { //ui 확인하기 위한 데이터

    val todayCondition = Condition(
        score = 26,
        sleep = SleepLevel.FOUR_TO_SIX,
        noise = NoiseLevel.QUIET,
        visual = VisualLevel.LOW,
        social = SocialLevel.LITTLE,
        energy = EnergyLevel.ENOUGH,
        sensitivity = SensitivityLevel.LOW
    )
}

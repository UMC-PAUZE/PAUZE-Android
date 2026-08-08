package com.example.pauze.data.dummies

import androidx.collection.objectListOf
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.EnergyLevel
import com.example.pauze.data.model.NoiseLevel
import com.example.pauze.data.model.SensitivityLevel
import com.example.pauze.data.model.SleepLevel
import com.example.pauze.data.model.SocialLevel
import com.example.pauze.data.model.VisualLevel

val conditions = objectListOf(
    Condition(
        score = 56,
        sleep = SleepLevel.LESS_4,
        noise = NoiseLevel.NORMAL,
        visual = VisualLevel.NORMAL,
        social = SocialLevel.SOME,
        energy = EnergyLevel.NORMAL,
        sensitivity = SensitivityLevel.NORMAL
    ),
    Condition(
        score = 78,
        sleep = SleepLevel.LESS_4,
        noise = NoiseLevel.HARD,
        visual = VisualLevel.HIGH,
        social = SocialLevel.MANY,
        energy = EnergyLevel.LOW,
        sensitivity = SensitivityLevel.HIGH
    )
)

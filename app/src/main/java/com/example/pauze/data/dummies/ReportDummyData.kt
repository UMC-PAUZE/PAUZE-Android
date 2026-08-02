package com.example.pauze.data.dummies

import com.example.pauze.data.model.Activity
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.Noise
import com.example.pauze.data.model.OverallIndex
import com.example.pauze.data.model.Sleeping

object ReportDummyData { //ui 확인하기 위한 데이터

    val todayCondition = Condition(
        score = 26,
        index = OverallIndex.Low,
        sleeping = Sleeping.Moderate,
        noise = Noise.Low,
        activity = Activity.Moderate
    )
}
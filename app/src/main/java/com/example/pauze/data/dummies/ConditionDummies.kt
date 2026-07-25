package com.example.pauze.data.dummies

import androidx.collection.objectListOf
import com.example.pauze.data.model.Activity
import com.example.pauze.data.model.Condition
import com.example.pauze.data.model.Noise
import com.example.pauze.data.model.OverallIndex
import com.example.pauze.data.model.Sleeping

val conditions = objectListOf(
    Condition(
        score = 56,
        index = OverallIndex.Moderate,
        sleeping = Sleeping.Low,
        noise = Noise.Moderate,
        activity = Activity.Moderate
    ),
    Condition(
        score = 78,
        index = OverallIndex.High,
        sleeping = Sleeping.Low,
        noise = Noise.High,
        activity = Activity.High
    )
)
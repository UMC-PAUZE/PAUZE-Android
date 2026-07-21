package com.example.pauze.data.dummies

import com.example.pauze.R
import com.example.pauze.data.model.SoundItem

object Sounds {
    val items = listOf(
        SoundItem("1", "빗소리", "자연소리", false, false, R.drawable.ic_rain),
        SoundItem("2", "파도소리", "자연소리", false, false, R.drawable.ic_empty_image),
        SoundItem("3", "시냇물소리", "자연소리", false, false, R.drawable.ic_empty_image),
        SoundItem("4", "천둥소리", "자연소리", false, false, R.drawable.ic_empty_image),
        SoundItem("5", "바람소리", "자연소리", false, false, R.drawable.ic_empty_image),
        SoundItem("6", "ASMR 속삭임", "ASMR", false, false, R.drawable.ic_empty_image),
        SoundItem("7", "백색소음", "노이즈", false, false, R.drawable.ic_empty_image)
    )
}

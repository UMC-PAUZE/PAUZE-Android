package com.example.pauze.data.model

enum class BreathPhase { READY, INHALE, HOLD, EXHALE } // 준비, 들숨, 참기, 날숨

data class BreathPattern(val inhale: Int, val hold: Int, val exhale: Int)
data class BreathState(val phase: BreathPhase, val second: Int)
package com.example.pauze.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

object UserProfileRepository {
    var birthday by mutableStateOf<LocalDate?>(null)
}
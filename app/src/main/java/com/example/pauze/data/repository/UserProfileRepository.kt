package com.example.pauze.data.repository

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

object UserProfileRepository {
    var nickname by mutableStateOf("조용한달빛님")
    var bio by mutableStateOf("")
    var birthday by mutableStateOf<LocalDate?>(null)
    var profileImageUri by mutableStateOf<Uri?>(null)
}
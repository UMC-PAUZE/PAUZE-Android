package com.example.pauze.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")
private val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")

@Singleton
class OnboardingDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val hasSeenOnboarding: Flow<Boolean> = context.onboardingDataStore.data
        .map { it[HAS_SEEN_ONBOARDING] ?: false }

    suspend fun markOnboardingSeen() {
        context.onboardingDataStore.edit { it[HAS_SEEN_ONBOARDING] = true }
    }
}
package com.hantash.notemark.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hantash.notemark.ui.component.SyncInterval
import com.hantash.notemark.utils.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
    // User Preferences
    val accessToken: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.ACCESS_TOKEN] ?: ""
    }

    val refreshToken: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.REFRESH_TOKEN] ?: ""
    }

    val username: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.USERNAME] ?: ""
    }

    val userId: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.USER_ID] ?: ""
    }

    suspend fun save(userId: String, username: String, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    // Last Sync
    val lastSyncFlow: Flow<Instant?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LAST_SYNC]?.let { Instant.ofEpochMilli(it) }
    }

    suspend fun savaLastSync(date: Instant) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SYNC] = date.toEpochMilli()
        }
    }

    // Sync Interval
    val syncIntervalFlow: Flow<SyncInterval> = dataStore.data.map { preferences ->
        SyncInterval.fromLabel(preferences[PreferencesKeys.SYNC_INTERVAL])
    }

    suspend fun saveSyncInterval(syncInterval: SyncInterval) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SYNC_INTERVAL] = syncInterval.label
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}
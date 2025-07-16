package com.hantash.notemark.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hantash.notemark.model.User
import com.hantash.notemark.utils.UserPreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
    val accessToken: Flow<String> = dataStore.data.map {
        it[UserPreferencesKeys.ACCESS_TOKEN] ?: ""
    }

    val refreshToken: Flow<String> = dataStore.data.map {
        it[UserPreferencesKeys.REFRESH_TOKEN] ?: ""
    }

    val username: Flow<String> = dataStore.data.map {
        it[UserPreferencesKeys.USERNAME] ?: ""
    }

    suspend fun save(username: String, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USERNAME] = username
            preferences[UserPreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[UserPreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}
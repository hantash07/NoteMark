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

    fun getUserDetail(): Flow<User> = flow{
        dataStore.data.map { preferences ->
            val username = preferences[UserPreferencesKeys.USERNAME] ?: ""
            val email = preferences[UserPreferencesKeys.EMAIL] ?: ""
            val password = preferences[UserPreferencesKeys.PASSWORD] ?: ""

            emit(User(username, email, password))
        }
    }

    suspend fun saveUserDetail(username: String, email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USERNAME] = username
            preferences[UserPreferencesKeys.EMAIL] = email
            preferences[UserPreferencesKeys.PASSWORD] = password
        }
    }

    suspend fun saveAuthDetail(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
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
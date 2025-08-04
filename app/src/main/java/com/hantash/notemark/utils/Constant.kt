package com.hantash.notemark.utils

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {
    const val APP_DEBUG = "app-debug"
    const val BASE_URL = "https://notemark.pl-coding.com"

    const val PREFERENCE_NAME = "user_preferences"

    const val CONTENT_LENGTH_PORTRAIT = 150
    const val CONTENT_LENGTH_LANDSCAPE = 250
}

object PreferencesKeys {
    val USER_ID = stringPreferencesKey("user_id")
    val USERNAME = stringPreferencesKey("username")
    val EMAIL = stringPreferencesKey("email")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val LAST_SYNC = longPreferencesKey("last_sync")
}
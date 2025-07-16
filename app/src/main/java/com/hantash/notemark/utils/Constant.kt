package com.hantash.notemark.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {
    const val APP_DEBUG = "app-debug"
    const val BASE_URL = "https://notemark.pl-coding.com"

    const val PREFERENCE_NAME = "user_preferences"
}

object UserPreferencesKeys {
    val USERNAME = stringPreferencesKey("username")
    val EMAIL = stringPreferencesKey("email")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}
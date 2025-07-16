package com.hantash.notemark.utils

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hantash.notemark.DevicePosture
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun debug(message: String) {
    Log.d(Constant.APP_DEBUG, message)
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    val pattern = Regex("^(?=.*[0-9!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")
    return pattern.matches(this)
}

fun String.beautifyUsername(): String {
    val username = this
    val words = username.trim()
        .split("\\s".toRegex())
        .filter { it.isNotEmpty() }

    return when {
        words.size == 1 -> username.take(2)
        words.size >= 2 -> "${words[0][0]}${words[words.size - 1][0]}"
        else -> ""
    }.toUpperCase(Locale.current)
}

fun Instant.toReadableDate(): String {
    val nowYear = Instant.now().atZone(ZoneId.systemDefault()).year
    val isSameYear = this.atZone(ZoneId.systemDefault()).year == nowYear
    val pattern = if (isSameYear) "dd MMM" else "dd MMM yyyy"

    val formatter = DateTimeFormatter.ofPattern(pattern)
        .withLocale(java.util.Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

//Added a CompositionLocal that allows passing the ScreenOrientation deep into the Composable tree:
val localScreenOrientation = staticCompositionLocalOf<DevicePosture> {
    error("No orientation provided")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constant.PREFERENCE_NAME)
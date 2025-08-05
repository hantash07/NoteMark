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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class EnumDateFormater(val pattern: String) {
    SIMPLE("dd MMM"),
    SIMPLE_YEAR("dd MMM yyyy"),
    DISPLAY("dd MMM yyyy, HH:mm");

    val formatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(pattern)
}

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

fun Instant?.beatifyLastSync(): String {
    if (this == null) {
        return "Never synced"
    }

    val now = Instant.now()
    val duration = Duration.between(this, now)
    if (duration.toMinutes() < 5) {
        return  "Just now"
    }

    return when {
        (duration < Duration.ofHours(1)) -> "${duration.toMinutes()} minutes ago"
        (duration < Duration.ofDays(1)) -> "${duration.toHours()} hours ago"
        (duration < Duration.ofDays(7)) -> "${duration.toDays()} days ago"
        else -> {
            // if duration is more than 7 days
            val formatter = DateTimeFormatter.ofPattern(EnumDateFormater.DISPLAY.pattern)
                .withLocale(java.util.Locale.getDefault())
                .withZone(ZoneId.systemDefault())
            formatter.format(this)
        }
    }

}

fun Instant.toReadableDate(enumDateFormat: EnumDateFormater = EnumDateFormater.SIMPLE): String {
    val nowYear = Instant.now().atZone(ZoneId.systemDefault()).year
    val isSameYear = this.atZone(ZoneId.systemDefault()).year == nowYear
    val dateFormater = if (enumDateFormat == EnumDateFormater.SIMPLE) {
        if (isSameYear) EnumDateFormater.SIMPLE.formatter  else EnumDateFormater.SIMPLE_YEAR.formatter
    } else {
        enumDateFormat.formatter
    }

    if (enumDateFormat == EnumDateFormater.DISPLAY) {
        val now = Instant.now()
        val duration = Duration.between(this, now)
        if (duration.toMinutes() < 5) {
            return  "Just now"
        }
    }

    val formatter = dateFormater
        .withLocale(java.util.Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun String.toMap(): Map<String, String> {
    val jsonElement = Json.parseToJsonElement(this)
    return jsonElement.jsonObject.mapValues { it.value.jsonPrimitive.content }
}

fun generateUserId(username: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(username.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) } // hex string
}

//Added a CompositionLocal that allows passing the ScreenOrientation deep into the Composable tree:
val localScreenOrientation = staticCompositionLocalOf<DevicePosture> {
    error("No orientation provided")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constant.PREFERENCE_NAME)
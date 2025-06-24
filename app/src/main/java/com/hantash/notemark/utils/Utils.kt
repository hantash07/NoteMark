package com.hantash.notemark.utils

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.staticCompositionLocalOf
import com.hantash.notemark.DevicePosture

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

//Added a CompositionLocal that allows passing the ScreenOrientation deep into the Composable tree:
val localScreenOrientation = staticCompositionLocalOf<DevicePosture> {
    error("No orientation provided")
}
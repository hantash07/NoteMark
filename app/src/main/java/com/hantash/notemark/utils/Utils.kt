package com.hantash.notemark.utils

import android.util.Log
import android.util.Patterns

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
package com.hantash.notemark.ui.common

sealed class UiState<out T> {
    object Idle: UiState<Nothing>()
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<Nothing>()
}
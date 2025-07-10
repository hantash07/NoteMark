package com.hantash.notemark.ui.common

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    data class Navigate(val route: String): UiEvent()
    object NavigateBack: UiEvent()
    object DismissDialog: UiEvent()
}
package com.hantash.notemark.ui.common

import com.hantash.notemark.ui.navigation.EnumScreen

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    data class Navigate(val enumScreen: EnumScreen): UiEvent()
    data class Authenticate(val email: String, val password: String): UiEvent()
    object NavigateBack: UiEvent()
    object DismissDialog: UiEvent()
}
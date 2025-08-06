package com.hantash.notemark.ui.common

import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.navigation.EnumScreen

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    data class DeleteNote(val note: Note): UiEvent()
    data class Navigate(val enumScreen: EnumScreen): UiEvent()
    data class Authenticate(val email: String, val password: String): UiEvent()
    data object NavigateBack: UiEvent()
    data object DismissDialog: UiEvent()
}
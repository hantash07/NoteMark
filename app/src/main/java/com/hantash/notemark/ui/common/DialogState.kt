package com.hantash.notemark.ui.common

data class DialogState(
    val isVisible: Boolean = false,
    val title: String = "",
    val message: String = "",
    var confirmText: String = "Confirm",
    var dismissText: String = "Cancel",
    val onConfirm: () -> Unit = {},
    val onDismiss: () -> Unit = {}
)

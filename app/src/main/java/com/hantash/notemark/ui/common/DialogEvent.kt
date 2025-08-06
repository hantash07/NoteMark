package com.hantash.notemark.ui.common

import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.navigation.EnumScreen

sealed class DialogEvent {
    data object SyncSuccess: DialogEvent()
    data object SyncFailed: DialogEvent()
//    data object SyncSuccessBeforeLogout: DialogEvent()
//    data object SyncFailedBeforeLogout: DialogEvent()
}
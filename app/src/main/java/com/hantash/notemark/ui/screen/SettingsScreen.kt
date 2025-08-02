package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hantash.notemark.R
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.ui.component.AppLoading
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.EnumSettingsItem
import com.hantash.notemark.ui.component.EnumSettingsItem.LOGOUT
import com.hantash.notemark.ui.component.EnumSettingsItem.SYNC_DATA
import com.hantash.notemark.ui.component.EnumSettingsItem.SYNC_INTERVAL
import com.hantash.notemark.ui.component.SettingsItem
import com.hantash.notemark.ui.component.SyncInterval
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.navigation.EnumScreen.SETTINGS
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.beatifyLastSync
import com.hantash.notemark.utils.debug
import com.hantash.notemark.viewmodel.AuthViewModel
import com.hantash.notemark.viewmodel.NoteViewModel
import java.time.Instant

@Composable
fun SettingsScreen(
    onNavigateTo: (EnumScreen) -> Unit,
    onNavigateBack: () -> Unit
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val uiStateLogout = authViewModel.uiLogoutState.collectAsStateWithLifecycle()

    val noteViewModel: NoteViewModel = hiltViewModel()
    val lastSyncState = noteViewModel.lastSyncStateFlow.collectAsState(null)
    val uiStateSync = noteViewModel.syncUiState.collectAsStateWithLifecycle()

    val snackBarHost = remember { SnackbarHostState() }
    val syncInterval = remember { mutableStateOf(SyncInterval.ManualOnly) }
    val expandSyncOption = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        authViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigateTo.invoke(event.enumScreen)
                }

                is UiEvent.ShowSnackBar -> {
                    debug(event.message)
                    snackBarHost.showSnackbar(message = event.message)
                }

                else -> {}
            }
        }
    }

    SettingsScaffold(
        onNavigateBack = {
            onNavigateBack.invoke()
        },
        snackBarHost = {
            SnackbarHost(snackBarHost)
        },
        content = { paddingValues ->
            SettingsContent(
                modifier = Modifier.padding(paddingValues),
                lastSync = lastSyncState.value,
                isLoading = uiStateLogout.value == UiState.Loading || uiStateSync.value == UiState.Loading,
                isExpandSyncOption = expandSyncOption.value,
                selectedSyncInterval = syncInterval.value,
                onSelectInterval = {
                    expandSyncOption.value = !expandSyncOption.value
                    syncInterval.value = it
                },
                onClick = { settingItem ->
                    when (settingItem) {
                        SYNC_INTERVAL -> {
                            expandSyncOption.value = !expandSyncOption.value
                        }
                        SYNC_DATA -> {
                            noteViewModel.syncPendingNotes()
                        }
                        LOGOUT -> {
                            authViewModel.logout()
                        }
                    }
                },
            )
        }
    )
}

@Composable
private fun SettingsScaffold(
    onNavigateBack: () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {
        SettingsContent(Modifier.padding(it))
    }
) {
    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = SETTINGS,
                onClickBackButton = onNavigateBack
            )
        },
        snackbarHost = snackBarHost,
        content = content
    )
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    lastSync: Instant? = null,
    isLoading: Boolean = false,
    isExpandSyncOption: Boolean = false,
    selectedSyncInterval: SyncInterval = SyncInterval.ManualOnly,
    onSelectInterval: (SyncInterval) -> Unit = {},
    onClick: (EnumSettingsItem) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Surface)
            .padding(start = 16.dp, end = 16.dp)
    ) {

        if (isLoading) {
            AppLoading(
                modifier = Modifier.align(Alignment.Center),
                size = 16.dp
            )
        }

        Column {
            SettingsItem(
                enumItem = SYNC_INTERVAL,
                icon = R.drawable.ic_clock,
                selectedSyncInterval = selectedSyncInterval,
                isExpandSyncOption = isExpandSyncOption,
                onSelectInterval = onSelectInterval,
                onClick = onClick,
            )

            SettingsItem(
                enumItem = SYNC_DATA,
                description = "Last sync: ${lastSync.beatifyLastSync()}",
                icon = R.drawable.ic_refresh,
                onClick = onClick,
            )

            SettingsItem(
                enumItem = LOGOUT,
                icon = R.drawable.ic_logout,
                onClick = onClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortrait() {
    SettingsScaffold()
}

@Preview(showBackground = true, widthDp = 700, heightDp = 350)
@Composable
private fun PreviewLandscape() {
    SettingsScaffold()
}
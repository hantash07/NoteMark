package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hantash.notemark.ui.component.SettingsItem
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.navigation.EnumScreen.SETTINGS
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.debug
import com.hantash.notemark.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    onNavigateTo: (EnumScreen) -> Unit,
    onNavigateBack: () -> Unit
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState = viewModel.uiLogoutState.collectAsStateWithLifecycle()

    val snackBarHost = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEventFlow.collect { event ->
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
                uiState = uiState.value,
                onClickLogout = {
                    viewModel.logout()
                }
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
    uiState: UiState<Unit> = UiState.Idle,
    onClickLogout: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Surface)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        SettingsItem(
            text = "Log out",
            icon = R.drawable.ic_logout,
            onClick = onClickLogout
        )
        if (uiState == UiState.Loading) {
            AppLoading(
                modifier = Modifier.align(Alignment.Center),
                size = 16.dp
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
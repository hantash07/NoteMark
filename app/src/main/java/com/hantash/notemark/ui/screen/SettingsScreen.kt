package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hantash.notemark.R
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.SettingsItem
import com.hantash.notemark.ui.navigation.EnumScreen.SETTINGS
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    SettingsContent(onNavigateBack = {
        onNavigateBack.invoke()
    })
}

@Composable
private fun SettingsContent(onNavigateBack: () -> Unit = {}) {
    val viewModel: AuthViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = SETTINGS,
                onClickBackButton = onNavigateBack
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)
                .fillMaxSize()
                .background(color = Surface)
                .padding(start = 16.dp, end = 16.dp)
            ) {
                SettingsItem(
                    text = "Log out",
                    icon = R.drawable.ic_logout,
                    onClick = {
                        viewModel.logout()
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortrait() {
    SettingsContent()
}

@Preview(showBackground = true, widthDp = 700, heightDp = 350)
@Composable
private fun PreviewLandscape() {
    SettingsContent()
}
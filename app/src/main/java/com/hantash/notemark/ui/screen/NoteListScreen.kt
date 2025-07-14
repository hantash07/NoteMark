package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.hantash.notemark.ui.component.AppFloatingButton
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_LIST
import com.hantash.notemark.ui.navigation.EnumScreen.SETTINGS
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12

@Composable
fun NoteListScreen(onNavigateTo: (EnumScreen) -> Unit) {
    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = NOTE_LIST,
                onClickSettings = {
                    onNavigateTo.invoke(SETTINGS)
                }
            )
        },
        content = { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues).background(color = OnSurfaceOpacity12)) { }
        },
        floatingActionButton = {
            AppFloatingButton(onClick = {})
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
//    NoteListScreen()
}

package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.EnumNoteField
import com.hantash.notemark.ui.component.NotesField
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.localScreenOrientation

@Composable
fun NoteAddEditScreen(onNavigateBack: () -> Unit) {
    val modifier = when (localScreenOrientation.current) {
        DevicePosture.MOBILE_PORTRAIT -> Modifier.fillMaxWidth()
        else -> Modifier.width(540.dp)
    }
    NoteAddEditContent(modifier, onNavigateBack = {
        onNavigateBack.invoke()
    })
}

@Composable
private fun NoteAddEditContent(modifier: Modifier = Modifier, onNavigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = NOTE_ADD_EDIT,
                onClickBackButton = onNavigateBack,
                onClickSaveNote = {

                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(color = Surface),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier
                ){
                    NotesField(
                        enumNoteField = EnumNoteField.TITLE,
                        modifier = Modifier.fillMaxWidth()
                            .height(76.dp),
                        placeholder = "Note Title",
                        value = "",
                        onValueChange = {}
                    )
                    NotesField(
                        enumNoteField = EnumNoteField.DESCRIPTION,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = "Note Description...",
                        value = "",
                        onValueChange = {}
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortraits() {
    NoteAddEditContent()
}

@Preview(showBackground = true, widthDp = 740, heightDp = 360)
@Composable
private fun PreviewLandscapes() {
    NoteAddEditContent()
}

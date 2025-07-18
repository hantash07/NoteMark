package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.EnumNoteField
import com.hantash.notemark.ui.component.NotesField
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.debug
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.viewmodel.NoteViewModel
import java.time.Instant

@Composable
fun NoteAddEditScreen(noteId: String, onNavigateBack: () -> Unit) {
    val modifier = when (localScreenOrientation.current) {
        DevicePosture.MOBILE_PORTRAIT -> Modifier.fillMaxWidth()
        else -> Modifier.width(540.dp)
    }

    val focusManager = LocalFocusManager.current

    val noteViewModel: NoteViewModel = hiltViewModel()
    val noteState = noteViewModel.noteStateFlow.collectAsState()
    var note = Note()

    debug("NoteAddEditScreen")
    // Fetch note only once when noteId changes
    LaunchedEffect(noteId) {
        if (noteId.isNotEmpty()) {
            debug("LaunchedEffect: $noteId")
            noteViewModel.getNote(noteId)
        }
        debug("LaunchedEffect")
    }

    // Update local note only when state changes (avoid unnecessary writes)
    LaunchedEffect(noteState.value) {
        noteState.value?.let {
            note = it
        }
    }

    NoteScaffold(
        modifier = modifier,
        onNavigateBack = {
            focusManager.clearFocus()

            if (noteId.isNotEmpty()) {
                note.lastEditedAt = Instant.now()
                noteViewModel.updateNote(note)
            } else {
                noteViewModel.addNote(note)
            }

            onNavigateBack.invoke()
        },
        onClickSaveNote = {
            focusManager.clearFocus()

            if (noteId.isNotEmpty()) {
                note.lastEditedAt = Instant.now()
                noteViewModel.updateNote(note)
            } else {
                noteViewModel.addNote(note)
            }

            onNavigateBack.invoke()
        },
        content = { paddingValues ->
            Content(
                modifier = modifier.padding(paddingValues),
                note = noteState.value ?: Note(),
                onSaveNote = {
                    note = it
                }
            )
        }
    )
}

@Composable
private fun NoteScaffold(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onClickSaveNote: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        Content(modifier.padding(paddingValues))
    }
) {
    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = NOTE_ADD_EDIT,
                onClickBackButton = onNavigateBack,
                onClickSaveNote = onClickSaveNote
            )
        },
        content = content
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    note: Note = Note(),
    onSaveNote: (Note) -> Unit = {}
) {
    /*
    * Once mutableState is initialize the value does not change during recomposition.
    * If the value needs to updated when recomposed, UI state need to be kept inside remember(updateValue) {}
    * */

    val title = rememberSaveable(note.title) { mutableStateOf(note.title) }
    val content = rememberSaveable(note.content) { mutableStateOf(note.content) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
        ) {
            NotesField(
                enumNoteField = EnumNoteField.TITLE,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp),
                placeholder = "Note Title",
                value = title.value,
                onValueChange = {
                    title.value = it

                    note.title = title.value
                    onSaveNote.invoke(note)
                }
            )
            NotesField(
                enumNoteField = EnumNoteField.DESCRIPTION,
                modifier = Modifier.fillMaxSize(),
                placeholder = "Note Description...",
                value = content.value,
                onValueChange = {
                    content.value = it

                    content.value = it
                    note.content = content.value
                    onSaveNote.invoke(note)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortraits() {
    NoteScaffold()
}

@Preview(showBackground = true, widthDp = 740, heightDp = 360)
@Composable
private fun PreviewLandscapes() {
    NoteScaffold()
}

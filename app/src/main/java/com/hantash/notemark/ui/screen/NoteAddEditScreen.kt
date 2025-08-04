package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.ConfirmationDialog
import com.hantash.notemark.ui.component.EnumNoteField
import com.hantash.notemark.ui.component.NotesField
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_LIST
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.debug
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun NoteAddEditScreen(noteId: String, onNavigateBack: () -> Unit) {
    val devicePosture = localScreenOrientation.current
    val focusManager = LocalFocusManager.current

    val noteViewModel: NoteViewModel = hiltViewModel()
    val noteState = noteViewModel.noteStateFlow.collectAsState()

    val scope = rememberCoroutineScope()
    val noteToAdd = remember { mutableStateOf(Note()) }
    val showDialog = rememberSaveable { mutableStateOf(false) }

    // Fetch note only once when noteId changes
    LaunchedEffect(noteId) {
        debug("LaunchedEffect: noteId")
        if (noteId.isNotEmpty()) {
            noteViewModel.getNote(noteId)
        }
    }

    // Update local note only when state changes (avoid unnecessary writes)
    LaunchedEffect(noteState.value) {
        noteState.value?.let {
            noteToAdd.value = it
        }
    }

    fun addUpdateNote() {
        focusManager.clearFocus()
        val note = noteToAdd.value

        scope.launch {
            if (noteId.isNotEmpty()) {
                if (noteViewModel.isNoteUpdated(note)) {
                    note.lastEditedAt = Instant.now().toString()
                    noteViewModel.updateNote(note)
                }
            } else {
                noteViewModel.addNote(note)
            }
        }
    }

    NoteScaffold(
        devicePosture = devicePosture,
        onNavigateBack = {
            if (noteId.isNotEmpty()) {
                scope.launch {
                    if (noteViewModel.isNoteUpdated(noteToAdd.value)) {
                        showDialog.value = true
                    } else {
                        onNavigateBack.invoke()
                    }
                }
            } else {
                onNavigateBack.invoke()
            }
        },
        onClickSaveNote = {
            addUpdateNote()

            onNavigateBack.invoke()
        },
        content = { paddingValues ->
            Content(
                modifier = Modifier.padding(paddingValues),
                note = noteState.value ?: Note(),
                onSaveNote = {
                    noteToAdd.value = it
                }
            )
        }
    )

    if (showDialog.value) {
        ConfirmationDialog(
            showDialog.value,
            title = "Discard Changes?",
            message = "You have unsaved changes. If you discard now, all changes will be lost.",
            confirmText = "Discard",
            dismissText = "Keep Editing",
            onConfirm = {
                showDialog.value = false
                onNavigateBack.invoke()
            },
            onDismiss = {
                showDialog.value = false
            },
        )
    }
}

@Composable
private fun NoteScaffold(
    modifier: Modifier = Modifier,
    devicePosture: DevicePosture = DevicePosture.MOBILE_PORTRAIT,
    onNavigateBack: () -> Unit = {},
    onClickSaveNote: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        Content(modifier.padding(paddingValues))
    }
) {

    if (devicePosture == DevicePosture.MOBILE_PORTRAIT || devicePosture == DevicePosture.TABLET_PORTRAIT) {
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
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Surface),
            contentAlignment = Alignment.TopCenter
        ) {
            BaseAppBar(
                modifier = Modifier.padding(start = 32.dp),
                enumScreen = NOTE_ADD_EDIT,
                onClickBackButton = onNavigateBack,
                onClickSaveNote = onClickSaveNote
            )
            Box(
                modifier = Modifier
                    .padding(WindowInsets.systemBars.asPaddingValues())
                    .width(540.dp)
            ) {
                content(PaddingValues())
            }
        }
    }

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

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val title = rememberSaveable(note.title) { mutableStateOf(note.title) }
    val content = rememberSaveable(note.content) { mutableStateOf(note.content) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Surface)
            .verticalScroll(scrollState)
            .imePadding()
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
            },
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortraits() {
    NoteScaffold(devicePosture = DevicePosture.MOBILE_PORTRAIT)
}

@Preview(showBackground = true, widthDp = 740, heightDp = 360)
@Composable
private fun PreviewLandscapes() {
    NoteScaffold(devicePosture = DevicePosture.MOBILE_LANDSCAPE)
}

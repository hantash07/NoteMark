package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.component.AppFloatingButton
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.ConfirmationDialog
import com.hantash.notemark.ui.component.NoteItem
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_ADD_EDIT
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_DETAIL
import com.hantash.notemark.ui.navigation.EnumScreen.NOTE_LIST
import com.hantash.notemark.ui.navigation.EnumScreen.SETTINGS
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.utils.Constant
import com.hantash.notemark.utils.beautifyUsername
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.viewmodel.AuthViewModel
import com.hantash.notemark.viewmodel.ConnectivityViewModel
import com.hantash.notemark.viewmodel.NoteViewModel

@Composable
fun NoteListScreen(
    onNavigateTo: (EnumScreen) -> Unit,
    onNavWithArguments: (EnumScreen, String) -> Unit
) {
    val contentMaxLength = when (localScreenOrientation.current) {
        DevicePosture.MOBILE_PORTRAIT -> Constant.CONTENT_LENGTH_PORTRAIT
        else -> Constant.CONTENT_LENGTH_LANDSCAPE
    }

    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val connectivityState = connectivityViewModel.isConnected.collectAsState(false)

    val authViewModel: AuthViewModel = hiltViewModel()
    val usernameState = authViewModel.usernameState.collectAsState(initial = "")

    val noteViewModel: NoteViewModel = hiltViewModel()
    val notesState = noteViewModel.notesState.collectAsState(initial = emptyList())

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val noteToDelete = rememberSaveable { mutableStateOf<Note?>(null) }

    LaunchedEffect(true) {
        noteViewModel.uiEventFlow.collect { event ->
            if (event is UiEvent.DeleteNote) {
                showDialog.value = true
                noteToDelete.value = event.note
            }
        }
    }

    NoteListScaffold(
        username = usernameState.value.beautifyUsername(),
        isConnected = connectivityState.value,
        onClickSettings = {
            onNavigateTo(SETTINGS)
        },
        onAddNotes = {
            onNavWithArguments(NOTE_ADD_EDIT, "")
        },
        content = {
            Content(
                modifier = Modifier.padding(it),
                notes = notesState.value,
                contentMaxLength = contentMaxLength,
                onPreview = { note ->
                    onNavWithArguments(NOTE_DETAIL, note.id.toString())
                },
                onDelete = { note ->
                    noteViewModel.showConfirmationDialog(note)
                }
            )
        }
    )

    if (showDialog.value) {
        ConfirmationDialog(
            showDialog.value,
            title = "Delete Note?",
            message = "Are you sure you want to delete this note?\n" +
                    "This action cannot be undone.",
            onConfirm = {
                noteToDelete.value?.let { note ->
                    noteViewModel.deleteNote(note)
                }
                showDialog.value = false
            },
            onDismiss = {
                showDialog.value = false
            },
        )
    }
}

@Composable
private fun NoteListScaffold(
    username: String = "",
    isConnected: Boolean = false,
    onClickSettings: () -> Unit = {},
    onAddNotes: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        topBar = {
            BaseAppBar(
                isConnected = isConnected,
                enumScreen = NOTE_LIST,
                username = username,
                onClickSettings = onClickSettings
            )
        },
        floatingActionButton = {
            AppFloatingButton(onClick = onAddNotes)
        },
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList(),
    contentMaxLength: Int = Constant.CONTENT_LENGTH_PORTRAIT,
    onPreview: (Note) -> Unit = {},
    onDelete: (Note) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You’ve got an empty board,\nlet’s place your first note on it!",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize().padding(start = if (contentMaxLength == Constant.CONTENT_LENGTH_PORTRAIT) 0.dp else 32.dp ),
                columns = StaggeredGridCells.Fixed( if (contentMaxLength == Constant.CONTENT_LENGTH_PORTRAIT) 2 else 3 ),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(notes) { note ->
                    NoteItem(note, contentMaxLength, onDelete = onDelete, onPreview = onPreview)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    NoteListScaffold()
}

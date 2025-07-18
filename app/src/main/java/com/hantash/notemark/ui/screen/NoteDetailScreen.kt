package com.hantash.notemark.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.EditPreviewNote
import com.hantash.notemark.ui.component.NoteDetailSection
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.utils.toReadableDate
import com.hantash.notemark.viewmodel.NoteViewModel

@Composable
fun NoteDetailScreen(
    noteId: String,
    onNavWithArguments: (EnumScreen, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val modifierContent = when (localScreenOrientation.current) {
        DevicePosture.MOBILE_PORTRAIT -> Modifier.fillMaxWidth()
        else -> Modifier.width(540.dp)
    }

    val noteViewModel: NoteViewModel = hiltViewModel()
    val noteState = noteViewModel.noteStateFlow.collectAsState()

    LaunchedEffect(noteId) {
        noteViewModel.getNote(noteId)
    }

    NoteDetailScaffold(
        onNavigateBack = {
            onNavigateBack.invoke()
        },
        content = { paddingValues ->
            noteState.value?.let { note ->
                Content(
                    note = note,
                    modifier = Modifier.padding(paddingValues),
                    modifierContent = modifierContent,
                    onUpdateNote = {
                        if (noteId.isNotEmpty()) {
                            onNavWithArguments(EnumScreen.NOTE_ADD_EDIT, noteId)
                        }
                    },
                    onPreviewLandscape = {

                    }
                )
            }
        }
    )
}

@Composable
private fun NoteDetailScaffold(
    onNavigateBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        Content(modifier = Modifier.padding(paddingValues))
    }
) {
    Scaffold(
        topBar = {
            BaseAppBar(
                enumScreen = EnumScreen.NOTE_DETAIL,
                title = "ALL NOTES",
                onClickBackButton = onNavigateBack
            )
        },
        content = content
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier.fillMaxSize(),
    modifierContent: Modifier = Modifier,
    note: Note = Note(),
    onPreviewLandscape: () -> Unit = {},
    onUpdateNote: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifierContent,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = note.title,
                style = MaterialTheme.typography.titleLarge
            )

            HorizontalDivider(color = OnSurfaceOpacity12)

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NoteDetailSection(
                    modifier = Modifier.weight(1f),
                    title = "Date created",
                    description = note.createdAt.toReadableDate()
                )
                NoteDetailSection(
                    modifier = Modifier.weight(1f),
                    title = "Last edited",
                    description = note.lastEditedAt.toReadableDate()
                )
            }

            HorizontalDivider(color = OnSurfaceOpacity12)

            Text(
                modifier = Modifier.weight(1f).padding(16.dp),
                text = note.content,
                style = MaterialTheme.typography.bodyLarge
            )

            EditPreviewNote(
                modifier = Modifier.padding(bottom = 16.dp),
                onClickEdit = onUpdateNote,
                onPreviewLandscape = onPreviewLandscape
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPortrait() {
    NoteDetailScaffold()
}

@Preview(showBackground = true, widthDp = 700, heightDp = 350)
@Composable
private fun PreviewLandscape() {
    NoteDetailScaffold()
}
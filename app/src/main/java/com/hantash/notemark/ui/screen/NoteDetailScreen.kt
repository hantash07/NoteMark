package com.hantash.notemark.ui.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hantash.notemark.DevicePosture
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.component.BaseAppBar
import com.hantash.notemark.ui.component.EditReaderMode
import com.hantash.notemark.ui.component.EnumNoteMode
import com.hantash.notemark.ui.component.NoteDetailSection
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.utils.EnumDateFormater
import com.hantash.notemark.utils.localScreenOrientation
import com.hantash.notemark.utils.toReadableDate
import com.hantash.notemark.viewmodel.NoteViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NoteDetailScreen(
    noteId: String,
    onNavWithArguments: (EnumScreen, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val activity = context as? Activity

    val screenOrientation = localScreenOrientation.current
    val modifierContent = when (screenOrientation) {
        DevicePosture.MOBILE_PORTRAIT -> Modifier.fillMaxWidth()
        else -> Modifier.width(540.dp)
    }

    val noteViewModel: NoteViewModel = hiltViewModel()
    val noteState = noteViewModel.noteStateFlow.collectAsState()

    val noteMode = rememberSaveable { mutableStateOf(EnumNoteMode.VIEW) }
    val isHideAlternativeUI = rememberSaveable { mutableStateOf(false) }
    val hideUiJob = rememberSaveable { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(noteId) {
        noteViewModel.getNote(noteId)
        noteMode.value = if (noteMode.value == EnumNoteMode.READER) EnumNoteMode.READER else EnumNoteMode.VIEW
    }

    fun hideUIAfterDelay() {
        hideUiJob.value?.cancel() // cancel previous timer
        hideUiJob.value = scope.launch {
            delay(5000)
            isHideAlternativeUI.value = true
        }
    }

    NoteDetailScaffold(
        isHideAlternativeUI = isHideAlternativeUI.value,
        onNavigateBack = {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            noteMode.value = EnumNoteMode.VIEW
            isHideAlternativeUI.value = false

            onNavigateBack.invoke()
        },
        content = { paddingValues ->
            noteState.value?.let { note ->
                Content(
                    note = note,
                    modifier = Modifier.padding(paddingValues),
                    modifierContent = modifierContent,
                    isHideAlternativeUI = isHideAlternativeUI.value,
                    noteMode = noteMode.value,
                    onChangeMode = { mode ->
                        when(mode) {
                            EnumNoteMode.EDIT -> {
                                if (noteId.isNotEmpty()) {
                                    onNavWithArguments(EnumScreen.NOTE_ADD_EDIT, noteId)
                                }
                                noteMode.value = mode
                            }
                            EnumNoteMode.READER -> {
                                if (noteMode.value == mode) {
                                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                                    noteMode.value = EnumNoteMode.VIEW
                                    isHideAlternativeUI.value = false
                                }
                                if (configuration.screenWidthDp < 600 && screenOrientation == DevicePosture.MOBILE_PORTRAIT) {
                                   activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                    noteMode.value = mode
                                    isHideAlternativeUI.value = true
                                }
                            }
                            else -> {}
                        }
                    },
                    onTapScreen = {
                        isHideAlternativeUI.value = false
                        hideUIAfterDelay()
                    }
                )
            }
        }
    )
}

@Composable
private fun NoteDetailScaffold(
    isHideAlternativeUI: Boolean = false,
    onNavigateBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        Content(modifier = Modifier.padding(paddingValues))
    }
) {
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = !isHideAlternativeUI,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                BaseAppBar(
                    enumScreen = EnumScreen.NOTE_DETAIL,
                    title = "ALL NOTES",
                    onClickBackButton = onNavigateBack
                )
            }
        },
        content = content
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier.fillMaxSize(),
    modifierContent: Modifier = Modifier,
    isHideAlternativeUI: Boolean = false,
    note: Note = Note(),
    noteMode: EnumNoteMode = EnumNoteMode.VIEW,
    onChangeMode: (EnumNoteMode) -> Unit = {},
    onTapScreen: (Offset) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTapScreen
                )
            }
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
                    description = note.createdAt.toReadableDate(EnumDateFormater.DISPLAY)
                )
                NoteDetailSection(
                    modifier = Modifier.weight(1f),
                    title = "Last edited",
                    description = note.lastEditedAt.toReadableDate(EnumDateFormater.DISPLAY)
                )
            }

            HorizontalDivider(color = OnSurfaceOpacity12)

            Text(
                modifier = Modifier.weight(1f).padding(16.dp),
                text = note.content,
                style = MaterialTheme.typography.bodyLarge
            )

            AnimatedVisibility(
                visible = !isHideAlternativeUI,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                EditReaderMode(
                    modifier = Modifier.padding(bottom = 16.dp),
                    noteMode = noteMode,
                    onChangeMode = onChangeMode
                )
            }
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

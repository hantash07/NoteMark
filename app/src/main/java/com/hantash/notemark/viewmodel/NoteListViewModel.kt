package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.data.repo.SyncRepository
import com.hantash.notemark.model.Note
import com.hantash.notemark.model.SyncOperation
import com.hantash.notemark.model.SyncRecord
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.utils.debug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val syncRepository: SyncRepository,
    private val prefRepository: PreferencesRepository,
): ViewModel() {
    /*
    * If we want to convert the State into StateFlow (during initialization) inside ViewMode then stateIn() is used.
    * We can also do it without stateIn() inside ViewModel by using init() function where will collect the value of
    * the Flow and assign the emitted value into StateFlow.
    * */

    /*
    * viewModelScope means the Flow will start collecting with ViewModel's lifecycle.
    * When Viewmodel is cleared, the Flow will stop collecting.
    * */

    /*
    * SharingStarted.WhileSubscribed(5000) It decide when to start or stop collection from Flow.
    * */

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    val notesStateFlow: StateFlow<List<Note>> = noteRepository.notesFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)

            // Before deleting first check if it is already available in SyncRecord.
            // If it is available delete from SyncRecord. Otherwise create a new SyncRecord with DELETE type.
            var record = syncRepository.fetch(note.id)
            if (record != null) {
                syncRepository.deleteSync(record)
            } else {
                record = SyncRecord(
                    userId = prefRepository.userId.firstOrNull() ?: "",
                    noteId = note.id,
                    operation = SyncOperation.DELETE,
                    payload = note.payload()
                )
                syncRepository.addSync(record)
            }
        }
    }

    fun showConfirmationDialog(note: Note) {
        viewModelScope.launch {
            _uiEventFlow.emit(UiEvent.DeleteNote(note))
        }
    }

    fun requestGetNotes() {
        viewModelScope.launch {
            val notes = noteRepository.notesFlow.firstOrNull()
            if (notes.isNullOrEmpty()) {
                noteRepository.requestGetNotes().collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.value = UiState.Loading
                        }
                        is Resource.Error -> {
                            debug("${result.message}")
                            debug("Error: ADD NOTE API: ${result.code} / ${result.message}")
                            _uiState.value = UiState.Idle
                            _uiEventFlow.emit(UiEvent.ShowSnackBar(result.message ?: ""))
                        }
                        is Resource.Success -> {
                            result.data?.notes?.let { notes ->
                                _uiState.value = UiState.Success(notes)
                                updateNotes(notes)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateNotes(notes: List<Note>) {
        debug("NOTES: ${notes.size}")
        viewModelScope.launch {
            if (notes.isNotEmpty()) {
                noteRepository.clearNotes()
                noteRepository.addNote(notes)
            }
        }
    }
}
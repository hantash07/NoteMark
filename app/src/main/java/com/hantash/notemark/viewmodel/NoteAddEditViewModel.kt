package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.data.repo.SyncRepository
import com.hantash.notemark.model.Note
import com.hantash.notemark.model.SyncOperation
import com.hantash.notemark.model.SyncRecord
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class NoteAddEditViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val syncRepository: SyncRepository,
    private val prefRepository: PreferencesRepository,
): ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    private val _noteStateFlow = MutableStateFlow<Note?>(null)
    val noteStateFlow: StateFlow<Note?> = _noteStateFlow.asStateFlow()

//    init {
//        viewModelScope.launch {
//            noteRepository.clearNotes()
//            syncRepository.clearRecords()
//        }
//    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addNote(note)

            // Now add the detail to SyncRecord
            syncRepository.addSync(SyncRecord(
                userId = prefRepository.userId.firstOrNull() ?: "",
                noteId = note.id,
                operation = SyncOperation.CREATE,
                payload = note.payload()
            ))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)

            // Before updating first check if it is already available in SyncRecord with SyncRecord.CREATE.
            // If it is available only update the payload of existing record. Otherwise create a new SyncRecord with UPDATE type.
            var record = syncRepository.fetch(note.id, SyncOperation.CREATE)
            if (record != null) {
                record.payload = note.payload()
                record.timestamp = Instant.now().toEpochMilli()
                syncRepository.updateSync(record)
            } else {
                record = SyncRecord(
                    userId = prefRepository.userId.firstOrNull() ?: "",
                    noteId = note.id,
                    operation = SyncOperation.UPDATE,
                    payload = note.payload()
                )
                syncRepository.addSync(record)
            }
        }
    }

    fun getNote(id: String) {
        viewModelScope.launch {
            noteRepository.fetchById(id).collect {
                _noteStateFlow.value = it
            }
        }
    }

    suspend fun isNoteUpdated(note: Note): Boolean {
        val noteDB = noteRepository.fetchById(note.id).firstOrNull()

        noteDB?.let {
            return it.title.lowercase().trim() != note.title.lowercase().trim()
                    || it.content.lowercase().trim() != note.content.lowercase().trim()
        }

        return false
    }
}
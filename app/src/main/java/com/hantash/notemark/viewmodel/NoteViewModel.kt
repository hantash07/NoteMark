package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.model.DataOrigin
import com.hantash.notemark.model.DataOrigin.LOCAL
import com.hantash.notemark.model.DataOrigin.REMOTE
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.utils.debug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
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

    private val _noteStateFlow = MutableStateFlow<Note?>(null)
    val noteStateFlow: StateFlow<Note?> = _noteStateFlow.asStateFlow()

    private val _notesUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val notesUiState: StateFlow<UiState<Unit>> = _notesUiState.asStateFlow()

    private val _syncUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val syncUiState: StateFlow<UiState<Unit>> = _syncUiState.asStateFlow()

    private val _notesState = MutableStateFlow<List<Note>>(emptyList())
    val notesState: StateFlow<List<Note>> = _notesState
//        noteRepository.notesFlow
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            emptyList()
//        )

    val lastSyncStateFlow: StateFlow<Instant?> = preferencesRepository.lastSyncFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    init {
        viewModelScope.launch {
            noteRepository.notesFlow.collect { notes ->
                _notesState.value = notes
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addNoteLocal(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNoteLocal(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            /*
            * If the data origin of note is REMOTE, then make soft deletion.
            * Otherwise, make hard deletion.
            * */
            if (note.origin == DataOrigin.REMOTE) {
                note.isSync = false
                note.isDeleted = true
                noteRepository.updateNoteLocal(note)
            } else {
                noteRepository.deleteNoteLocal(note)
            }
        }
    }

    fun clearNotes() {
        viewModelScope.launch {
            noteRepository.clearNotes()
        }
    }

    fun getNote(id: String) {
        viewModelScope.launch {
            noteRepository.fetchById(id).collect {
                _noteStateFlow.value = it
            }
        }
    }

    fun showConfirmationDialog(note: Note) {
        viewModelScope.launch {
            _uiEventFlow.emit(UiEvent.DeleteNote(note))
        }
    }

    fun saveLastSync(date: Instant) {
        viewModelScope.launch {
            preferencesRepository.savaLastSync(date)
        }
    }

    suspend fun isNoteUpdated(note: Note): Boolean {
        val noteDB = noteRepository.fetchById(note.id.toString()).firstOrNull()

        noteDB?.let {
            return it.title.lowercase().trim() != note.title.lowercase().trim()
                    || it.content.lowercase().trim() != note.content.lowercase().trim()
        }

        return false
    }

    fun getNotesFromRemote() {
        viewModelScope.launch {
            noteRepository.getNotes().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _notesUiState.value = UiState.Loading
                    }
                    is Resource.Error -> {
                        _notesUiState.value = UiState.Idle
                        _uiEventFlow.emit(UiEvent.ShowSnackBar(result.message ?: ""))
                    }
                    is Resource.Success -> {
                        _notesUiState.value = UiState.Success(result.data)
                        _notesState.value = result.data ?: emptyList()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun syncPendingNotes() {
        viewModelScope.launch {
            _syncUiState.value = UiState.Loading

            noteRepository.getPendingNotes()
                .take(1) // In ensures to only process one note at a time
                .flatMapConcat { notes -> notes.asFlow() } // It executes the inner flow in a sequential manner.
                .collect { note ->
                    try {
                        if (note.isDeleted) {
                            debug("DELETING NOTE -> ${note.id}")
                            noteRepository.deleteNote(note).collect { result ->
                                when(result) {
                                    is Resource.Loading -> {}
                                    is Resource.Error -> {
                                        debug("Error: DELETE Note API: ${result.code} / ${result.message}")
                                    }
                                    is Resource.Success -> {
                                        noteRepository.deleteNoteLocal(note)
                                    }
                                }
                            }
                        } else {

                            if (note.origin == LOCAL) {
                                debug("ADDING NOTE -> ${note.id}")
                                noteRepository.addNote(note).collect { result ->
                                    when(result) {
                                        is Resource.Loading -> {}
                                        is Resource.Error -> {
                                            debug("Error: ADD NOTE API: ${result.code} / ${result.message}")
                                        }
                                        is Resource.Success -> {
                                            result.data?.let { note ->
                                                note.origin = REMOTE
                                                note.isSync = true
                                                noteRepository.updateNote(note)
                                            }
                                        }
                                    }
                                }
                            } else { //Origin is Remote
                                debug("Updating NOTE -> ${note.id}")
                                noteRepository.updateNote(note).collect { result ->
                                    when(result) {
                                        is Resource.Loading -> {}
                                        is Resource.Error -> {
                                            debug("Error: Update Note API: ${result.code} / ${result.message}")
                                        }
                                        is Resource.Success -> {
                                            result.data?.let { note ->
                                                note.origin = REMOTE
                                                note.isSync = true
                                                noteRepository.updateNote(note)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        debug("FAILED SYNCS: ${e.localizedMessage}")
                        _syncUiState.value = UiState.Idle
                        _uiEventFlow.emit(UiEvent.ShowSnackBar(e.localizedMessage ?: ""))
                    }
                }

            _syncUiState.value = UiState.Success(Unit)
            saveLastSync(Instant.now()) //Saving last sync date
        }
    }
}
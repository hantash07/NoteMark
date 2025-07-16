package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {
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

    val notesStateFlow: StateFlow<List<Note>> = noteRepository.notesFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun showConfirmationDialog(note: Note) {
        viewModelScope.launch {
            _uiEventFlow.emit(UiEvent.DeleteNote(note))
        }
    }

}
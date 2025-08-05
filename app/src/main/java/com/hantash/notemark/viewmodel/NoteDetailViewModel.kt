package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.SyncRepository
import com.hantash.notemark.model.Note
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val syncRepository: SyncRepository,
): ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    private val _noteStateFlow = MutableStateFlow<Note?>(null)
    val noteStateFlow: StateFlow<Note?> = _noteStateFlow.asStateFlow()

    fun getNote(id: String) {
        viewModelScope.launch {
            noteRepository.fetchById(id).collect {
                _noteStateFlow.value = it
            }
        }
    }
}
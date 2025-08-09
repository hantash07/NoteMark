package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.data.repo.SyncRepository
import com.hantash.notemark.model.SyncOperation
import com.hantash.notemark.ui.common.DialogEvent
import com.hantash.notemark.ui.common.DialogState
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.ui.component.SyncInterval
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
class SettingsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val syncRepository: SyncRepository,
    private val prefRepository: PreferencesRepository,
): ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    private val _dialogEvent = MutableSharedFlow<DialogEvent>()
    val dialogEvent = _dialogEvent.asSharedFlow()

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    val syncIntervalStateFlow: StateFlow<SyncInterval> = prefRepository.syncIntervalFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SyncInterval.ManualOnly
        )

    val lastSyncStateFlow: StateFlow<Instant?> = prefRepository.lastSyncFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    fun showDialog(
        title: String,
        message: String,
        confirmText: String,
        dismissText: String,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        _dialogState.value = DialogState(
            isVisible = true,
            title = title,
            message = message,
            confirmText = confirmText,
            dismissText = dismissText,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }

    fun hideDialog() {
        _dialogState.value = DialogState()
    }

    fun saveSyncInterval(syncInterval: SyncInterval) {
        viewModelScope.launch {
            prefRepository.saveSyncInterval(syncInterval)
        }
    }

    suspend fun isSyncPending(): Boolean {
        val sync = syncRepository.fetchByUserId(prefRepository.userId.firstOrNull() ?: "").firstOrNull()
        return !sync.isNullOrEmpty()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun requestSyncRecords(isLogoutAttempted: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            debug("SYNC STARTED")

            // Getting all records needed to be synced into remote based on logged in user.
            syncRepository.fetchByUserId(prefRepository.userId.firstOrNull() ?: "")
                .take(1) // In ensures to only process one note at a time
                .flatMapConcat { records -> records.asFlow() } // It executes the inner flow in a sequential manner.
                .collect { record ->
                    try {
                        when (record.operation) {
                            SyncOperation.CREATE -> {
                                debug("ADDING NOTE -> ${record.noteId}")
                                noteRepository.requestAddNote(record.payload).collect { result ->
                                    when(result) {
                                        is Resource.Loading -> {}
                                        is Resource.Error -> {
                                            debug("Error: ADD NOTE API: ${result.code} / ${result.message}")
                                        }
                                        is Resource.Success -> {
                                            debug("ADDING NOTE -> SUCCESS -> ${result.data?.id}")
                                            result.data?.let {
                                                syncRepository.deleteSync(record)
                                            }
                                        }
                                    }
                                }
                            }
                            SyncOperation.UPDATE -> {
                                debug("UPDATING NOTE -> ${record.noteId}")
                                noteRepository.requestUpdateNote(record.payload).collect { result ->
                                    when(result) {
                                        is Resource.Loading -> {}
                                        is Resource.Error -> {
                                            debug("Error: UPDATE NOTE API: ${result.code} / ${result.message}")
                                        }
                                        is Resource.Success -> {
                                            debug("UPDATING NOTE -> SUCCESS -> ${result.data?.id}")
                                            result.data?.let {
                                                syncRepository.deleteSync(record)
                                            }
                                        }
                                    }
                                }
                            }
                            SyncOperation.DELETE -> {
                                debug("UPDATING NOTE -> ${record.noteId}")
                                noteRepository.requestDeleteNote(record.noteId).collect { result ->
                                    when(result) {
                                        is Resource.Loading -> {}
                                        is Resource.Error -> {
                                            debug("Error: DELETE NOTE API: ${result.code} / ${result.message}")
                                        }
                                        is Resource.Success -> {
                                            debug("DELETING NOTE -> SUCCESS")
                                            result.data?.let {
                                                syncRepository.deleteSync(record)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        debug("FAILED SYNCS: ${e.localizedMessage}")
                        _uiState.value = UiState.Idle
                        _uiEvent.emit(UiEvent.ShowSnackBar(e.localizedMessage ?: ""))
                    }
                }

            if (isSyncPending()) { // Sync is Failed
                _uiState.value = UiState.Idle
                if (isLogoutAttempted) {
                    _dialogEvent.emit(DialogEvent.SyncFailed)
                }
            } else { // Sync Successfully
                _uiState.value = UiState.Success(Unit)
                if (isLogoutAttempted) {
                    _dialogEvent.emit(DialogEvent.SyncSuccess)
                }

                prefRepository.savaLastSync(Instant.now()) //Saving last sync date
            }
            debug("SYNC COMPLETED")
        }
    }
}
package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.repo.AuthRepository
import com.hantash.notemark.data.repo.NoteRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.ui.navigation.EnumScreen
import com.hantash.notemark.utils.debug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prefRepository: PreferencesRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {
    /*
    * The flows used in this class are Hot Flow.
    * Hot flows means that the flow start emitting without the collector. The flow start its execution even if there is no Collector
    * StateFlow is a state holder same behaviour like LiveData.
    * SharedFlow is an event broadcaster same like broadcast event.
    * */

    private val _uiSignUpState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiSignUpState: StateFlow<UiState<Unit>> = _uiSignUpState.asStateFlow()

    private val _uiLoginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiLoginState: StateFlow<UiState<Unit>> = _uiLoginState.asStateFlow()

    private val _uiLogoutState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiLogoutState: StateFlow<UiState<Unit>> = _uiLogoutState.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _isUserLoggedInState = MutableStateFlow<Boolean?>(null)
    val isUserLoggedInState: StateFlow<Boolean?> = _isUserLoggedInState.asStateFlow()

    private val _usernameState = MutableStateFlow("")
    val usernameState: StateFlow<String> = _usernameState.asStateFlow()

    init {
        viewModelScope.launch {
            prefRepository.accessToken.collect { token ->
                _isUserLoggedInState.value = token.isNotBlank()
            }
        }

        viewModelScope.launch {
            prefRepository.username.collect { username ->
                _usernameState.value = username
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(username, email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiSignUpState.value = UiState.Loading
                    }

                    is Resource.Success -> {
                        _uiSignUpState.value = UiState.Success(result.data)
                        _uiEventFlow.emit(UiEvent.Authenticate(email, password))
                    }

                    is Resource.Error -> {
                        _uiSignUpState.value = UiState.Idle
                        _uiEventFlow.emit(UiEvent.ShowSnackBar(result.message ?: ""))
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiLoginState.value = UiState.Loading
                    }

                    is Resource.Success -> {
                        val user = result.data
                        _uiLoginState.value = UiState.Success(user)
                        _uiEventFlow.emit(UiEvent.Navigate(EnumScreen.NOTE_LIST))

                        prefRepository.save(user?.username ?: "", user?.accessToken ?: "", user?.refreshToken ?: "")
                    }

                    is Resource.Error -> {
                        _uiLoginState.value = UiState.Idle
                        _uiEventFlow.emit(UiEvent.ShowSnackBar(result.message ?: ""))
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val refreshToken = prefRepository.refreshToken.first()

            authRepository.logout(refreshToken).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiLogoutState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _uiLogoutState.value = UiState.Success(result.data)
                        _uiEventFlow.emit(UiEvent.Navigate(EnumScreen.LOGIN))

                        noteRepository.clearNotes()
                        prefRepository.clear()
                    }
                    is Resource.Error -> {
                        _uiLogoutState.value = UiState.Idle
                        _uiEventFlow.emit(UiEvent.ShowSnackBar(result.message ?: ""))
                    }
                }
            }
        }
    }
}


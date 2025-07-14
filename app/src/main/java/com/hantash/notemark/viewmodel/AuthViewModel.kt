package com.hantash.notemark.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.repo.AuthRepository
import com.hantash.notemark.data.repo.PreferencesRepository
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.ui.navigation.EnumScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prefRepository: PreferencesRepository
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

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _isUserLoggedInState = MutableStateFlow<Boolean?>(null)
    val isUserLoggedInState: StateFlow<Boolean?> = _isUserLoggedInState.asStateFlow()

    init {
        viewModelScope.launch {
            prefRepository.accessToken.collect { token ->
                _isUserLoggedInState.value = token.isNotBlank()
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

                        prefRepository.saveUserDetail(username, email, password)
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
                        val authToken = result.data
                        _uiLoginState.value = UiState.Success(authToken)
                        _uiEventFlow.emit(UiEvent.Navigate(EnumScreen.NOTE_LIST))

                        prefRepository.saveAuthDetail(authToken?.accessToken ?: "", authToken?.refreshToken ?: "")
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
            prefRepository.clear()
        }
    }
}


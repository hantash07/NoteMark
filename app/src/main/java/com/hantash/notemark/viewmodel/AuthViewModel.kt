package com.hantash.notemark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hantash.notemark.data.api.Resource
import com.hantash.notemark.data.repo.AuthRepository
import com.hantash.notemark.ui.common.UiEvent
import com.hantash.notemark.ui.common.UiState
import com.hantash.notemark.ui.navigation.EnumScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _uiSignUpState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiSignUpState: StateFlow<UiState<Unit>> = _uiSignUpState.asStateFlow()

    private val _uiLoginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiLoginState: StateFlow<UiState<Unit>> = _uiLoginState.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(username, email, password).collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiSignUpState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _uiSignUpState.value = UiState.Success(result.data)
                        _uiEventFlow.emit(UiEvent.Navigate(EnumScreen.NOTE_LIST.name))
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
                when(result) {
                    is Resource.Loading -> {
                        _uiLoginState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _uiLoginState.value = UiState.Success(result.data)
                        _uiEventFlow.emit(UiEvent.Navigate(EnumScreen.NOTE_LIST.name))
                    }
                    is Resource.Error -> {
                        _uiLoginState.value = UiState.Idle
                        _uiEventFlow.emit(UiEvent.ShowSnackBar(result.message ?: ""))
                    }
                }
            }
        }
    }
}


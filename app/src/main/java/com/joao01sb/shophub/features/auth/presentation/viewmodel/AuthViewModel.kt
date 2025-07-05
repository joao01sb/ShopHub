package com.joao01sb.shophub.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.features.auth.presentation.event.AuthEvent
import com.joao01sb.shophub.features.auth.presentation.event.AuthUiEvent
import com.joao01sb.shophub.features.auth.domain.usecase.IsUserLoggedInUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LoginUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.RegisterUseCase
import com.joao01sb.shophub.features.auth.presentation.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> _uiState.update { currentState ->
                currentState.copy(email = event.email)
            }

            is AuthEvent.PasswordChanged -> _uiState.update { currentState ->
                currentState.copy(password = event.password)
            }

            is AuthEvent.NameChanged -> _uiState.update { currentState ->
                currentState.copy(name = event.name)
            }

            is AuthEvent.Login -> login()
            is AuthEvent.Register -> register()
            is AuthEvent.SingUp -> {
                viewModelScope.launch {
                    clearState()
                    _uiEvent.emit(AuthUiEvent.NavigateToRegister)
                }
            }
            is AuthEvent.NavigateToLogin -> {
                viewModelScope.launch {
                    _uiEvent.emit(AuthUiEvent.NavigateToLogin)
                }
            }
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            isUserLoggedInUseCase()
                .onSuccess { isLoggedIn ->
                    _isAuthenticated.value = isLoggedIn
                    if (isLoggedIn) {
                        _uiEvent.emit(AuthUiEvent.NavigateToHome)
                    }
                }
                .onFailure {
                    _isAuthenticated.value = false
                }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, error = null)
            }

            loginUseCase(email = uiState.value.email, password = uiState.value.password)
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
                .onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    _isAuthenticated.value = true
                    _uiEvent.emit(AuthUiEvent.NavigateToHome)
                }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, error = null)
            }

            registerUseCase(uiState.value.email, uiState.value.password, uiState.value.name)
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
                .onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    clearState()
                    _uiEvent.emit(AuthUiEvent.NavigateToLogin)
                }
        }
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }
}
package com.joao01sb.shophub.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.utils.auth.AuthEvent
import com.joao01sb.shophub.core.utils.auth.AuthUiEvent
import com.joao01sb.shophub.features.auth.domain.usecase.LoginUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.RegisterUseCase
import com.joao01sb.shophub.features.auth.presentation.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow< AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()



    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> _uiState.update { currentState ->
                currentState.copy(email = event.email)
            }

            is AuthEvent.PasswordChanged -> _uiState.update { currentState ->
                currentState.copy(
                    password = event.password
                )
            }

            is AuthEvent.NameChanged -> _uiState.update { currentState ->
                currentState.copy(name = event.name)
            }

            is AuthEvent.Login -> login()
            is AuthEvent.Register -> register()
            is AuthEvent.SingUp -> singUp()
        }
    }

    private fun singUp() {
        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.NavigateToRegister)
        }
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            loginUseCase(email = uiState.value.email, password = uiState.value.password)
                .onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = it.message
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
                    _uiEvent.emit(AuthUiEvent.NavigateToHome)
                }
        }
    }


    private fun register() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            registerUseCase(uiState.value.email, uiState.value.password, uiState.value.name)
                .onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
                }
                .onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false
                        )
                    }
                    _uiEvent.emit(AuthUiEvent.NavigateToLogin)
                }
        }

    }
}
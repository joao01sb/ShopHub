package com.joao01sb.shophub.core.utils.auth

sealed class AuthUiEvent {
    object NavigateToLogin : AuthUiEvent()
    object NavigateToHome : AuthUiEvent()
    object NavigateToRegister : AuthUiEvent()
    data class ShowSnackbar(val message: String) : AuthUiEvent()
}

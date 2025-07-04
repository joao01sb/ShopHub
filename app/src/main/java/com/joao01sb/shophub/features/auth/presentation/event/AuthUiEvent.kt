package com.joao01sb.shophub.features.auth.presentation.event


sealed class AuthUiEvent {
    data object NavigateToLogin : AuthUiEvent()
    data object NavigateToRegister : AuthUiEvent()
    data object NavigateToHome : AuthUiEvent()
}

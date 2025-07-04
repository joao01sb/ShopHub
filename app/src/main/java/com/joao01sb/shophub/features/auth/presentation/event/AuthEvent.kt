package com.joao01sb.shophub.features.auth.presentation.event

sealed class AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class NameChanged(val name: String) : AuthEvent()

    data object Login : AuthEvent()
    data object Register : AuthEvent()
    data object SingUp : AuthEvent()
    data object NavigateToLogin : AuthEvent()
}

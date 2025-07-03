package com.joao01sb.shophub.core.utils.auth

sealed class AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class NameChanged(val name: String) : AuthEvent()
    object SingUp : AuthEvent()
    object Login : AuthEvent()
    object Register : AuthEvent()
}

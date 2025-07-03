package com.joao01sb.shophub.features.auth.presentation.state

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

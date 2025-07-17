package com.joao01sb.shophub.core.domain.manager

import com.joao01sb.shophub.features.auth.domain.usecase.GetCurrentUserIdUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.IsUserLoggedInUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LoginUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LogoutUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.RegisterUseCase

class AuthManager(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val currentUserUseCase: GetCurrentUserIdUseCase,
    private val isLoggedInUseCase: IsUserLoggedInUseCase
) {

    suspend fun registerUser(email: String, password: String, name: String) = registerUseCase(email, password, name)
    suspend fun loginUser(email: String, password: String) = loginUseCase(email, password)
    suspend fun logoutUser() = logoutUseCase()
    fun getCurrentUserId() = currentUserUseCase()
    fun isUserLoggedIn() = isLoggedInUseCase()

}
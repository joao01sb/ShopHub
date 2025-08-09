package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String, name: String): DomainResult<Unit> {

        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            return DomainResult.Error(
                message = "Email, password, and name cannot be empty",
                type = ErrorType.VALIDATION
            )
        }

        when (val authResult = repository.register(email, password, name)) {
            is DomainResult.Success -> {
                val uid = repository.getUserId()
                return if (uid != null) {
                    when (val userResult = repository.saveUser(uid, email, name)) {
                        is DomainResult.Success -> DomainResult.Success(Unit)
                        is DomainResult.Error -> {
                            repository.logout()
                            userResult
                        }
                    }
                } else {
                    DomainResult.Error(
                        message = "Failed to get user ID after registration",
                        type = ErrorType.AUTHENTICATION
                    )
                }
            }
            is DomainResult.Error -> return authResult
        }
    }

}
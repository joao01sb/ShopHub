package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String, name: String): DomainResult<Unit> {
        when (val authResult = repository.register(email, password, name)) {
            is DomainResult.Success -> {
                val uid = repository.getUserId()
                if (uid != null) {
                    return when (val userResult = repository.saveUser(uid, email, name)) {
                        is DomainResult.Success -> DomainResult.Success(Unit)
                        is DomainResult.Error -> {
                            repository.logout()
                            userResult
                        }
                    }
                } else {
                    return DomainResult.Error(
                        message = "Failed to get user ID after registration",
                        type = ErrorType.AUTHENTICATION
                    )
                }
            }
            is DomainResult.Error -> return authResult
        }
    }

}
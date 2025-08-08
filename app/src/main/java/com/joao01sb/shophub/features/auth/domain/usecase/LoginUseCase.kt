package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): DomainResult<Unit> {
        return when(val result = repository.login(email, password)) {
            is DomainResult.Success -> DomainResult.Success(Unit)
            is DomainResult.Error -> result
        }
    }

}
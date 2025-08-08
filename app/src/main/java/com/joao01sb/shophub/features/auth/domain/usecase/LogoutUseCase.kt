package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() : DomainResult<Unit> {
        return when(val result = repository.logout()) {
            is DomainResult.Success -> result
            is DomainResult.Error -> result
        }
    }

}
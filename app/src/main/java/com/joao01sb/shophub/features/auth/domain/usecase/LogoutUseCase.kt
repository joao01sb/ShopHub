package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() : Result<Unit> {
        return try {
            repository.logout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class GetCurrentUserIdUseCase(
    private val repository: AuthRepository
) {

    operator fun invoke() : Result<String> {
        return try {
            repository.getUserId()?.let {
                Result.success(it)
            } ?: Result.failure(NullPointerException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
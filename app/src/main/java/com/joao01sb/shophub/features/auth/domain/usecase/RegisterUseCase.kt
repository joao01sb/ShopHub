package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String, name: String): Result<Unit> {
        return try {
            repository.register(
                email = email,
                password = password,
                name = name
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
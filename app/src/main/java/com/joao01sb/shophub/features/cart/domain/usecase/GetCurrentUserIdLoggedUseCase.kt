package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class GetCurrentUserIdLoggedUseCase(
    private val cartRepository: CartRepository
) {

    operator fun invoke() : Result<String> {
        return try {
            cartRepository.getCurrentUserId()?.let {
                Result.success(it)
            } ?: Result.failure(NullPointerException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
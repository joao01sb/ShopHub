package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class ClearCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String) : Result<Unit> {
        return try {
            cartRepository.clearCart(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
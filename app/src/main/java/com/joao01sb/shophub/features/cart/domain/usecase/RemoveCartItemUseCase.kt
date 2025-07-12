package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class RemoveCartItemUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String, productId: String) : Result<Unit> {
        return try {
            cartRepository.removeItem(userId, productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
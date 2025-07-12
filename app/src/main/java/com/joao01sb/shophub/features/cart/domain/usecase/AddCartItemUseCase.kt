package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class AddCartItemUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String, item: CartItem) : Result<Unit> {
        return try {
            cartRepository.addOrUpdateItem(userId, item)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
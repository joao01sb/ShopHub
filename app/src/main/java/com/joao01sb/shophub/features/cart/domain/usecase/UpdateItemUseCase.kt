package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class UpdateItemUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String, item: CartItem, quantity: Int) : Result<Unit> {
        return try {
            val updatedItem = item.copy(quantidade = quantity)
            cartRepository.updateItem(userId, updatedItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
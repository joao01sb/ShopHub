package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class GetCartItemsUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String): Result<List<CartItem>> {
        return try {
            val cartItems = cartRepository.getCartItems(userId)
            Result.success(cartItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
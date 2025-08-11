package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCartItemsUseCase(
    private val cartRepository: CartRepository
) {

    operator fun invoke(userId: String): Flow<List<CartItem>> {
        return cartRepository
            .observeCartItems(userId)
            .map { items ->
                items.sortedBy { it.name }
            }
    }

}
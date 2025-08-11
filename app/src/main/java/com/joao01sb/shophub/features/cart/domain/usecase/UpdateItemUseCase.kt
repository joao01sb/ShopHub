package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class UpdateItemUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String, item: CartItem, quantity: Int) : DomainResult<Unit> {
        return when (val result = cartRepository.updateItem(userId, item)) {
            is DomainResult.Success -> result
            is DomainResult.Error -> result
        }
    }

}
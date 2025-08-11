package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class RemoveCartItemUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String, productId: String) : DomainResult<Unit> {
        return when (val result = cartRepository.removeItem(userId, productId)) {
            is DomainResult.Success -> DomainResult.Success(Unit)
            is DomainResult.Error -> result
        }
    }

}
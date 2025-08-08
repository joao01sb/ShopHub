package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class ClearCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String) : DomainResult<Unit> {
        return when(val result = cartRepository.clearCart(userId)) {
            is DomainResult.Success -> result
            is DomainResult.Error -> result
        }
    }

}
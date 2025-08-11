package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class PlaceOrderUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(userId: String, items: List<CartItem>, info: CheckoutInfo) : DomainResult<Unit> {
        return when (val result = cartRepository.placeOrder(userId, items, info)) {
            is DomainResult.Success -> DomainResult.Success(Unit)
            is DomainResult.Error -> result
        }
    }

}
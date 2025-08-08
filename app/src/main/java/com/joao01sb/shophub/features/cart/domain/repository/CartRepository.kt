package com.joao01sb.shophub.features.cart.domain.repository

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun observeCartItems(userId: String): Flow<List<CartItem>>
    suspend fun updateItem(userId: String, item: CartItem) : DomainResult<Unit>
    suspend fun removeItem(userId: String, productId: String) : DomainResult<Unit>
    suspend fun clearCart(userId: String) : DomainResult<Unit>
    suspend fun placeOrder(userId: String, items: List<CartItem>, info: CheckoutInfo) : DomainResult<Unit>
    fun getCurrentUserId(): String?

}
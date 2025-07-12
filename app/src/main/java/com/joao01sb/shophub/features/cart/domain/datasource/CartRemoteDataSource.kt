package com.joao01sb.shophub.features.cart.domain.datasource

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

interface CartRemoteDataSource {
    suspend fun getCartItems(userId: String): List<CartItem>
    suspend fun addOrUpdateItem(userId: String, item: CartItem)
    suspend fun removeItem(userId: String, productId: String)
    suspend fun clearCart(userId: String)
    suspend fun placeOrder(userId: String, items: List<CartItem>, info: CheckoutInfo)
}

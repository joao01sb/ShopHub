package com.joao01sb.shophub.features.cart.domain.datasource

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import kotlinx.coroutines.flow.Flow

interface CartRemoteDataSource {
    fun observeCartItems(userId: String): Flow<List<CartItem>>
    suspend fun updateItem(userId: String, item: CartItem) : FirebaseResult<Unit>
    suspend fun removeItem(userId: String, productId: String) : FirebaseResult<Unit>
    suspend fun clearCart(userId: String) : FirebaseResult<Unit>
    suspend fun placeOrder(userId: String, items: List<CartItem>, info: CheckoutInfo) : FirebaseResult<Unit>
    fun getCurrentUserId(): String?
}

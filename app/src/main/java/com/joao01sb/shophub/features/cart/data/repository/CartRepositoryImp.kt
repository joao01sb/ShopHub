package com.joao01sb.shophub.features.cart.data.repository

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.datasource.CartRemoteDataSource
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepositoryImp(
    private val remoteDataSource: CartRemoteDataSource
) : CartRepository {

    override fun observeCartItems(userId: String): Flow<List<CartItem>> {
        return remoteDataSource.observeCartItems(userId)
    }

    override suspend fun updateItem(
        userId: String,
        item: CartItem
    ) {
        remoteDataSource.updateItem(userId, item)
    }

    override suspend fun removeItem(userId: String, productId: String) {
        remoteDataSource.removeItem(userId, productId)
    }

    override suspend fun clearCart(userId: String) {
        remoteDataSource.clearCart(userId)
    }

    override suspend fun placeOrder(
        userId: String,
        items: List<CartItem>,
        info: CheckoutInfo
    ) {
        remoteDataSource.placeOrder(userId, items, info)
    }

    override fun getCurrentUserId(): String? {
        return remoteDataSource.getCurrentUserId()
    }
}
package com.joao01sb.shophub.features.cart.data.repository

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.datasource.CartRemoteDataSource
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository

class CartRepositoryImp(
    private val remoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun getCartItems(userId: String): List<CartItem> {
        return remoteDataSource.getCartItems(userId)
    }

    override suspend fun addOrUpdateItem(
        userId: String,
        item: CartItem
    ) {
        remoteDataSource.addOrUpdateItem(userId, item)
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
}
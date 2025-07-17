package com.joao01sb.shophub.core.domain.manager

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.usecase.ClearCartUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.GetCartItemsUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.PlaceOrderUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.RemoveCartItemUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.UpdateItemUseCase
import kotlinx.coroutines.flow.Flow

class CartManager(
    private val updateItemUseCase: UpdateItemUseCase,
    private val removeItemUseCase: RemoveCartItemUseCase,
    private val getItemsUseCase: GetCartItemsUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
) {
    suspend fun addItem(userId: String, item: CartItem, quantity: Int = 1) = updateItemUseCase(userId, item, quantity)

    suspend fun removeItem(userId: String, productId: String) = removeItemUseCase(userId, productId)

    fun getItems(userId: String): Flow<List<CartItem>> = getItemsUseCase(userId)

    suspend fun clearCart(userId: String) = clearCartUseCase(userId)

    suspend fun placeOrder(userId: String, items: List<CartItem>, info: CheckoutInfo) =
        placeOrderUseCase(userId, items, info)
}

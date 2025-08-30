package com.joao01sb.shophub.features.cart.data.fake

import com.google.firebase.firestore.FirebaseFirestoreException
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartRepository : CartRepository {

    private var shouldReturnError = false
    private var cartItems = mutableListOf<CartItem>()
    private var currentUserId: String? = null

    private var clearCartCallCount = 0
    private var updateItemCallCount = 0
    private var removeItemCallCount = 0
    private var placeOrderCallCount = 0
    private var observeCartItemsCallCount = 0
    private var lastUpdatedItem: CartItem? = null
    private var lastRemovedProductId: String? = null

    fun getClearCartCallCount() = clearCartCallCount
    fun getUpdateItemCallCount() = updateItemCallCount
    fun getRemoveItemCallCount() = removeItemCallCount
    fun getPlaceOrderCallCount() = placeOrderCallCount
    fun getObserveCartItemsCallCount() = observeCartItemsCallCount
    fun getLastUpdatedItem() = lastUpdatedItem
    fun getLastRemovedProductId() = lastRemovedProductId

    fun resetCounters() {
        clearCartCallCount = 0
        updateItemCallCount = 0
        removeItemCallCount = 0
        placeOrderCallCount = 0
        observeCartItemsCallCount = 0
        lastUpdatedItem = null
        lastRemovedProductId = null
        shouldReturnError = false
    }

    fun setErrorState(shouldReturnError: Boolean) {
        this.shouldReturnError = shouldReturnError
    }

    fun setCartItems(items: List<CartItem>) {
        cartItems = items.toMutableList()
    }

    override fun observeCartItems(userId: String): Flow<List<CartItem>> = flow {
        observeCartItemsCallCount++
        if (shouldReturnError) {
            throw FirebaseFirestoreException(
                "Error observing cart items",
                FirebaseFirestoreException.Code.UNKNOWN
            )
        }
        emit(cartItems)
    }

    override suspend fun updateItem(userId: String, item: CartItem): DomainResult<Unit> {
        updateItemCallCount++
        lastUpdatedItem = item
        return if (shouldReturnError) {
            DomainResult.Error("Error updating item", ErrorType.UNKNOWN)
        } else {
            val index = cartItems.indexOfFirst { it.productId == item.productId }
            if (index != -1) {
                cartItems[index] = item
            } else {
                cartItems.add(item)
            }
            DomainResult.Success(Unit)
        }
    }

    override suspend fun removeItem(userId: String, productId: String): DomainResult<Unit> {
        removeItemCallCount++
        lastRemovedProductId = productId
        return if (shouldReturnError) {
            DomainResult.Error("Error removing item", ErrorType.UNKNOWN)
        } else {
            cartItems.removeIf { it.productId.toString() == productId }
            DomainResult.Success(Unit)
        }
    }

    override suspend fun clearCart(userId: String): DomainResult<Unit> {
        clearCartCallCount++
        return if (shouldReturnError) {
            DomainResult.Error("Error clearing cart", ErrorType.UNKNOWN)
        } else {
            cartItems.clear()
            DomainResult.Success(Unit)
        }
    }

    override suspend fun placeOrder(
        userId: String,
        items: List<CartItem>,
        info: CheckoutInfo
    ): DomainResult<Unit> {
        placeOrderCallCount++
        return if (shouldReturnError) {
            DomainResult.Error("Error placing order", ErrorType.UNKNOWN)
        } else {
            cartItems.clear()
            DomainResult.Success(Unit)
        }
    }

    override fun getCurrentUserId(): String? {
        return currentUserId
    }
}
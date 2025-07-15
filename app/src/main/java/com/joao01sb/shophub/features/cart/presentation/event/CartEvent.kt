package com.joao01sb.shophub.features.cart.presentation.event

import com.joao01sb.shophub.core.domain.model.CartItem

sealed class CartEvent {
    data class UpdateCartItem(val cartItem: CartItem, val quantity: Int) : CartEvent()
    data class RemoveCartItem(val cartItem: CartItem) : CartEvent()
    data object ClearCart : CartEvent()
    data object NavigateToCheckout : CartEvent()
    data object Back : CartEvent()
    data object Retry : CartEvent()
}
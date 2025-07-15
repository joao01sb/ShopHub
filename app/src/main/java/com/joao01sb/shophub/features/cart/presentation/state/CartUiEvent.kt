package com.joao01sb.shophub.features.cart.presentation.state

sealed class CartUiEvent {
    data object Back : CartUiEvent()
    data object Checkout : CartUiEvent()
}
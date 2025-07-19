package com.joao01sb.shophub.features.cart.presentation.state

sealed class CheckoutUiEvent {
    data object Back : CheckoutUiEvent()
    data object Finaly : CheckoutUiEvent()
    data class Error(val message: String) : CheckoutUiEvent()
}
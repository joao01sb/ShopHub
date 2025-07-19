package com.joao01sb.shophub.features.cart.presentation.state

import com.joao01sb.shophub.core.domain.model.CartItem

data class CheckoutUiState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val fullName: String = "",
    val phone: String = "",
    val itens: List<CartItem> = emptyList(),
    val isLoading: Boolean = false
)
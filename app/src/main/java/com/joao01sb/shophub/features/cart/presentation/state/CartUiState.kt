package com.joao01sb.shophub.features.cart.presentation.state

import com.joao01sb.shophub.core.domain.model.CartItem

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val cart: List<CartItem>) : CartUiState()
    data class Error(val message: String) : CartUiState()
}
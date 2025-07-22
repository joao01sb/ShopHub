package com.joao01sb.shophub.features.orders.presentation.state

import com.joao01sb.shophub.core.domain.model.Order

sealed class OrderDetailsUiState {
    data class Success(val order: Order) : OrderDetailsUiState()
    data class Error(val message: String) : OrderDetailsUiState()
    object Loading : OrderDetailsUiState()
}
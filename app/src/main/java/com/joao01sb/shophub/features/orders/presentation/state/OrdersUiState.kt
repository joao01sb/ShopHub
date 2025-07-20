package com.joao01sb.shophub.features.orders.presentation.state

import com.joao01sb.shophub.core.domain.model.Order

sealed class OrdersUiState {
    data class Success(val orders: List<Order>) : OrdersUiState()
    data class Error(val message: String) : OrdersUiState()
    object Loading : OrdersUiState()
}
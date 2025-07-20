package com.joao01sb.shophub.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.features.orders.domain.usecase.GetOrdersUseCase
import com.joao01sb.shophub.features.orders.presentation.state.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val authManager: AuthManager
) : ViewModel(){

    private var userId: String? = null

    init {
        authManager.getCurrentUserId()
            .onSuccess {
                userId = it
            }
            .onFailure {

            }
        viewModelScope.launch {
            getOrders()
        }
    }

    private val _ordersUiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val ordersUiState: StateFlow<OrdersUiState> = _ordersUiState.asStateFlow()

    suspend fun getOrders() {
        userId?.let { id ->
            getOrdersUseCase(id)
                .onSuccess { orders ->
                    _ordersUiState.value = OrdersUiState.Success(orders)
                }
                .onFailure { error ->
                    _ordersUiState.value = OrdersUiState.Error(error.message ?: "Unknown error")
                }
        } ?: run {
            _ordersUiState.value = OrdersUiState.Error("User not authenticated")
        }
    }

}
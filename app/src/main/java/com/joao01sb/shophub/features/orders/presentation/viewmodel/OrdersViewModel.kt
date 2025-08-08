package com.joao01sb.shophub.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.orders.domain.usecase.GetOrdersUseCase
import com.joao01sb.shophub.features.orders.presentation.event.OrderUiEvent
import com.joao01sb.shophub.features.orders.presentation.state.OrdersEvent
import com.joao01sb.shophub.features.orders.presentation.state.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
                userId = null
            }
        viewModelScope.launch {
            getOrders()
        }
    }

    private val _ordersUiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val ordersUiState: StateFlow<OrdersUiState> = _ordersUiState.asStateFlow()

    private val _orderUiEvent = MutableSharedFlow<OrderUiEvent>(replay = 1)
    val orderUiEvent = _orderUiEvent.asSharedFlow()

    suspend fun getOrders() {
        userId?.let { id ->
            when(val result = getOrdersUseCase(id)) {
                is DomainResult.Success -> {
                    _ordersUiState.value = OrdersUiState.Success(result.data)
                }
                is DomainResult.Error -> {
                    _ordersUiState.value = OrdersUiState.Error(result.message ?: "Unknown error")
                }
            }
        } ?: run {
            _ordersUiState.value = OrdersUiState.Error("User not authenticated")
        }
    }

    fun onEvent(event: OrdersEvent) {
        when (event) {
            is OrdersEvent.RefreshOrders -> {
                _ordersUiState.value = OrdersUiState.Loading
                viewModelScope.launch {
                    getOrders()
                }
            }
            is OrdersEvent.Logout -> {
                viewModelScope.launch {

                    when(val result = authManager.logoutUser()) {
                        is DomainResult.Success -> {
                            _orderUiEvent.tryEmit(OrderUiEvent.Logout)
                        }
                        is DomainResult.Error -> {
                            _ordersUiState.value = OrdersUiState.Error("Erro ao fazer logout: ${result.message}")
                        }
                    }
                }
            }
        }
    }

}
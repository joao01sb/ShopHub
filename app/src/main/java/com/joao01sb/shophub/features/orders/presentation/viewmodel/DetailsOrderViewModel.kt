package com.joao01sb.shophub.features.orders.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.features.orders.domain.usecase.GetOrderByIdUseCase
import com.joao01sb.shophub.features.orders.presentation.state.DetailsOrderEvent
import com.joao01sb.shophub.features.orders.presentation.state.OrderDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsOrderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"]) { "Missing id argument" }

    private var userId: String? = null

    init {
        authManager.getCurrentUserId()
            .onSuccess { id ->
                userId = id
            }
            .onFailure { error ->
                throw IllegalStateException("Failed to get user ID: ${error.message}")
            }

        viewModelScope.launch {
            getOrderDetails()
        }
    }

    fun onEvent(event: DetailsOrderEvent) {
        when (event) {
            is DetailsOrderEvent.RefreshOrder -> {
                viewModelScope.launch {
                    getOrderDetails()
                }
            }
        }
    }

    private val _orderDetailsUiState = MutableStateFlow<OrderDetailsUiState>(OrderDetailsUiState.Loading)
    val orderDetailsUiState = _orderDetailsUiState.asStateFlow()


    suspend fun getOrderDetails() {
        userId?.let { id ->
            getOrderByIdUseCase(id, orderId)
                .onSuccess { order ->
                    _orderDetailsUiState.value = OrderDetailsUiState.Success(order)
                }
                .onFailure { error ->
                    _orderDetailsUiState.value = OrderDetailsUiState.Error(error.message ?: "Unknown error")
                }
        } ?: run {
            _orderDetailsUiState.value = OrderDetailsUiState.Error("User not authenticated")
        }
    }
}
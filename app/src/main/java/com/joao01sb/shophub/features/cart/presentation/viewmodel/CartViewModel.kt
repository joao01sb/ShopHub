package com.joao01sb.shophub.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.features.cart.presentation.event.CartEvent
import com.joao01sb.shophub.features.cart.presentation.state.CartUiEvent
import com.joao01sb.shophub.features.cart.presentation.state.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartManager: CartManager,
    private val authManager: AuthManager
) : ViewModel() {

    private var userId: String? = null

    private val _uiEvent = MutableSharedFlow<CartUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _cartItems = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val cartItems: StateFlow<CartUiState> = _cartItems.asStateFlow()

    init {
        authManager.getCurrentUserId()
            .onSuccess {
                userId = it
            }
            .onFailure {

            }

        viewModelScope.launch {
            userId?.let {
                loadProducts(it)
            }
        }
    }

    private suspend fun loadProducts(string: String) {
        cartManager.getItems(string)
            .catch {
                _cartItems.update {
                    CartUiState.Error("Error fetching cart items")
                }
            }.collect { items ->
                _cartItems.update {
                    CartUiState.Success(items)
                }
            }
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.UpdateCartItem -> {
                userId?.let {
                    viewModelScope.launch {
                        cartManager.addItem(it, event.cartItem, event.quantity)
                    }
                }
            }

            is CartEvent.RemoveCartItem -> {
                userId?.let {
                    viewModelScope.launch {
                        cartManager.removeItem(it, event.cartItem.productId.toString())
                    }
                }
            }

            is CartEvent.ClearCart -> {
                userId?.let {
                    viewModelScope.launch {
                        cartManager.clearCart(it)
                    }
                }
            }

            is CartEvent.NavigateToCheckout -> {
                viewModelScope.launch {
                    _uiEvent.emit(CartUiEvent.Checkout)
                }
            }

            CartEvent.Back -> {
                viewModelScope.launch {
                    _uiEvent.emit(CartUiEvent.Back)
                }
            }

            CartEvent.Retry -> {
                userId?.let { userId ->
                    viewModelScope.launch {
                        loadProducts(userId)
                    }
                }
            }
        }
    }
}

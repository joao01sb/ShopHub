package com.joao01sb.shophub.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.features.cart.domain.usecase.UpdateItemUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.ClearCartUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.GetCartItemsUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.GetCurrentUserIdLoggedUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.RemoveCartItemUseCase
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
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdLoggedUseCase,
) : ViewModel() {

    private var userId: String? = null

    private val _uiEvent = MutableSharedFlow<CartUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _cartItems = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val cartItems: StateFlow<CartUiState> = _cartItems.asStateFlow()


    init {
        getCurrentUserIdUseCase()
            .onSuccess {
                userId = it
            }
            .onFailure {

            }

        viewModelScope.launch {
            userId?.let {
                getCartItemsUseCase(it)
                    .catch {
                        _cartItems.update {
                            CartUiState.Error("Error fetching cart items")
                        }
                    }.collect { items ->
                        if (items.isNotEmpty()) {
                            _cartItems.update {
                                CartUiState.Success(items)
                            }
                        } else {
                            _cartItems.update {
                                CartUiState.Error("Cart is empty")
                            }
                        }
                    }
            }
        }
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.UpdateCartItem -> {
                userId?.let {
                    viewModelScope.launch {
                        updateItemUseCase(it, event.cartItem, event.quantity)
                    }
                }
            }

            is CartEvent.RemoveCartItem -> {
                userId?.let {
                    viewModelScope.launch {
                        removeCartItemUseCase(it, event.cartItem.idProduto.toString())
                    }
                }
            }

            is CartEvent.ClearCart -> {
                userId?.let {
                    viewModelScope.launch {
                        clearCartUseCase(it)
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

            }
        }
    }
}

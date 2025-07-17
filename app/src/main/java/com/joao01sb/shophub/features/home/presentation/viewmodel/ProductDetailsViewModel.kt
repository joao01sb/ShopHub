package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.core.domain.mapper.toCartItem
import com.joao01sb.shophub.features.home.domain.usecase.GetProductByIdUseCase
import com.joao01sb.shophub.features.home.presentation.event.DetailsEvent
import com.joao01sb.shophub.features.home.presentation.state.ProductDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val cartManager: CartManager,
    private val authManager: AuthManager
) : ViewModel() {

    val productId: Int = checkNotNull(savedStateHandle["idUser"]) { "Missing id argument" }

    private var userId: String = ""

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    init {
        getProductById()
        authManager.getCurrentUserId()
            .onSuccess {
                userId = it
            }
            .onFailure {

            }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.AddToCart -> addToCart()
        }
    }

    private fun addToCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            cartManager.addItem(userId, _uiState.value.product!!.toCartItem(), 1)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }

        }
    }

    private fun getProductById() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getProductByIdUseCase(productId)
                .onSuccess { product ->
                    _uiState.value = _uiState.value.copy(
                        product = product,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }
}
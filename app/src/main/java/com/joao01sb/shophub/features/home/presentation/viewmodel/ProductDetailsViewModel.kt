package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.core.domain.mapper.toCartItem
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.domain.usecase.GetProductByIdUseCase
import com.joao01sb.shophub.features.home.presentation.event.DetailsEvent
import com.joao01sb.shophub.features.home.presentation.event.DetailsUiEvent
import com.joao01sb.shophub.features.home.presentation.state.ProductDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _uiEvent: MutableSharedFlow<DetailsUiEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<DetailsUiEvent> = _uiEvent.asSharedFlow()

    init {
        getProductById()
        authManager.getCurrentUserId()
            .onSuccess {
                userId = it
            }
            .onFailure {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to retrieve user ID: ${it.message}",
                    isLoading = false
                )
            }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.AddToCart -> addToCart()
            is DetailsEvent.NavigateToCard -> {
                viewModelScope.launch {
                    _uiEvent.emit(DetailsUiEvent.NavigateToCart)
                }
            }
        }
    }

    private fun addToCart() {
        viewModelScope.launch {
            when(val result = cartManager.addItem(userId, _uiState.value.product!!.toCartItem(), 1)) {
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        isShowButtonToCart = true
                    )
                    viewModelScope.launch {
                        _uiEvent.emit(DetailsUiEvent.Success("Product added to cart"))
                    }
                }
            }
        }
    }

    private fun getProductById() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            when(val result = getProductByIdUseCase(productId)) {
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        product = result.data,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
}
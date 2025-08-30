package com.joao01sb.shophub.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.usecase.ValidateCheckoutInfoUseCase
import com.joao01sb.shophub.features.cart.presentation.event.CheckoutEvent
import com.joao01sb.shophub.features.cart.presentation.state.CheckoutUiEvent
import com.joao01sb.shophub.features.cart.presentation.state.CheckoutUiEvent.Error
import com.joao01sb.shophub.features.cart.presentation.state.CheckoutUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val validateCheckoutInfoUseCase: ValidateCheckoutInfoUseCase,
    private val cartManager: CartManager,
    private val authManager: AuthManager
) : ViewModel() {

    private var userId: String? = null

    private val _checkoutState: MutableStateFlow<CheckoutUiState> = MutableStateFlow(CheckoutUiState())
    val checkoutSate: StateFlow<CheckoutUiState> = _checkoutState.asStateFlow()

    private val _checkoutUiEvent: MutableSharedFlow<CheckoutUiEvent> = MutableSharedFlow(replay = 1)
    val checkoutUiEvent: SharedFlow<CheckoutUiEvent> = _checkoutUiEvent.asSharedFlow()

    init {
        authManager.getCurrentUserId()
            .onSuccess {
                userId = it
            }
            .onFailure {
                userId = null
            }

        viewModelScope.launch {
            userId?.let {
                cartManager.getItems(it).collect { cartItens ->
                    _checkoutState.update {
                        it.copy(itens = cartItens,)
                    }
                }
            } ?: run {
                _checkoutUiEvent.tryEmit(CheckoutUiEvent.Error("User not authenticated"))
            }
        }
    }

    fun onEvent(event: CheckoutEvent) {
        when (event) {
            is CheckoutEvent.CardCVVChanged -> _checkoutState.update {
                it.copy(cvv = event.cvv,)
            }
            is CheckoutEvent.CardNameChanged -> _checkoutState.update {
                it.copy(cardHolderName = event.name,)
            }
            is CheckoutEvent.CardNumberChanged -> _checkoutState.update {
                it.copy(cardNumber = event.number,)
            }
            is CheckoutEvent.CardValidationChanged -> _checkoutState.update {
                it.copy(expiryDate = event.validation,)
            }
            is CheckoutEvent.FullNameChanged -> _checkoutState.update {
                it.copy(fullName = event.fullName,)
            }
            is CheckoutEvent.PhoneChanged -> _checkoutState.update {
                it.copy(phone = event.phone,)
            }
            is CheckoutEvent.Checkout -> {
                viewModelScope.launch {
                    val isValid = validateCheckoutInfoUseCase(
                        CheckoutInfo(
                            numberCard = _checkoutState.value.cardNumber,
                            nameCard = _checkoutState.value.cardHolderName,
                            dateCard = _checkoutState.value.expiryDate,
                            cvvCard = _checkoutState.value.cvv,
                            fullName = _checkoutState.value.fullName,
                            phoneNumber = _checkoutState.value.phone
                        )
                    )
                    if (isValid) {
                        _checkoutState.update {
                            it.copy(isLoading = true)
                        }
                        placerOrder()
                    } else {
                        viewModelScope.launch {
                            _checkoutUiEvent.tryEmit(CheckoutUiEvent.Error("invalid checkout information"))
                        }
                    }
                }
            }
        }
    }

    private suspend fun placerOrder() {
        try {
            when (val result = cartManager.placeOrder(userId!!, _checkoutState.value.itens, CheckoutInfo(
                numberCard = _checkoutState.value.cardNumber,
                nameCard = _checkoutState.value.cardHolderName,
                dateCard = _checkoutState.value.expiryDate,
                cvvCard = _checkoutState.value.cvv,
                fullName = _checkoutState.value.fullName,
                phoneNumber = _checkoutState.value.phone
            ))) {
                is DomainResult.Success -> {
                    _checkoutState.update {
                        it.copy(isLoading = false)
                    }
                    _checkoutUiEvent.tryEmit(CheckoutUiEvent.Finaly)
                }
                is DomainResult.Error -> {
                    _checkoutState.update {
                        it.copy(isLoading = false)
                    }
                    viewModelScope.launch {
                        _checkoutUiEvent.tryEmit(Error(result.message ?: "Unknow error"))
                    }
                }
            }
        } catch (e: Exception) {
            _checkoutState.update {
                it.copy(isLoading = false)
            }
            _checkoutUiEvent.tryEmit(CheckoutUiEvent.Error(e.message ?: "Unknow error"))
        }
    }

}
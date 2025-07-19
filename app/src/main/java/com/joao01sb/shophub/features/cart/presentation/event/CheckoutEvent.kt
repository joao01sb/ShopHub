package com.joao01sb.shophub.features.cart.presentation.event

import com.joao01sb.shophub.features.auth.presentation.event.AuthEvent

sealed class CheckoutEvent {
    data class CardNumberChanged(val number: String) : CheckoutEvent()
    data class CardNameChanged(val name: String) : CheckoutEvent()
    data class CardValidationChanged(val validation: String) : CheckoutEvent()
    data class CardCVVChanged(val cvv: String) : CheckoutEvent()
    data class FullNameChanged(val fullName: String) : CheckoutEvent()
    data class PhoneChanged(val phone: String) : CheckoutEvent()
    data object Checkout : CheckoutEvent()
}
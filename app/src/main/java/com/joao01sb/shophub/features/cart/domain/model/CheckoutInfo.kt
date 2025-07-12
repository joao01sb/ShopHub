package com.joao01sb.shophub.features.cart.domain.model

data class CheckoutInfo(
    val numberCard: String = "",
    val nameCard: String = "",
    val dateCard: String = "",
    val cvvCard: String = "",
    val fullName: String = "",
    val phoneNumber: String = ""
)
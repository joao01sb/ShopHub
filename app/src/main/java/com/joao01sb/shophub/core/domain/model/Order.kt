package com.joao01sb.shophub.core.domain.model

import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val status: String,
    val createdAt: Long,
    val paymentInfo: CheckoutInfo
)

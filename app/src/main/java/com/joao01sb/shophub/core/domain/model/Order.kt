package com.joao01sb.shophub.core.domain.model

import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val status: OrderStatus,
    val createdAt: Long,
    val paymentInfo: CheckoutInfo
)
